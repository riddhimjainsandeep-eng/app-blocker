package com.riddh.strictblocker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.riddh.strictblocker.data.BlockerDatabase
import com.riddh.strictblocker.data.BreachLog
import com.riddh.strictblocker.services.EmergencyService
import kotlinx.coroutines.launch

@Composable
fun EmergencyExitDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val emergencyService = remember { EmergencyService(context) }
    val prefs = remember { context.getSharedPreferences("blocker_prefs", android.content.Context.MODE_PRIVATE) }
    
    var currentLayer by remember { mutableIntStateOf(1) } // Layer 1 is now OTP, Layer 2 is Countdown
    var otpValue by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSendingOtp by remember { mutableStateOf(false) }

    // Auto-send OTP when dialog opens
    LaunchedEffect(Unit) {
        isSendingOtp = true
        val otp = emergencyService.generateOtp()
        val success = emergencyService.sendOtpEmail(otp)
        isSendingOtp = false
        if (!success) {
            errorMessage = "No internet detected. Skipping to countdown."
            // Auto skip to countdown if email fails
            kotlinx.coroutines.delay(2000)
            currentLayer = 2
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Emergency Exit",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(24.dp))

                when (currentLayer) {
                    1 -> {
                        Text(
                            text = "A 6-digit OTP has been sent to your registered Gmail.",
                            textAlign = TextAlign.Center,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = otpValue,
                            onValueChange = { if (it.length <= 6) otpValue = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("6-Digit OTP") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if (emergencyService.validateOtp(otpValue)) {
                                    logBreach(context, "EMERGENCY_EXIT_OTP_SUCCESS")
                                    // Instant Unlock
                                    prefs.edit()
                                        .putBoolean("is_active", false)
                                        .putLong("session_end_time", 0L)
                                        .apply()
                                    onDismiss()
                                } else {
                                    errorMessage = "Invalid or expired OTP"
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            enabled = otpValue.length == 6 && !isSendingOtp
                        ) {
                            Text("Verify & Unlock Instantly")
                        }

                        if (isSendingOtp) {
                            Spacer(modifier = Modifier.height(16.dp))
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            TextButton(onClick = {
                                isSendingOtp = true
                                scope.launch {
                                    val otp = emergencyService.generateOtp()
                                    val success = emergencyService.sendOtpEmail(otp)
                                    isSendingOtp = false
                                    if (!success) errorMessage = "Failed to send email. Check internet."
                                }
                            }) {
                                Text("Resend OTP", color = Color(0xFF4A90E2))
                            }
                        }

                        TextButton(onClick = { currentLayer = 2 }) {
                            Text("No Internet? Use 15-Min Timer", color = Color.Gray)
                        }
                    }
                    2 -> {
                        Text(
                            text = "15-Minute Countdown",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCF6679)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Once started, you must wait 15 minutes. This overlay cannot be bypassed.",
                            textAlign = TextAlign.Center,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                val endTime = System.currentTimeMillis() + 15 * 60 * 1000
                                prefs.edit().putLong("emergency_timer_end", endTime).apply()
                                logBreach(context, "EMERGENCY_EXIT_TIMER_STARTED")
                                onDismiss()
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCF6679))
                        ) {
                            Text("START 15-MIN COUNTDOWN", color = Color.White)
                        }
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(errorMessage!!, color = Color(0xFFCF6679), fontSize = 12.sp, textAlign = TextAlign.Center)
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onDismiss) {
                    Text("CANCEL EMERGENCY EXIT", color = Color.Gray)
                }
            }
        }
    }
}

private fun logBreach(context: android.content.Context, type: String) {
    val db = BlockerDatabase.getDatabase(context).blockerDao()
    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
        db.insertBreach(BreachLog(timestamp = System.currentTimeMillis(), targetApp = "System", breachType = type))
    }
}
