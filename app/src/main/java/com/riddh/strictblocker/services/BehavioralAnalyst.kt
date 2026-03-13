package com.riddh.strictblocker.services

import android.content.Context
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.riddh.strictblocker.data.AuditReport
import com.riddh.strictblocker.data.BlockerDatabase
import com.riddh.strictblocker.data.BreachLog
import java.text.SimpleDateFormat
import java.util.*
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class BehavioralAnalyst(private val context: Context) {

    // API Key and Model Configuration
    private val GEMINI_API_KEY = "AIzaSyDfvWeD7QTmW4F_XNaD2tVkXIjSDvZj_W0"
    private val MODEL_NAME = "gemini-3-flash" 
    
    private val EMAIL_USER = "appblocker05@gmail.com"
    private val EMAIL_PASS = "wgvp uceh pykb avgf"

    suspend fun generateDailyAudit(): AuditReport? {
        val db = BlockerDatabase.getDatabase(context).blockerDao()
        val yesterday = System.currentTimeMillis() - (24 * 60 * 60 * 1000)
        val breaches = db.getBreachesSince(yesterday)

        if (breaches.isEmpty()) {
            return null
        }

        val analysis = callGemini(breaches)
        val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        
        val report = AuditReport(
            date = dateString,
            analysisText = analysis,
            totalBreaches = breaches.size,
            peakTime = calculatePeakTime(breaches)
        )

        db.insertReport(report)
        sendEmail(report)
        
        return report
    }

    private suspend fun callGemini(breaches: List<BreachLog>): String {
        val generativeModel = GenerativeModel(
            modelName = MODEL_NAME,
            apiKey = GEMINI_API_KEY
        )

        val dataSummary = breaches.joinToString("\n") { 
            "Time: ${formatTime(it.timestamp)}, App: ${it.targetApp}, Frustration: ${it.frustrationCount}"
        }

        val prompt = """
            You are a strict, analytical behavioral psychologist. Analyze this user's app-blocker breach data. 
            The user follows highly structured routines. Provide a direct, 3-point psychological breakdown 
            of WHY they failed today, identifying patterns in cognitive depletion, and suggest one actionable change.
            
            Data:
            $dataSummary
        """.trimIndent()

        return try {
            val response = generativeModel.generateContent(prompt)
            response.text ?: "Unable to analyze data."
        } catch (e: Exception) {
            "Analysis failed: ${e.message}"
        }
    }

    private fun sendEmail(report: AuditReport) {
        val props = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.socketFactory.port", "465")
            put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            put("mail.smtp.auth", "true")
            put("mail.smtp.port", "465")
        }

        val session = Session.getDefaultInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(EMAIL_USER, EMAIL_PASS)
            }
        })

        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(EMAIL_USER))
                addRecipient(Message.RecipientType.TO, InternetAddress("riddhimjainsandeep@gmail.com"))
                subject = "Daily Discipline Audit - ${report.date}"
                setText("""
                    Total Breaches: ${report.totalBreaches}
                    Peak Distraction Time: ${report.peakTime}

                    Psychological Analysis (via Gemini Thinking):
                    ${report.analysisText}
                """.trimIndent())
            }
            Transport.send(message)
            Log.d("Analyst", "Email Sent Successfully")
        } catch (e: Exception) {
            Log.e("Analyst", "Email Failed: ${e.message}")
        }
    }

    private fun calculatePeakTime(breaches: List<BreachLog>): String {
        val hourCounts = breaches.groupBy { 
            Calendar.getInstance().apply { timeInMillis = it.timestamp }.get(Calendar.HOUR_OF_DAY)
        }
        val peakHour = hourCounts.maxByOrNull { it.value.size }?.key ?: 0
        return "$peakHour:00"
    }

    private fun formatTime(time: Long): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(time))
    }
}
