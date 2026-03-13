package com.riddh.strictblocker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [BlockedApp::class, BlockedUrl::class, BlockedKeyword::class, BreachLog::class, AuditReport::class], version = 4, exportSchema = false)
abstract class BlockerDatabase : RoomDatabase() {
    abstract fun blockerDao(): BlockerDao

    companion object {
        @Volatile
        private var INSTANCE: BlockerDatabase? = null

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS audit_reports (date TEXT PRIMARY KEY NOT NULL, analysisText TEXT NOT NULL, totalBreaches INTEGER NOT NULL, peakTime TEXT NOT NULL)")
            }
        }

        fun getDatabase(context: Context): BlockerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BlockerDatabase::class.java,
                    "blocker_database"
                )
                .addMigrations(MIGRATION_3_4)
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
