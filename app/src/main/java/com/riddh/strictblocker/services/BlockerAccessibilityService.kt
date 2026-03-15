package com.riddh.strictblocker.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
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

    private var emergencyOverlayView: android.view.View? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        
        // ... (existing code)
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

        // Emergency Timer Monitor
        serviceScope.launch(Dispatchers.Main) {
            while (true) {
                val timerEnd = prefs.getLong("emergency_timer_end", 0L)
                val now = System.currentTimeMillis()
                
                if (timerEnd > 0 && now < timerEnd) {
                    showEmergencyOverlay(timerEnd - now)
                } else if (emergencyOverlayView != null) {
                    removeEmergencyOverlay()
                }
                delay(1000)
            }
        }
    }

    private fun showEmergencyOverlay(remainingMillis: Long) {
        if (emergencyOverlayView == null) {
            windowManager = getSystemService(Context.WINDOW_SERVICE) as android.view.WindowManager
            val params = android.view.WindowManager.LayoutParams(
                android.view.WindowManager.LayoutParams.MATCH_PARENT,
                android.view.WindowManager.LayoutParams.MATCH_PARENT,
                android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                android.graphics.PixelFormat.TRANSLUCENT
            )

            val layout = android.widget.LinearLayout(this).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                gravity = android.view.Gravity.CENTER
                setBackgroundColor(android.graphics.Color.parseColor("#1A1A1A"))
                setPadding(64, 64, 64, 64)
            }

            emergencyOverlayView = layout
            windowManager?.addView(emergencyOverlayView, params)
        }

        val layout = emergencyOverlayView as? android.widget.LinearLayout ?: return
        layout.removeAllViews()

        val seconds = (remainingMillis / 1000) % 60
        val minutes = (remainingMillis / (1000 * 60)) % 60
        val timeStr = String.format("%02d:%02d", minutes, seconds)

        val timerText = android.widget.TextView(this).apply {
            text = "Emergency Unlocking in\n$timeStr"
            textSize = 32f
            setTextColor(android.graphics.Color.WHITE)
            gravity = android.view.Gravity.CENTER
            setTypeface(null, android.graphics.Typeface.BOLD)
        }
        layout.addView(timerText)

        // Study Progress
        val startTime = prefs.getLong("session_start_time", 0L)
        if (startTime > 0) {
            val studyMillis = System.currentTimeMillis() - startTime
            val studyHours = studyMillis / (1000 * 60 * 60)
            val studyMins = (studyMillis / (1000 * 60)) % 60
            
            val progressText = android.widget.TextView(this).apply {
                text = "\n\nYou have focused for ${studyHours}h ${studyMins}m today.\nDon't let it go to waste."
                textSize = 16f
                setTextColor(android.graphics.Color.GRAY)
                gravity = android.view.Gravity.CENTER
            }
            layout.addView(progressText)
        }

        // CA Final Countdown
        val examDate = prefs.getLong("ca_exam_date", 0L)
        if (examDate > 0) {
            val days = (examDate - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)
            val examText = android.widget.TextView(this).apply {
                text = "\n$days Days until CA Final Exam"
                textSize = 18f
                setTextColor(android.graphics.Color.parseColor("#CF6679"))
                gravity = android.view.Gravity.CENTER
                setTypeface(null, android.graphics.Typeface.BOLD)
            }
            layout.addView(examText)
        }

        val cancelButton = android.widget.Button(this).apply {
            text = "I AM OKAY NOW (CANCEL EXIT)"
            setBackgroundColor(android.graphics.Color.parseColor("#4A90E2"))
            setTextColor(android.graphics.Color.WHITE)
            setOnClickListener {
                prefs.edit().putLong("emergency_timer_end", 0L).apply()
                removeEmergencyOverlay()
            }
        }
        val btnParams = android.widget.LinearLayout.LayoutParams(
            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 100, 0, 0) }
        layout.addView(cancelButton, btnParams)
        
        // Redirect to home if they try to leave
        val startMain = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(startMain)
    }

    private fun removeEmergencyOverlay() {
        if (emergencyOverlayView != null) {
            windowManager?.removeView(emergencyOverlayView)
            emergencyOverlayView = null
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
            val allText = extractAllText(rootInActiveWindow).lowercase()
            val className = event.className?.toString() ?: ""
            
            val isDeviceAdmin = className == "com.android.settings.DeviceAdminSettings" || 
                                className == "com.android.settings.Settings\$DeviceAdminSettingsActivity" ||
                                allText.contains("device admin") || 
                                allText.contains("deactivate") || 
                                allText.contains("device administrators")
                                
            val isAppInfoScreen = className == "com.android.settings.applications.InstalledAppDetailsTop" ||
                                  (packagesToCheck.any { it.contains("com.android.settings") } && allText.contains("com.riddh.strictblocker"))

            val isStrictBlockerAppInfo = isAppInfoScreen ||
                allText.contains("com.riddh.strictblocker") ||
                (allText.contains("strict blocker") && 
                (allText.contains("force stop") || allText.contains("uninstall") || allText.contains("clear data") || allText.contains("storage")))
            
            val isDateTimeSettings = (allText.contains("date and time") || allText.contains("date & time") || allText.contains("set time")) && 
                (allText.contains("automatic") || allText.contains("time zone"))

            if (isStrictBlockerAppInfo || isDateTimeSettings || isDeviceAdmin || packagesToCheck.any { it.contains("packageinstaller") }) {
                logAndBlock("Settings/Uninstaller", "UNINSTALL_ATTEMPT")
                return
            }
        }

        // 1. App Blocking
        for (pkg in packagesToCheck) {
            if (blockedApps.any { it.packageName == pkg }) {
                logAndBlock(pkg, "APP_OPEN")
                return
            }
        }

        // Determine the best node to search for URLs
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
                        val cleanUrl = currentUrl.removePrefix("https://").removePrefix("http://")
                        val host = cleanUrl.substringBefore("/")
                        
                        if (blockedUrls.any { host.contains(it.url.lowercase()) }) {
                            logAndBlock(currentUrl, "URL_DETECTED")
                            return
                        }
                    }
                }
            }

            // Keyword detection (completely separate from URL detection)
            for (node in nodesToSearch) {
                if (scanNodesForKeywords(node, pkg)) {
                    logAndBlock(pkg, "KEYWORD")
                    return
                }
            }
        }
    }

    private fun extractAllText(node: AccessibilityNodeInfo?): String {
        if (node == null) return ""
        val text = StringBuilder()
        if (node.text != null) text.append(node.text).append(" ")
        if (node.contentDescription != null) text.append(node.contentDescription).append(" ")
        for (i in 0 until node.childCount) {
            text.append(extractAllText(node.getChild(i)))
        }
        return text.toString()
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
        val text = node.text?.toString()?.lowercase()?.trim() ?: ""
        val contentDesc = node.contentDescription?.toString()?.lowercase() ?: ""
        val className = node.className?.toString() ?: ""
        
        // A valid URL/domain must contain a dot and have no spaces.
        // This prevents search keywords (like "youtube") from being falsely flagged as URLs.
        if (text.contains(".") && !text.contains(" ") && text.length > 3) {
            val isAddressBar = className.contains("EditText") || node.isEditable
            val hasUrlDesc = contentDesc.contains("address") || contentDesc.contains("url")
            val isUrlFormat = text.matches(Regex("^(https?://)?([a-z0-9-]+\\.)+[a-z]{2,}(/.*)?$"))
            
            if (isAddressBar || hasUrlDesc || isUrlFormat) {
                return text
            }
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

    private var overlayView: android.view.View? = null
    private var windowManager: android.view.WindowManager? = null

    private fun launchBlockScreen() {
        if (overlayView != null) return
        
        windowManager = getSystemService(Context.WINDOW_SERVICE) as android.view.WindowManager
        val params = android.view.WindowManager.LayoutParams(
            android.view.WindowManager.LayoutParams.MATCH_PARENT,
            android.view.WindowManager.LayoutParams.MATCH_PARENT,
            android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
            android.graphics.PixelFormat.TRANSLUCENT
        )
        
        val linearLayout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
            setBackgroundColor(android.graphics.Color.parseColor("#121212"))
            
            val textView = android.widget.TextView(context).apply {
                text = "Focus Mode Active\n\nTake a deep breath and return to your focus."
                textSize = 24f
                setTextColor(android.graphics.Color.WHITE)
                gravity = android.view.Gravity.CENTER
                setPadding(64, 64, 64, 64)
            }
            addView(textView)
        }
        
        overlayView = linearLayout
        windowManager?.addView(overlayView, params)
        
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
            kotlinx.coroutines.delay(5000)
            if (overlayView != null) {
                windowManager?.removeView(overlayView)
                overlayView = null
            }
        }
        
        val startMain = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(startMain)
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
