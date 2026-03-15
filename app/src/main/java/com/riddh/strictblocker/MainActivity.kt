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
import androidx.compose.material.icons.filled.Security
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
            var showEmergencyDialog by remember { mutableStateOf(false) }

            DarkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (currentScreen) {
                        "dashboard" -> DashboardScreen(
                            onNavigateToSelection = { currentScreen = "selection" },
                            onShowEmergencyExit = { showEmergencyDialog = true }
                        )
                        "selection" -> SelectionScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = "dashboard" }
                        )
                    }

                    if (showEmergencyDialog) {
                        EmergencyExitDialog(
                            onDismiss = { showEmergencyDialog = false }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onNavigateToSelection: () -> Unit, onShowEmergencyExit: () -> Unit) {
    val context = LocalContext.current
    
    val prefs = remember { context.getSharedPreferences("blocker_prefs", Context.MODE_PRIVATE) }
    var hasUsageStatsPermission by remember { mutableStateOf(checkUsageStatsPermission(context)) }
    var hasAccessibilityPermission by remember { mutableStateOf(checkAccessibilityPermission(context)) }
    var isAdminActive by remember { mutableStateOf(checkAdminActive(context)) }
    var hasOverlayPermission by remember { mutableStateOf(checkOverlayPermission(context)) }
    var isBatteryOptimized by remember { mutableStateOf(checkBatteryOptimization(context)) }
    var hasNotificationPermission by remember { mutableStateOf(checkNotificationPermission(context)) }
    var hasPostNotificationPermission by remember { mutableStateOf(checkPostNotificationPermission(context)) }

    var isActive by remember { mutableStateOf(prefs.getBoolean("is_active", false)) }
    var sessionEndTime by remember { mutableLongStateOf(prefs.getLong("session_end_time", 0L)) }
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    var caExamDate by remember { mutableLongStateOf(prefs.getLong("ca_exam_date", 0L)) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showExamDatePicker by remember { mutableStateOf(false) }

    // Update state polling and ticker
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                hasUsageStatsPermission = checkUsageStatsPermission(context)
                hasAccessibilityPermission = checkAccessibilityPermission(context)
                isAdminActive = checkAdminActive(context)
                hasOverlayPermission = checkOverlayPermission(context)
                isBatteryOptimized = checkBatteryOptimization(context)
                hasNotificationPermission = checkNotificationPermission(context)
                hasPostNotificationPermission = checkPostNotificationPermission(context)
                isActive = prefs.getBoolean("is_active", false)
                sessionEndTime = prefs.getLong("session_end_time", 0L)
                caExamDate = prefs.getLong("ca_exam_date", 0L)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(isActive, sessionEndTime) {
        while (isActive) {
            currentTime = System.currentTimeMillis()
            
            // Check if emergency timer is running
            val emergencyTimerEnd = prefs.getLong("emergency_timer_end", 0L)
            if (emergencyTimerEnd > 0 && currentTime >= emergencyTimerEnd) {
                // Emergency timer finished, unlock app
                prefs.edit()
                    .putBoolean("is_active", false)
                    .putLong("session_end_time", 0L)
                    .putLong("emergency_timer_end", 0L)
                    .apply()
                isActive = false
                context.stopService(Intent(context, BlockerForegroundService::class.java))
                break
            }

            if (currentTime >= sessionEndTime && sessionEndTime != 0L) {
                // Time has expired, automatically deactivate
                prefs.edit()
                    .putBoolean("is_active", false)
                    .putLong("session_end_time", 0L)
                    .apply()
                isActive = false
                context.stopService(Intent(context, BlockerForegroundService::class.java))
                break
            }
            delay(1000)
        }
    }

    val permissionsGranted = hasUsageStatsPermission && hasAccessibilityPermission && isAdminActive && hasOverlayPermission && isBatteryOptimized && hasNotificationPermission && hasPostNotificationPermission

    // Design Colors mapped to Compose Hex
    val bgColor = Color(0xFF121212)
    val surfaceColor = Color(0xFF1E1E1E)
    val accentBlue = Color(0xFF4A90E2)
    val textPrimary = Color(0xFFE0E0E0)
    val textSecondary = Color(0xFF9E9E9E)
    val accentRed = Color(0xFFCF6679)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // TOP HEADER SECTION
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                        tint = textSecondary
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "CURRENT STATUS",
                        color = textSecondary,
                        style = MaterialTheme.typography.labelMedium.copy(
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isActive) "Strict Mode Active" else if (permissionsGranted) "Ready to Focus" else "Setup Required",
                        color = textPrimary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        )
                    )
                }

                Row {
                    IconButton(onClick = { showExamDatePicker = true }) {
                        Icon(
                            Icons.Default.Security,
                            contentDescription = "Exam Date",
                            tint = if (caExamDate > 0) accentBlue else textSecondary
                        )
                    }
                    IconButton(onClick = onNavigateToSelection) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Vault",
                            tint = textSecondary
                        )
                    }
                }
            }
        }

        // HERO ELEMENT: Timer
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isActive) {
                val remainingMillis = sessionEndTime - currentTime
                val hours = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(remainingMillis)
                val minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(remainingMillis) % 60
                val seconds = java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(remainingMillis) % 60
                val timeString = String.format(java.util.Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
                
                Text(
                    text = timeString,
                    color = accentBlue,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Light
                    )
                )
                
                if (caExamDate > 0) {
                    val daysRemaining = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(caExamDate - currentTime)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "$daysRemaining days until CA Final",
                        color = accentRed,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Remaining Focus Time",
                    color = textSecondary,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else if (!permissionsGranted) {
                 Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = surfaceColor
                )
            } else {
                Text(
                    text = "00:00:00",
                    color = surfaceColor,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Light
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Select a duration to begin",
                    color = textSecondary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // MIDDLE SECTION / INFO CARDS
        if (!permissionsGranted) {
            SetupCard(context, hasAccessibilityPermission, hasUsageStatsPermission, isAdminActive, hasOverlayPermission, hasNotificationPermission)
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Shield",
                        tint = accentRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = if (isActive) "Device Locked" else "App Blocking Paused",
                            color = textPrimary,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isActive) "Social media and games disabled." else "Ready to enforce focus session.",
                            color = textSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // PRIMARY ACTION BUTTON
        if (permissionsGranted) {
            if (!isActive) {
                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentBlue, 
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "ACTIVATE STRICT BLOCK", 
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp, 
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.sp
                        )
                    )
                }
            } else {
                Button(
                    onClick = onShowEmergencyExit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = surfaceColor, 
                        contentColor = textSecondary
                    )
                ) {
                    Text(
                        text = "EMERGENCY EXIT", 
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp, 
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }

    if (showExamDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = if (caExamDate > 0) caExamDate else currentTime
        )
        DatePickerDialog(
            onDismissRequest = { showExamDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { selected ->
                        prefs.edit().putLong("ca_exam_date", selected).apply()
                        caExamDate = selected
                    }
                    showExamDatePicker = false
                }) {
                    Text("SET EXAM DATE", color = accentBlue)
                }
            }
        ) {
            DatePicker(state = datePickerState)
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
                        // Set expiration to 23:59:59 of the selected date
                        val calendar = java.util.Calendar.getInstance()
                        calendar.timeInMillis = selected
                        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23)
                        calendar.set(java.util.Calendar.MINUTE, 59)
                        calendar.set(java.util.Calendar.SECOND, 59)
                        val endTime = calendar.timeInMillis

                        prefs.edit()
                            .putLong("session_start_time", System.currentTimeMillis())
                            .putLong("session_end_time", endTime)
                            .putBoolean("is_active", true)
                            .apply()
                        
                        val serviceIntent = Intent(context, BlockerForegroundService::class.java)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            context.startForegroundService(serviceIntent)
                        } else {
                            context.startService(serviceIntent)
                        }

                        val delay = endTime - System.currentTimeMillis()
                        if (delay > 0) {
                            val workRequest = androidx.work.OneTimeWorkRequestBuilder<com.riddh.strictblocker.services.SessionEndWorker>()
                                .setInitialDelay(delay, java.util.concurrent.TimeUnit.MILLISECONDS)
                                .build()
                            androidx.work.WorkManager.getInstance(context).enqueue(workRequest)
                        }
                    }
                    showDatePicker = false
                }) {
                    Text("LOCK SYSTEM", color = accentBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("CANCEL", color = textSecondary)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun SetupCard(context: Context, hasAccessibility: Boolean, hasUsage: Boolean, isAdmin: Boolean, hasOverlay: Boolean, hasNotification: Boolean = checkNotificationPermission(context), hasPostNotification: Boolean = checkPostNotificationPermission(context)) {
    val surfaceColor = Color(0xFF1E1E1E)
    val textPrimary = Color(0xFFE0E0E0)
    val textSecondary = Color(0xFF9E9E9E)
    val buttonColor = Color(0xFF2C2C2C)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Required Setup", style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Medium), color = textPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Complete these to enable the lock.", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp), color = textSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Spacer(modifier = Modifier.height(24.dp))
            
            if (!hasOverlay) {
                Button(onClick = { 
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, android.net.Uri.parse("package:${context.packageName}"))
                    context.startActivity(intent)
                }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = buttonColor)) {
                    Text("Grant Display Over Apps", color = textPrimary)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (!hasAccessibility) {
                Button(onClick = { context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = buttonColor)) {
                    Text("Grant Accessibility", color = textPrimary)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (!hasUsage) {
                Button(onClick = { context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)) }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = buttonColor)) {
                    Text("Grant Usage Access", color = textPrimary)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (!hasNotification) {
                Button(onClick = { context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = buttonColor)) {
                    Text("Grant Notification Access", color = textPrimary)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (!hasPostNotification) {
                Button(onClick = { 
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        context.startActivity(intent)
                    }
                }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = buttonColor)) {
                    Text("Allow App Notifications", color = textPrimary)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (!checkBatteryOptimization(context)) {
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, android.net.Uri.parse("package:${context.packageName}"))
                    context.startActivity(intent)
                }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = buttonColor)) {
                    Text("Ignore Battery Optimization", color = textPrimary)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (!isAdmin) {
                Button(onClick = {
                    val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, ComponentName(context, AdminReceiver::class.java))
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Required to prevent uninstallation.")
                    context.startActivity(intent)
                }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = buttonColor)) {
                    Text("Secure Device (Admin)", color = textPrimary)
                }
            }
        }
    }
}

fun checkNotificationPermission(context: Context): Boolean {
    val listeners = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
    return listeners != null && listeners.contains(context.packageName)
}

fun checkBatteryOptimization(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
    return powerManager.isIgnoringBatteryOptimizations(context.packageName)
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

fun checkPostNotificationPermission(context: Context): Boolean {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        return androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }
    return true
}