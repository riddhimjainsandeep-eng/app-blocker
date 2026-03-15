package com.riddh.strictblocker.services

import android.content.Context
import android.util.Log
import com.riddh.strictblocker.BuildConfig
import java.util.*
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmergencyService(private val context: Context) {

    private val EMAIL_USER = BuildConfig.EMAIL_USER
    private val EMAIL_PASS = BuildConfig.EMAIL_PASS

    fun generateOtp(): String {
        val otp = (100000..999999).random().toString()
        val prefs = context.getSharedPreferences("blocker_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("emergency_otp", otp)
            .putLong("emergency_otp_time", System.currentTimeMillis())
            .apply()
        return otp
    }

    fun validateOtp(input: String): Boolean {
        val prefs = context.getSharedPreferences("blocker_prefs", Context.MODE_PRIVATE)
        val savedOtp = prefs.getString("emergency_otp", null)
        val savedTime = prefs.getLong("emergency_otp_time", 0L)
        val now = System.currentTimeMillis()

        // Valid for 10 minutes
        if (savedOtp != null && input == savedOtp && (now - savedTime) < 10 * 60 * 1000) {
            prefs.edit().remove("emergency_otp").remove("emergency_otp_time").apply()
            return true
        }
        return false
    }

    suspend fun sendOtpEmail(otp: String): Boolean = withContext(Dispatchers.IO) {
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
                subject = "Emergency Exit OTP - Strict Blocker"
                setText("Your one-time password for Emergency Exit is: $otp\n\nThis OTP is valid for 10 minutes.")
            }
            Transport.send(message)
            true
        } catch (e: Exception) {
            Log.e("EmergencyService", "Failed to send OTP: ${e.message}")
            false
        }
    }
}
