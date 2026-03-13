package com.riddh.strictblocker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocked_apps")
data class BlockedApp(
    @PrimaryKey val packageName: String,
    val appName: String,
    val lockDeadline: Long = 0L // 0 means session-only, >0 is 24/7 until timestamp
)

@Entity(tableName = "blocked_urls")
data class BlockedUrl(
    @PrimaryKey val url: String,
    val lockDeadline: Long = 0L
)

@Entity(tableName = "blocked_keywords")
data class BlockedKeyword(
    @PrimaryKey val keyword: String,
    val lockDeadline: Long = 0L
)
