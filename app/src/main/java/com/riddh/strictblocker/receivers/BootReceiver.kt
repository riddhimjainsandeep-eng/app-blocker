package com.riddh.strictblocker.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.riddh.strictblocker.services.BlockerForegroundService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val prefs = context.getSharedPreferences("blocker_prefs", Context.MODE_PRIVATE)
            val endTime = prefs.getLong("session_end_time", 0L)
            
            if (System.currentTimeMillis() < endTime) {
                val serviceIntent = Intent(context, BlockerForegroundService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent)
                }
            }
        }
    }
}
