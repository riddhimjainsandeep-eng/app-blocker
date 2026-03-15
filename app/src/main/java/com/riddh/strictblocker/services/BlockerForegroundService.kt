package com.riddh.strictblocker.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.riddh.strictblocker.MainActivity
import com.riddh.strictblocker.R

class BlockerForegroundService : Service() {

    private val CHANNEL_ID = "blocker_service_channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Strict Blocker Active")
            .setContentText("Your focus is being protected 24/7.")
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(1, notification)

        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        restartService()
    }

    override fun onDestroy() {
        super.onDestroy()
        restartService()
    }

    private fun restartService() {
        val prefs = getSharedPreferences("blocker_prefs", Context.MODE_PRIVATE)
        if (prefs.getBoolean("is_active", false)) {
            val broadcastIntent = Intent(this, com.riddh.strictblocker.receivers.BootReceiver::class.java)
            broadcastIntent.action = "com.riddh.strictblocker.RESTART_SERVICE"
            sendBroadcast(broadcastIntent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Strict Blocker Core Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
