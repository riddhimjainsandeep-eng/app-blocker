package com.riddh.strictblocker.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.riddh.strictblocker.services.BlockerForegroundService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == "com.riddh.strictblocker.RESTART_SERVICE") {
            val prefs = context.getSharedPreferences("blocker_prefs", Context.MODE_PRIVATE)
            val endTime = prefs.getLong("session_end_time", 0L)
            
            if (System.currentTimeMillis() < endTime || prefs.getBoolean("is_active", false)) {
                val serviceIntent = Intent(context, BlockerForegroundService::class.java)
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(serviceIntent)
                    } else {
                        context.startService(serviceIntent)
                    }
                } catch (e: Exception) {
                    // Fallback to WorkManager if background starts are restricted
                    val workRequest = androidx.work.OneTimeWorkRequestBuilder<com.riddh.strictblocker.services.SessionEndWorker>()
                        .setInitialDelay(0, java.util.concurrent.TimeUnit.MILLISECONDS)
                        .build()
                    androidx.work.WorkManager.getInstance(context).enqueue(workRequest)
                }
            }
        }
    }
}
