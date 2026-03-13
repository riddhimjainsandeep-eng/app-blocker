package com.riddh.strictblocker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riddh.strictblocker.data.AuditReport
import com.riddh.strictblocker.data.BlockerDatabase
import com.riddh.strictblocker.ui.theme.DarkTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MorningAuditActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            DarkTheme {
                MorningAuditScreen(onDismiss = { finish() })
            }
        }
    }
}

@Composable
fun MorningAuditScreen(onDismiss: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    var report by remember { mutableStateOf<AuditReport?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val db = BlockerDatabase.getDatabase(context).blockerDao()
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        report = db.getReportForDate(today)
        isLoading = false
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (report == null) {
            // No report yet (maybe no breaches yesterday?)
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Morning Audit", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text("No data to analyze from yesterday. Keep up the good work!", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = onDismiss) { Text("CLOSE") }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Yesterday's Reflection",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            StatItem("Breaches", report!!.totalBreaches.toString())
                            StatItem("Peak Time", report!!.peakTime)
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Text(
                            text = "Psychological Analysis",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = report!!.analysisText,
                            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Button(
                    onClick = {
                        scope.launch {
                            val analyst = com.riddh.strictblocker.services.BehavioralAnalyst(context)
                            val newReport = analyst.generateDailyAudit()
                            if (newReport != null) {
                                android.widget.Toast.makeText(context, "Email report sent!", android.widget.Toast.LENGTH_SHORT).show()
                            } else {
                                android.widget.Toast.makeText(context, "No breaches to report!", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("SEND EMAIL REPORT NOW")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("I UNDERSTAND")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(onClick = { /* Share logic */ }) {
                    Text("SEND TO ARCHIVE", color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        Text(text = value, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
    }
}
