package com.riddh.strictblocker.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00052\u00020\u0001:\u0001\u0005B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0006"}, d2 = {"Lcom/riddh/strictblocker/data/BlockerDatabase;", "Landroidx/room/RoomDatabase;", "()V", "blockerDao", "Lcom/riddh/strictblocker/data/BlockerDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.riddh.strictblocker.data.BlockedApp.class, com.riddh.strictblocker.data.BlockedUrl.class, com.riddh.strictblocker.data.BlockedKeyword.class, com.riddh.strictblocker.data.BreachLog.class, com.riddh.strictblocker.data.AuditReport.class}, version = 4, exportSchema = false)
public abstract class BlockerDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.riddh.strictblocker.data.BlockerDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_3_4 = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.riddh.strictblocker.data.BlockerDatabase.Companion Companion = null;
    
    public BlockerDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.riddh.strictblocker.data.BlockerDao blockerDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/riddh/strictblocker/data/BlockerDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/riddh/strictblocker/data/BlockerDatabase;", "MIGRATION_3_4", "Landroidx/room/migration/Migration;", "getDatabase", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.riddh.strictblocker.data.BlockerDatabase getDatabase(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}