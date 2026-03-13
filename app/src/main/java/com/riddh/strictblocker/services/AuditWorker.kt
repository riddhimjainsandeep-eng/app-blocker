package com.riddh.strictblocker.services

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

class AuditWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val analyst = BehavioralAnalyst(applicationContext)
            analyst.generateDailyAudit()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            // Run once a day
            val workRequest = PeriodicWorkRequestBuilder<AuditWorker>(24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .setInitialDelay(calculateDelayToMidnight(), TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "daily_audit",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }

        private fun calculateDelayToMidnight(): Long {
            val now = Calendar.getInstance()
            val dueDate = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (before(now)) {
                    add(Calendar.HOUR_OF_DAY, 24)
                }
            }
            return dueDate.timeInMillis - now.timeInMillis
        }
    }
}
