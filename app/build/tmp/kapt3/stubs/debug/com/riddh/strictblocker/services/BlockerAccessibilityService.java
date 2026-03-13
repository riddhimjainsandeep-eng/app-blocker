package com.riddh.strictblocker.services;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\r\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0012\u0010\u0019\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\b\u0010\u001c\u001a\u00020\u001dH\u0002J\u0018\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u00102\u0006\u0010 \u001a\u00020\u0010H\u0002J\u0012\u0010!\u001a\u00020\u001d2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\b\u0010$\u001a\u00020\u001dH\u0016J\b\u0010%\u001a\u00020\u001dH\u0016J\b\u0010&\u001a\u00020\u001dH\u0014J\u001c\u0010\'\u001a\u00020\u001d2\b\u0010(\u001a\u0004\u0018\u00010\u00142\b\u0010)\u001a\u0004\u0018\u00010\u0010H\u0016J(\u0010*\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u00102\u0006\u0010+\u001a\u00020\u00122\u0006\u0010 \u001a\u00020\u00102\u0006\u0010,\u001a\u00020\fH\u0002J\u0010\u0010-\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\u0018\u0010.\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010/\u001a\u00020\u0010H\u0002R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lcom/riddh/strictblocker/services/BlockerAccessibilityService;", "Landroid/accessibilityservice/AccessibilityService;", "Landroid/content/SharedPreferences$OnSharedPreferenceChangeListener;", "()V", "blockedApps", "", "Lcom/riddh/strictblocker/data/BlockedApp;", "blockedKeywords", "Lcom/riddh/strictblocker/data/BlockedKeyword;", "blockedUrls", "Lcom/riddh/strictblocker/data/BlockedUrl;", "currentFrustrationCount", "", "isActive", "", "lastBreachTarget", "", "lastBreachTime", "", "prefs", "Landroid/content/SharedPreferences;", "serviceJob", "Lkotlinx/coroutines/CompletableJob;", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "findUrlInNodes", "node", "Landroid/view/accessibility/AccessibilityNodeInfo;", "launchBlockScreen", "", "logAndBlock", "target", "type", "onAccessibilityEvent", "event", "Landroid/view/accessibility/AccessibilityEvent;", "onDestroy", "onInterrupt", "onServiceConnected", "onSharedPreferenceChanged", "sharedPreferences", "key", "saveBreachToDb", "time", "count", "scanForTampering", "scanNodesForKeywords", "currentPackage", "app_debug"})
public final class BlockerAccessibilityService extends android.accessibilityservice.AccessibilityService implements android.content.SharedPreferences.OnSharedPreferenceChangeListener {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CompletableJob serviceJob = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.riddh.strictblocker.data.BlockedApp> blockedApps;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.riddh.strictblocker.data.BlockedUrl> blockedUrls;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.riddh.strictblocker.data.BlockedKeyword> blockedKeywords;
    private long lastBreachTime = 0L;
    private int currentFrustrationCount = 0;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String lastBreachTarget = "";
    private boolean isActive = false;
    private android.content.SharedPreferences prefs;
    
    public BlockerAccessibilityService() {
        super();
    }
    
    @java.lang.Override()
    protected void onServiceConnected() {
    }
    
    @java.lang.Override()
    public void onSharedPreferenceChanged(@org.jetbrains.annotations.Nullable()
    android.content.SharedPreferences sharedPreferences, @org.jetbrains.annotations.Nullable()
    java.lang.String key) {
    }
    
    @java.lang.Override()
    public void onAccessibilityEvent(@org.jetbrains.annotations.Nullable()
    android.view.accessibility.AccessibilityEvent event) {
    }
    
    private final void logAndBlock(java.lang.String target, java.lang.String type) {
    }
    
    private final void saveBreachToDb(java.lang.String target, long time, java.lang.String type, int count) {
    }
    
    private final java.lang.String findUrlInNodes(android.view.accessibility.AccessibilityNodeInfo node) {
        return null;
    }
    
    private final boolean scanForTampering(android.view.accessibility.AccessibilityNodeInfo node) {
        return false;
    }
    
    private final boolean scanNodesForKeywords(android.view.accessibility.AccessibilityNodeInfo node, java.lang.String currentPackage) {
        return false;
    }
    
    private final void launchBlockScreen() {
    }
    
    @java.lang.Override()
    public void onInterrupt() {
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
}