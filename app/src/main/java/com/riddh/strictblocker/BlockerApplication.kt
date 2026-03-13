package com.riddh.strictblocker

import android.app.Application
import android.content.Context
import android.content.Intent
import com.riddh.strictblocker.data.BlockerDatabase
import com.riddh.strictblocker.data.BlockerRepository
import com.riddh.strictblocker.services.AuditWorker
import com.riddh.strictblocker.services.BlockerForegroundService

class BlockerApplication : Application() {
    val database by lazy { BlockerDatabase.getDatabase(this) }
    val repository by lazy { BlockerRepository(database.blockerDao()) }

    override fun onCreate() {
        super.onCreate()
        AuditWorker.schedule(this)
        
        // Ensure Foreground Service is running if a lock is active
        val prefs = getSharedPreferences("blocker_prefs", Context.MODE_PRIVATE)
        val endTime = prefs.getLong("session_end_time", 0L)
        if (System.currentTimeMillis() < endTime) {
            val serviceIntent = Intent(this, BlockerForegroundService::class.java)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
        }
    }
}
