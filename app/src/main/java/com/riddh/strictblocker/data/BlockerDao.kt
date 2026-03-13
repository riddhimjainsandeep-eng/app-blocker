package com.riddh.strictblocker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockerDao {
    @Query("SELECT * FROM blocked_apps")
    fun getAllApps(): Flow<List<BlockedApp>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(app: BlockedApp)

    @Delete
    suspend fun deleteApp(app: BlockedApp)

    @Query("SELECT * FROM blocked_urls")
    fun getAllUrls(): Flow<List<BlockedUrl>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUrl(url: BlockedUrl)

    @Delete
    suspend fun deleteUrl(url: BlockedUrl)

    @Query("SELECT * FROM blocked_keywords")
    fun getAllKeywords(): Flow<List<BlockedKeyword>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyword(keyword: BlockedKeyword)

    @Delete
    suspend fun deleteKeyword(keyword: BlockedKeyword)

    // BREACH LOGS
    @Insert
    suspend fun insertBreach(log: BreachLog)

    @Query("SELECT * FROM breach_logs ORDER BY timestamp DESC")
    fun getAllBreaches(): Flow<List<BreachLog>>

    @Query("SELECT * FROM breach_logs WHERE timestamp > :since ORDER BY timestamp ASC")
    suspend fun getBreachesSince(since: Long): List<BreachLog>

    @Query("DELETE FROM breach_logs WHERE timestamp < :before")
    suspend fun clearOldBreaches(before: Long)

    // AUDIT REPORTS
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: AuditReport)

    @Query("SELECT * FROM audit_reports WHERE date = :date")
    suspend fun getReportForDate(date: String): AuditReport?

    @Query("SELECT * FROM audit_reports ORDER BY date DESC")
    fun getAllReports(): Flow<List<AuditReport>>
}
