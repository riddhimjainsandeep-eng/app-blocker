package com.riddh.strictblocker.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u000b\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u0014\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00150\u0014H\'J\u0014\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00150\u0014H\'J\u0014\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00150\u0014H\'J\u0014\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u00150\u0014H\'J\u0014\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00150\u0014H\'J\u001c\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00170\u00152\u0006\u0010\u001d\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u001e\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u001f\u001a\u00020 H\u00a7@\u00a2\u0006\u0002\u0010!J\u0016\u0010\"\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010#\u001a\u00020\u00032\u0006\u0010$\u001a\u00020\u0017H\u00a7@\u00a2\u0006\u0002\u0010%J\u0016\u0010&\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\'\u001a\u00020\u00032\u0006\u0010(\u001a\u00020\u001aH\u00a7@\u00a2\u0006\u0002\u0010)J\u0016\u0010*\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010\u0012\u00a8\u0006+"}, d2 = {"Lcom/riddh/strictblocker/data/BlockerDao;", "", "clearOldBreaches", "", "before", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteApp", "app", "Lcom/riddh/strictblocker/data/BlockedApp;", "(Lcom/riddh/strictblocker/data/BlockedApp;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteKeyword", "keyword", "Lcom/riddh/strictblocker/data/BlockedKeyword;", "(Lcom/riddh/strictblocker/data/BlockedKeyword;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteUrl", "url", "Lcom/riddh/strictblocker/data/BlockedUrl;", "(Lcom/riddh/strictblocker/data/BlockedUrl;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllApps", "Lkotlinx/coroutines/flow/Flow;", "", "getAllBreaches", "Lcom/riddh/strictblocker/data/BreachLog;", "getAllKeywords", "getAllReports", "Lcom/riddh/strictblocker/data/AuditReport;", "getAllUrls", "getBreachesSince", "since", "getReportForDate", "date", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertApp", "insertBreach", "log", "(Lcom/riddh/strictblocker/data/BreachLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertKeyword", "insertReport", "report", "(Lcom/riddh/strictblocker/data/AuditReport;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertUrl", "app_debug"})
@androidx.room.Dao()
public abstract interface BlockerDao {
    
    @androidx.room.Query(value = "SELECT * FROM blocked_apps")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.riddh.strictblocker.data.BlockedApp>> getAllApps();
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertApp(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BlockedApp app, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteApp(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BlockedApp app, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM blocked_urls")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.riddh.strictblocker.data.BlockedUrl>> getAllUrls();
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertUrl(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BlockedUrl url, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteUrl(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BlockedUrl url, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM blocked_keywords")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.riddh.strictblocker.data.BlockedKeyword>> getAllKeywords();
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertKeyword(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BlockedKeyword keyword, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteKeyword(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BlockedKeyword keyword, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertBreach(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BreachLog log, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM breach_logs ORDER BY timestamp DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.riddh.strictblocker.data.BreachLog>> getAllBreaches();
    
    @androidx.room.Query(value = "SELECT * FROM breach_logs WHERE timestamp > :since ORDER BY timestamp ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getBreachesSince(long since, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.riddh.strictblocker.data.BreachLog>> $completion);
    
    @androidx.room.Query(value = "DELETE FROM breach_logs WHERE timestamp < :before")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearOldBreaches(long before, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertReport(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.AuditReport report, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM audit_reports WHERE date = :date")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReportForDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.riddh.strictblocker.data.AuditReport> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM audit_reports ORDER BY date DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.riddh.strictblocker.data.AuditReport>> getAllReports();
}