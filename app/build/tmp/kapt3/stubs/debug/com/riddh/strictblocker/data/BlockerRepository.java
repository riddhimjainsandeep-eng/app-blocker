package com.riddh.strictblocker.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\f\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\u001aJ\u0016\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\u001aR\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/riddh/strictblocker/data/BlockerRepository;", "", "blockerDao", "Lcom/riddh/strictblocker/data/BlockerDao;", "(Lcom/riddh/strictblocker/data/BlockerDao;)V", "allApps", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/riddh/strictblocker/data/BlockedApp;", "getAllApps", "()Lkotlinx/coroutines/flow/Flow;", "allKeywords", "Lcom/riddh/strictblocker/data/BlockedKeyword;", "getAllKeywords", "allUrls", "Lcom/riddh/strictblocker/data/BlockedUrl;", "getAllUrls", "deleteApp", "", "app", "(Lcom/riddh/strictblocker/data/BlockedApp;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteKeyword", "keyword", "(Lcom/riddh/strictblocker/data/BlockedKeyword;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteUrl", "url", "(Lcom/riddh/strictblocker/data/BlockedUrl;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertApp", "insertKeyword", "insertUrl", "app_debug"})
public final class BlockerRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.riddh.strictblocker.data.BlockerDao blockerDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.riddh.strictblocker.data.BlockedApp>> allApps = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.riddh.strictblocker.data.BlockedUrl>> allUrls = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.riddh.strictblocker.data.BlockedKeyword>> allKeywords = null;
    
    public BlockerRepository(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BlockerDao blockerDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.riddh.strictblocker.data.BlockedApp>> getAllApps() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.riddh.strictblocker.data.BlockedUrl>> getAllUrls() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.riddh.strictblocker.data.BlockedKeyword>> getAllKeywords() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertApp(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BlockedApp app, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteApp(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BlockedApp app, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertUrl(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BlockedUrl url, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteUrl(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BlockedUrl url, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertKeyword(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BlockedKeyword keyword, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteKeyword(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BlockedKeyword keyword, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}