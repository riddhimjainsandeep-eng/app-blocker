package com.riddh.strictblocker.services

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters

class SessionEndWorker(private val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val prefs = context.getSharedPreferences("blocker_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("is_active", false)
            .putLong("session_end_time", 0L)
            .apply()
        context.stopService(Intent(context, BlockerForegroundService::class.java))
        return Result.success()
    }
}