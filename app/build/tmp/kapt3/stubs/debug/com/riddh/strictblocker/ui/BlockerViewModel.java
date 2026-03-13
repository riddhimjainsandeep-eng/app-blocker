package com.riddh.strictblocker.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J \u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\b\b\u0002\u0010\u0016\u001a\u00020\u0017J\u0018\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u00142\b\b\u0002\u0010\u0016\u001a\u00020\u0017J\u0018\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\u00142\b\b\u0002\u0010\u0016\u001a\u00020\u0017J\u0016\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0014J\u000e\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u0014J\u000e\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\u0014R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/riddh/strictblocker/ui/BlockerViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/riddh/strictblocker/data/BlockerRepository;", "(Lcom/riddh/strictblocker/data/BlockerRepository;)V", "blockedApps", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/riddh/strictblocker/data/BlockedApp;", "getBlockedApps", "()Lkotlinx/coroutines/flow/StateFlow;", "blockedKeywords", "Lcom/riddh/strictblocker/data/BlockedKeyword;", "getBlockedKeywords", "blockedUrls", "Lcom/riddh/strictblocker/data/BlockedUrl;", "getBlockedUrls", "addApp", "Lkotlinx/coroutines/Job;", "packageName", "", "appName", "deadline", "", "addKeyword", "keyword", "addUrl", "url", "removeApp", "removeKeyword", "removeUrl", "app_debug"})
public final class BlockerViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.riddh.strictblocker.data.BlockerRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.riddh.strictblocker.data.BlockedApp>> blockedApps = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.riddh.strictblocker.data.BlockedUrl>> blockedUrls = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.riddh.strictblocker.data.BlockedKeyword>> blockedKeywords = null;
    
    public BlockerViewModel(@org.jetbrains.annotations.NotNull()
    com.riddh.strictblocker.data.BlockerRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.riddh.strictblocker.data.BlockedApp>> getBlockedApps() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.riddh.strictblocker.data.BlockedUrl>> getBlockedUrls() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.riddh.strictblocker.data.BlockedKeyword>> getBlockedKeywords() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job addApp(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    java.lang.String appName, long deadline) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job removeApp(@org.jetbrains.annotations.NotNull()
    java.lang.String packageName, @org.jetbrains.annotations.NotNull()
    java.lang.String appName) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job addUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String url, long deadline) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job removeUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String url) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job addKeyword(@org.jetbrains.annotations.NotNull()
    java.lang.String keyword, long deadline) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job removeKeyword(@org.jetbrains.annotations.NotNull()
    java.lang.String keyword) {
        return null;
    }
}