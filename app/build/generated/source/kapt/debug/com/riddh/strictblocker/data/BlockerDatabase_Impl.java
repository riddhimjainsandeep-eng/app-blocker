package com.riddh.strictblocker.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BlockerDatabase_Impl extends BlockerDatabase {
  private volatile BlockerDao _blockerDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `blocked_apps` (`packageName` TEXT NOT NULL, `appName` TEXT NOT NULL, `lockDeadline` INTEGER NOT NULL, PRIMARY KEY(`packageName`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `blocked_urls` (`url` TEXT NOT NULL, `lockDeadline` INTEGER NOT NULL, PRIMARY KEY(`url`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `blocked_keywords` (`keyword` TEXT NOT NULL, `lockDeadline` INTEGER NOT NULL, PRIMARY KEY(`keyword`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `breach_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `targetApp` TEXT NOT NULL, `breachType` TEXT NOT NULL, `frustrationCount` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `audit_reports` (`date` TEXT NOT NULL, `analysisText` TEXT NOT NULL, `totalBreaches` INTEGER NOT NULL, `peakTime` TEXT NOT NULL, PRIMARY KEY(`date`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a734797631bafe1496ab0e46e035bc8c')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `blocked_apps`");
        db.execSQL("DROP TABLE IF EXISTS `blocked_urls`");
        db.execSQL("DROP TABLE IF EXISTS `blocked_keywords`");
        db.execSQL("DROP TABLE IF EXISTS `breach_logs`");
        db.execSQL("DROP TABLE IF EXISTS `audit_reports`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsBlockedApps = new HashMap<String, TableInfo.Column>(3);
        _columnsBlockedApps.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlockedApps.put("appName", new TableInfo.Column("appName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlockedApps.put("lockDeadline", new TableInfo.Column("lockDeadline", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBlockedApps = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBlockedApps = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBlockedApps = new TableInfo("blocked_apps", _columnsBlockedApps, _foreignKeysBlockedApps, _indicesBlockedApps);
        final TableInfo _existingBlockedApps = TableInfo.read(db, "blocked_apps");
        if (!_infoBlockedApps.equals(_existingBlockedApps)) {
          return new RoomOpenHelper.ValidationResult(false, "blocked_apps(com.riddh.strictblocker.data.BlockedApp).\n"
                  + " Expected:\n" + _infoBlockedApps + "\n"
                  + " Found:\n" + _existingBlockedApps);
        }
        final HashMap<String, TableInfo.Column> _columnsBlockedUrls = new HashMap<String, TableInfo.Column>(2);
        _columnsBlockedUrls.put("url", new TableInfo.Column("url", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlockedUrls.put("lockDeadline", new TableInfo.Column("lockDeadline", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBlockedUrls = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBlockedUrls = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBlockedUrls = new TableInfo("blocked_urls", _columnsBlockedUrls, _foreignKeysBlockedUrls, _indicesBlockedUrls);
        final TableInfo _existingBlockedUrls = TableInfo.read(db, "blocked_urls");
        if (!_infoBlockedUrls.equals(_existingBlockedUrls)) {
          return new RoomOpenHelper.ValidationResult(false, "blocked_urls(com.riddh.strictblocker.data.BlockedUrl).\n"
                  + " Expected:\n" + _infoBlockedUrls + "\n"
                  + " Found:\n" + _existingBlockedUrls);
        }
        final HashMap<String, TableInfo.Column> _columnsBlockedKeywords = new HashMap<String, TableInfo.Column>(2);
        _columnsBlockedKeywords.put("keyword", new TableInfo.Column("keyword", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlockedKeywords.put("lockDeadline", new TableInfo.Column("lockDeadline", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBlockedKeywords = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBlockedKeywords = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBlockedKeywords = new TableInfo("blocked_keywords", _columnsBlockedKeywords, _foreignKeysBlockedKeywords, _indicesBlockedKeywords);
        final TableInfo _existingBlockedKeywords = TableInfo.read(db, "blocked_keywords");
        if (!_infoBlockedKeywords.equals(_existingBlockedKeywords)) {
          return new RoomOpenHelper.ValidationResult(false, "blocked_keywords(com.riddh.strictblocker.data.BlockedKeyword).\n"
                  + " Expected:\n" + _infoBlockedKeywords + "\n"
                  + " Found:\n" + _existingBlockedKeywords);
        }
        final HashMap<String, TableInfo.Column> _columnsBreachLogs = new HashMap<String, TableInfo.Column>(5);
        _columnsBreachLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBreachLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBreachLogs.put("targetApp", new TableInfo.Column("targetApp", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBreachLogs.put("breachType", new TableInfo.Column("breachType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBreachLogs.put("frustrationCount", new TableInfo.Column("frustrationCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBreachLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBreachLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBreachLogs = new TableInfo("breach_logs", _columnsBreachLogs, _foreignKeysBreachLogs, _indicesBreachLogs);
        final TableInfo _existingBreachLogs = TableInfo.read(db, "breach_logs");
        if (!_infoBreachLogs.equals(_existingBreachLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "breach_logs(com.riddh.strictblocker.data.BreachLog).\n"
                  + " Expected:\n" + _infoBreachLogs + "\n"
                  + " Found:\n" + _existingBreachLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsAuditReports = new HashMap<String, TableInfo.Column>(4);
        _columnsAuditReports.put("date", new TableInfo.Column("date", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditReports.put("analysisText", new TableInfo.Column("analysisText", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditReports.put("totalBreaches", new TableInfo.Column("totalBreaches", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditReports.put("peakTime", new TableInfo.Column("peakTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAuditReports = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAuditReports = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAuditReports = new TableInfo("audit_reports", _columnsAuditReports, _foreignKeysAuditReports, _indicesAuditReports);
        final TableInfo _existingAuditReports = TableInfo.read(db, "audit_reports");
        if (!_infoAuditReports.equals(_existingAuditReports)) {
          return new RoomOpenHelper.ValidationResult(false, "audit_reports(com.riddh.strictblocker.data.AuditReport).\n"
                  + " Expected:\n" + _infoAuditReports + "\n"
                  + " Found:\n" + _existingAuditReports);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "a734797631bafe1496ab0e46e035bc8c", "ae34608e82a05e1f6e2de88be6b2c6a5");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "blocked_apps","blocked_urls","blocked_keywords","breach_logs","audit_reports");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `blocked_apps`");
      _db.execSQL("DELETE FROM `blocked_urls`");
      _db.execSQL("DELETE FROM `blocked_keywords`");
      _db.execSQL("DELETE FROM `breach_logs`");
      _db.execSQL("DELETE FROM `audit_reports`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(BlockerDao.class, BlockerDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public BlockerDao blockerDao() {
    if (_blockerDao != null) {
      return _blockerDao;
    } else {
      synchronized(this) {
        if(_blockerDao == null) {
          _blockerDao = new BlockerDao_Impl(this);
        }
        return _blockerDao;
      }
    }
  }
}
