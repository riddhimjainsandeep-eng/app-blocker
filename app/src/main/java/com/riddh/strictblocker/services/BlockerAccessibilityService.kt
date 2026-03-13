package com.riddh.strictblocker.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.riddh.strictblocker.BlockOverlayActivity
import com.riddh.strictblocker.data.BlockerDatabase
import com.riddh.strictblocker.data.BlockedApp
import com.riddh.strictblocker.data.BlockedUrl
import com.riddh.strictblocker.data.BlockedKeyword
import com.riddh.strictblocker.data.BreachLog
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class BlockerAccessibilityService : AccessibilityService(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    
    private var blockedApps = listOf<BlockedApp>()
    private var blockedUrls = listOf<BlockedUrl>()
    private var blockedKeywords = listOf<BlockedKeyword>()

    private var lastBreachTime = 0L
    private var currentFrustrationCount = 0
    private var lastBreachTarget = ""
    
    private var isActive = false
    private lateinit var prefs: SharedPreferences

    override fun onServiceConnected() {
        super.onServiceConnected()
        
        // Programmatically set configuration to ensure the service runs aggressively
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
            notificationTimeout = 150 // Throttle slightly to prevent OS from killing the service for event spam
        }
        serviceInfo = info

        prefs = getSharedPreferences("blocker_prefs", Context.MODE_PRIVATE)
        isActive = prefs.getBoolean("is_active", false)
        prefs.registerOnSharedPreferenceChangeListener(this)
        
        serviceScope.launch(Dispatchers.IO) {
            val db = BlockerDatabase.getDatabase(applicationContext).blockerDao()
            launch { db.getAllApps().collectLatest { blockedApps = it } }
            launch { db.getAllUrls().collectLatest { blockedUrls = it } }
            launch { db.getAllKeywords().collectLatest { blockedKeywords = it } }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "is_active") {
            isActive = sharedPreferences?.getBoolean("is_active", false) ?: false
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (!isActive || event == null) return

        val activeRootPackage = rootInActiveWindow?.packageName?.toString() ?: ""
        val eventPackage = event.packageName?.toString() ?: ""

        // Fast fail: never block our own app or overlay
        if (eventPackage == "com.riddh.strictblocker" || activeRootPackage == "com.riddh.strictblocker") return

        val packagesToCheck = listOf(activeRootPackage, eventPackage).filter { it.isNotBlank() }.distinct()

        // 0. Prevent Tampering & Uninstallation (Only during active session)
        val sensitivePackages = listOf(
            "com.google.android.packageinstaller", 
            "com.android.packageinstaller", 
            "com.samsung.android.packageinstaller",
            "com.android.settings",
            "com.samsung.android.settings.accessibility",
            "com.miui.securitycenter"
        )
        if (packagesToCheck.any { it in sensitivePackages }) {
            val nodesToSearch = listOfNotNull(rootInActiveWindow, event.source)
            for (node in nodesToSearch) {
                if (scanForTampering(node)) {
                    logAndBlock("Settings/Uninstaller", "UNINSTALL_ATTEMPT")
                    return
                }
            }
        }

        // 1. App Blocking
        for (pkg in packagesToCheck) {
            if (blockedApps.any { it.packageName == pkg }) {
                logAndBlock(pkg, "APP_OPEN")
                return
            }
        }

        // Determine the best node to search for URLs and Keywords
        // rootInActiveWindow is usually best, but event.source is a fallback
        val nodesToSearch = listOfNotNull(rootInActiveWindow, event.source)

        // 2. Aggressive URL & Keyword Sniffing
        for (pkg in packagesToCheck) {
            val isBrowserOrGoogle = pkg.contains("chrome") || 
                                    pkg.contains("browser") || 
                                    pkg.contains("search") || 
                                    pkg == "com.google.android.googlequicksearchbox"

            if (isBrowserOrGoogle) {
                for (node in nodesToSearch) {
                    val currentUrl = findUrlInNodes(node)
                    if (currentUrl != null) {
                        if (blockedUrls.any { currentUrl.contains(it.url.lowercase()) }) {
                            logAndBlock(currentUrl, "URL_DETECTED")
                            return
                        }
                    }
                }
            }

            for (node in nodesToSearch) {
                if (scanNodesForKeywords(node, pkg)) {
                    logAndBlock(pkg, "KEYWORD")
                    return
                }
            }
        }
    }

    private fun logAndBlock(target: String, type: String) {
        val now = System.currentTimeMillis()
        if (target == lastBreachTarget && (now - lastBreachTime) < 10000) {
            currentFrustrationCount++
        } else {
            if (lastBreachTarget.isNotEmpty()) saveBreachToDb(lastBreachTarget, lastBreachTime, type, currentFrustrationCount)
            lastBreachTarget = target
            currentFrustrationCount = 1
        }
        lastBreachTime = now
        launchBlockScreen()
    }

    private fun saveBreachToDb(target: String, time: Long, type: String, count: Int) {
        serviceScope.launch(Dispatchers.IO) {
            val db = BlockerDatabase.getDatabase(applicationContext).blockerDao()
            db.insertBreach(BreachLog(timestamp = time, targetApp = target, breachType = type, frustrationCount = count))
        }
    }

    private fun findUrlInNodes(node: AccessibilityNodeInfo): String? {
        val text = node.text?.toString() ?: ""
        val contentDesc = node.contentDescription?.toString() ?: ""
        val className = node.className?.toString() ?: ""
        
        // Match anything that looks like a domain or has a dot and no spaces
        if (className.contains("EditText") || node.isEditable) {
            val potentialUrl = text.lowercase().trim()
            if (potentialUrl.contains(".") && !potentialUrl.contains(" ") && potentialUrl.length > 3) {
                return potentialUrl
            }
        }
        
        // Also check Google App Custom Tab titles which might contain the domain
        val isLikelyTitleOrUrl = contentDesc.lowercase().contains("address") || 
                                 contentDesc.lowercase().contains("url") ||
                                 text.lowercase().trim().contains(".")

        if (isLikelyTitleOrUrl && !text.contains(" ")) {
            val potentialUrl = text.lowercase().trim()
            if (potentialUrl.isNotEmpty()) return potentialUrl
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            val found = findUrlInNodes(child)
            if (found != null) return found
        }
        return null
    }

    private fun scanForTampering(node: AccessibilityNodeInfo): Boolean {
        val text = node.text?.toString()?.lowercase() ?: ""
        val contentDesc = node.contentDescription?.toString()?.lowercase() ?: ""
        
        // Block interaction with Strict Blocker settings, and prevent Date/Time manipulation
        if (text.contains("strict blocker") || contentDesc.contains("strict blocker") || 
            text.contains("strict blocker engine") || contentDesc.contains("strict blocker engine") ||
            text == "date and time" || text == "date & time" || text == "set time") {
            return true
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            if (scanForTampering(child)) {
                return true
            }
        }
        return false
    }

    private fun scanNodesForKeywords(node: AccessibilityNodeInfo, currentPackage: String): Boolean {
        val text = node.text?.toString()?.lowercase() ?: ""
        for (kw in blockedKeywords) {
            val k = kw.keyword
            // Exact match or isolated word match, to avoid partial matches triggering false positives
            if (text.isNotBlank() && (text == k || text.contains(" $k ") || text.startsWith("$k ") || text.endsWith(" $k"))) return true
        }
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            if (scanNodesForKeywords(child, currentPackage)) return true
        }
        return false
    }

    private fun launchBlockScreen() {
        // Step 2: Launch the block overlay screen
        val intent = Intent(this, BlockOverlayActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
    }

    override fun onInterrupt() {}
    
    override fun onDestroy() {
        if (lastBreachTarget.isNotEmpty()) saveBreachToDb(lastBreachTarget, lastBreachTime, "FINAL", currentFrustrationCount)
        if (::prefs.isInitialized) {
            prefs.unregisterOnSharedPreferenceChangeListener(this)
        }
        super.onDestroy()
        serviceJob.cancel()
    }
}
