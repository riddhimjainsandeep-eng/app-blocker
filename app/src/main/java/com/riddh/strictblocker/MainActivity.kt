package com.riddh.strictblocker

import android.app.AppOpsManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riddh.strictblocker.receivers.AdminReceiver
import com.riddh.strictblocker.services.BlockerAccessibilityService
import com.riddh.strictblocker.services.BlockerForegroundService
import com.riddh.strictblocker.ui.BlockerViewModel
import com.riddh.strictblocker.ui.BlockerViewModelFactory
import com.riddh.strictblocker.ui.SelectionScreen
import com.riddh.strictblocker.ui.theme.DarkTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val viewModel: BlockerViewModel by viewModels {
        BlockerViewModelFactory((application as BlockerApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentScreen by remember { mutableStateOf("dashboard") }

            DarkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (currentScreen) {
                        "dashboard" -> DashboardScreen(
                            onNavigateToSelection = { currentScreen = "selection" }
                        )
                        "selection" -> SelectionScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = "dashboard" }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onNavigateToSelection: () -> Unit) {
    val context = LocalContext.current
    
    val prefs = remember { context.getSharedPreferences("blocker_prefs", Context.MODE_PRIVATE) }
    var hasUsageStatsPermission by remember { mutableStateOf(checkUsageStatsPermission(context)) }
    var hasAccessibilityPermission by remember { mutableStateOf(checkAccessibilityPermission(context)) }
    var isAdminActive by remember { mutableStateOf(checkAdminActive(context)) }
    var hasOverlayPermission by remember { mutableStateOf(checkOverlayPermission(context)) }
    
    var isActive by remember { mutableStateOf(prefs.getBoolean("is_active", false)) }
    var sessionEndTime by remember { mutableLongStateOf(prefs.getLong("session_end_time", 0L)) }
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    var showDatePicker by remember { mutableStateOf(false) }

    // Update state polling
    LaunchedEffect(Unit) {
        while(true) {
            hasUsageStatsPermission = checkUsageStatsPermission(context)
            hasAccessibilityPermission = checkAccessibilityPermission(context)
            isAdminActive = checkAdminActive(context)
            hasOverlayPermission = checkOverlayPermission(context)
            isActive = prefs.getBoolean("is_active", false)
            sessionEndTime = prefs.getLong("session_end_time", 0L)
            currentTime = System.currentTimeMillis()

            if (isActive && currentTime >= sessionEndTime && sessionEndTime != 0L) {
                // Time has expired, automatically deactivate
                prefs.edit()
                    .putBoolean("is_active", false)
                    .putLong("session_end_time", 0L)
                    .apply()
                isActive = false
                context.stopService(Intent(context, BlockerForegroundService::class.java))
            }
            delay(1000) 
        }
    }

    val permissionsGranted = hasUsageStatsPermission && hasAccessibilityPermission && isAdminActive && hasOverlayPermission

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // TOP SECTION: Hero
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { 
                context.startActivity(Intent(context, MorningAuditActivity::class.java))
            }) {
                Icon(
                    Icons.Default.Analytics,
                    contentDescription = "Audit",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Strict Blocker", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isActive) MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (isActive) "System Locked" else if (permissionsGranted) "Ready to Lock" else "Setup Required",
                        color = if (isActive || permissionsGranted) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            IconButton(onClick = onNavigateToSelection) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Vault",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // MIDDLE SECTION
        if (!permissionsGranted) {
            SetupCard(context, hasAccessibilityPermission, hasUsageStatsPermission, isAdminActive, hasOverlayPermission)
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isActive) "BLOCKING IS ACTIVE" else "BLOCKING IS PAUSED",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                    color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                if (isActive) {
                    val dateFormat = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
                    val dateString = dateFormat.format(java.util.Date(sessionEndTime))
                    Text(
                        text = "System strictly locked until end of:\n$dateString",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "There is no disable button. Wait for the time to expire.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                } else {
                    Text(
                        text = "Select a date to activate the strict block. Once locked, it cannot be undone until the date passes.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // BOTTOM SECTION
        if (permissionsGranted && !isActive) {
            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth().height(72.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, 
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "ACTIVATE STRICT BLOCK", 
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp, letterSpacing = 1.sp)
                )
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = currentTime
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { selected ->
                        // Set expiration to 23:59:59 of the selected date to cover the whole day
                        val calendar = java.util.Calendar.getInstance()
                        calendar.timeInMillis = selected
                        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23)
                        calendar.set(java.util.Calendar.MINUTE, 59)
                        calendar.set(java.util.Calendar.SECOND, 59)
                        val endTime = calendar.timeInMillis

                        prefs.edit()
                            .putLong("session_end_time", endTime)
                            .putBoolean("is_active", true)
                            .apply()
                        
                        val serviceIntent = Intent(context, BlockerForegroundService::class.java)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            context.startForegroundService(serviceIntent)
                        } else {
                            context.startService(serviceIntent)
                        }
                    }
                    showDatePicker = false
                }) {
                    Text("LOCK SYSTEM")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("CANCEL")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun SetupCard(context: Context, hasAccessibility: Boolean, hasUsage: Boolean, isAdmin: Boolean, hasOverlay: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Required Setup", style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp), color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Complete these to enable the lock.", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Spacer(modifier = Modifier.height(24.dp))
            
            if (!hasOverlay) {
                Button(onClick = { 
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, android.net.Uri.parse("package:${context.packageName}"))
                    context.startActivity(intent)
                }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A3A3A))) {
                    Text("Grant Display Over Apps", color = Color.White)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (!hasAccessibility) {
                Button(onClick = { context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A3A3A))) {
                    Text("Grant Accessibility", color = Color.White)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (!hasUsage) {
                Button(onClick = { context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)) }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A3A3A))) {
                    Text("Grant Usage Access", color = Color.White)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (!isAdmin) {
                Button(onClick = {
                    val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, ComponentName(context, AdminReceiver::class.java))
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Required to prevent uninstallation.")
                    context.startActivity(intent)
                }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A3A3A))) {
                    Text("Secure Device (Admin)", color = Color.White)
                }
            }
        }
    }
}

fun checkOverlayPermission(context: Context): Boolean {
    return Settings.canDrawOverlays(context)
}

fun checkUsageStatsPermission(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.packageName)
    return mode == AppOpsManager.MODE_ALLOWED
}

fun checkAccessibilityPermission(context: Context): Boolean {
    var accessibilityEnabled = 0
    val service = "${context.packageName}/${com.riddh.strictblocker.services.BlockerAccessibilityService::class.java.canonicalName}"
    try {
        accessibilityEnabled = Settings.Secure.getInt(context.applicationContext.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
    } catch (e: Settings.SettingNotFoundException) {}
    val stringColonSplitter = android.text.TextUtils.SimpleStringSplitter(':')
    if (accessibilityEnabled == 1) {
        val settingValue = Settings.Secure.getString(context.applicationContext.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        if (settingValue != null) {
            stringColonSplitter.setString(settingValue)
            while (stringColonSplitter.hasNext()) {
                val accessibilityService = stringColonSplitter.next()
                if (accessibilityService.equals(service, ignoreCase = true)) return true
            }
        }
    }
    return false
}

fun checkAdminActive(context: Context): Boolean {
    val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    return dpm.isAdminActive(ComponentName(context, AdminReceiver::class.java))
}