package com.riddh.strictblocker.services

import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.riddh.strictblocker.data.BlockerDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class BlockerNotificationService : NotificationListenerService() {
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn?.let {
            val prefs = getSharedPreferences("blocker_prefs", Context.MODE_PRIVATE)
            val isActive = prefs.getBoolean("is_active", false)
            if (!isActive) return

            val pkg = it.packageName
            serviceScope.launch {
                val db = BlockerDatabase.getDatabase(applicationContext).blockerDao()
                val blockedApps = db.getAllApps().first()
                if (blockedApps.any { app -> app.packageName == pkg }) {
                    cancelNotification(it.key)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
