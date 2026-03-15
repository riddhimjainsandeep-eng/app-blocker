package com.riddh.strictblocker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breach_logs")
data class BreachLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val targetApp: String,
    val breachType: String, // "APP_OPEN", "KEYWORD_DETECTED", "SETTINGS_TAMPER", "EMERGENCY_EXIT_ATTEMPT"
    val frustrationCount: Int = 1
)
