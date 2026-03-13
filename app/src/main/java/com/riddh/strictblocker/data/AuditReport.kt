package com.riddh.strictblocker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audit_reports")
data class AuditReport(
    @PrimaryKey val date: String, // Format: YYYY-MM-DD
    val analysisText: String,
    val totalBreaches: Int,
    val peakTime: String
)
