package com.riddh.strictblocker.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BlockerDao_Impl implements BlockerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BlockedApp> __insertionAdapterOfBlockedApp;

  private final EntityInsertionAdapter<BlockedUrl> __insertionAdapterOfBlockedUrl;

  private final EntityInsertionAdapter<BlockedKeyword> __insertionAdapterOfBlockedKeyword;

  private final EntityInsertionAdapter<BreachLog> __insertionAdapterOfBreachLog;

  private final EntityInsertionAdapter<AuditReport> __insertionAdapterOfAuditReport;

  private final EntityDeletionOrUpdateAdapter<BlockedApp> __deletionAdapterOfBlockedApp;

  private final EntityDeletionOrUpdateAdapter<BlockedUrl> __deletionAdapterOfBlockedUrl;

  private final EntityDeletionOrUpdateAdapter<BlockedKeyword> __deletionAdapterOfBlockedKeyword;

  private final SharedSQLiteStatement __preparedStmtOfClearOldBreaches;

  public BlockerDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBlockedApp = new EntityInsertionAdapter<BlockedApp>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `blocked_apps` (`packageName`,`appName`,`lockDeadline`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BlockedApp entity) {
        if (entity.getPackageName() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getPackageName());
        }
        if (entity.getAppName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getAppName());
        }
        statement.bindLong(3, entity.getLockDeadline());
      }
    };
    this.__insertionAdapterOfBlockedUrl = new EntityInsertionAdapter<BlockedUrl>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `blocked_urls` (`url`,`lockDeadline`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BlockedUrl entity) {
        if (entity.getUrl() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getUrl());
        }
        statement.bindLong(2, entity.getLockDeadline());
      }
    };
    this.__insertionAdapterOfBlockedKeyword = new EntityInsertionAdapter<BlockedKeyword>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `blocked_keywords` (`keyword`,`lockDeadline`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BlockedKeyword entity) {
        if (entity.getKeyword() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getKeyword());
        }
        statement.bindLong(2, entity.getLockDeadline());
      }
    };
    this.__insertionAdapterOfBreachLog = new EntityInsertionAdapter<BreachLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `breach_logs` (`id`,`timestamp`,`targetApp`,`breachType`,`frustrationCount`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BreachLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        if (entity.getTargetApp() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTargetApp());
        }
        if (entity.getBreachType() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBreachType());
        }
        statement.bindLong(5, entity.getFrustrationCount());
      }
    };
    this.__insertionAdapterOfAuditReport = new EntityInsertionAdapter<AuditReport>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `audit_reports` (`date`,`analysisText`,`totalBreaches`,`peakTime`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AuditReport entity) {
        if (entity.getDate() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getDate());
        }
        if (entity.getAnalysisText() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getAnalysisText());
        }
        statement.bindLong(3, entity.getTotalBreaches());
        if (entity.getPeakTime() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPeakTime());
        }
      }
    };
    this.__deletionAdapterOfBlockedApp = new EntityDeletionOrUpdateAdapter<BlockedApp>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `blocked_apps` WHERE `packageName` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BlockedApp entity) {
        if (entity.getPackageName() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getPackageName());
        }
      }
    };
    this.__deletionAdapterOfBlockedUrl = new EntityDeletionOrUpdateAdapter<BlockedUrl>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `blocked_urls` WHERE `url` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BlockedUrl entity) {
        if (entity.getUrl() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getUrl());
        }
      }
    };
    this.__deletionAdapterOfBlockedKeyword = new EntityDeletionOrUpdateAdapter<BlockedKeyword>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `blocked_keywords` WHERE `keyword` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BlockedKeyword entity) {
        if (entity.getKeyword() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getKeyword());
        }
      }
    };
    this.__preparedStmtOfClearOldBreaches = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM breach_logs WHERE timestamp < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertApp(final BlockedApp app, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBlockedApp.insert(app);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertUrl(final BlockedUrl url, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBlockedUrl.insert(url);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertKeyword(final BlockedKeyword keyword,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBlockedKeyword.insert(keyword);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBreach(final BreachLog log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBreachLog.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertReport(final AuditReport report,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAuditReport.insert(report);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteApp(final BlockedApp app, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBlockedApp.handle(app);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteUrl(final BlockedUrl url, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBlockedUrl.handle(url);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteKeyword(final BlockedKeyword keyword,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBlockedKeyword.handle(keyword);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearOldBreaches(final long before, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearOldBreaches.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, before);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearOldBreaches.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BlockedApp>> getAllApps() {
    final String _sql = "SELECT * FROM blocked_apps";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"blocked_apps"}, new Callable<List<BlockedApp>>() {
      @Override
      @NonNull
      public List<BlockedApp> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "appName");
          final int _cursorIndexOfLockDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "lockDeadline");
          final List<BlockedApp> _result = new ArrayList<BlockedApp>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BlockedApp _item;
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final String _tmpAppName;
            if (_cursor.isNull(_cursorIndexOfAppName)) {
              _tmpAppName = null;
            } else {
              _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            }
            final long _tmpLockDeadline;
            _tmpLockDeadline = _cursor.getLong(_cursorIndexOfLockDeadline);
            _item = new BlockedApp(_tmpPackageName,_tmpAppName,_tmpLockDeadline);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<BlockedUrl>> getAllUrls() {
    final String _sql = "SELECT * FROM blocked_urls";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"blocked_urls"}, new Callable<List<BlockedUrl>>() {
      @Override
      @NonNull
      public List<BlockedUrl> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfLockDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "lockDeadline");
          final List<BlockedUrl> _result = new ArrayList<BlockedUrl>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BlockedUrl _item;
            final String _tmpUrl;
            if (_cursor.isNull(_cursorIndexOfUrl)) {
              _tmpUrl = null;
            } else {
              _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            }
            final long _tmpLockDeadline;
            _tmpLockDeadline = _cursor.getLong(_cursorIndexOfLockDeadline);
            _item = new BlockedUrl(_tmpUrl,_tmpLockDeadline);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<BlockedKeyword>> getAllKeywords() {
    final String _sql = "SELECT * FROM blocked_keywords";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"blocked_keywords"}, new Callable<List<BlockedKeyword>>() {
      @Override
      @NonNull
      public List<BlockedKeyword> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKeyword = CursorUtil.getColumnIndexOrThrow(_cursor, "keyword");
          final int _cursorIndexOfLockDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "lockDeadline");
          final List<BlockedKeyword> _result = new ArrayList<BlockedKeyword>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BlockedKeyword _item;
            final String _tmpKeyword;
            if (_cursor.isNull(_cursorIndexOfKeyword)) {
              _tmpKeyword = null;
            } else {
              _tmpKeyword = _cursor.getString(_cursorIndexOfKeyword);
            }
            final long _tmpLockDeadline;
            _tmpLockDeadline = _cursor.getLong(_cursorIndexOfLockDeadline);
            _item = new BlockedKeyword(_tmpKeyword,_tmpLockDeadline);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<BreachLog>> getAllBreaches() {
    final String _sql = "SELECT * FROM breach_logs ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"breach_logs"}, new Callable<List<BreachLog>>() {
      @Override
      @NonNull
      public List<BreachLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfTargetApp = CursorUtil.getColumnIndexOrThrow(_cursor, "targetApp");
          final int _cursorIndexOfBreachType = CursorUtil.getColumnIndexOrThrow(_cursor, "breachType");
          final int _cursorIndexOfFrustrationCount = CursorUtil.getColumnIndexOrThrow(_cursor, "frustrationCount");
          final List<BreachLog> _result = new ArrayList<BreachLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BreachLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpTargetApp;
            if (_cursor.isNull(_cursorIndexOfTargetApp)) {
              _tmpTargetApp = null;
            } else {
              _tmpTargetApp = _cursor.getString(_cursorIndexOfTargetApp);
            }
            final String _tmpBreachType;
            if (_cursor.isNull(_cursorIndexOfBreachType)) {
              _tmpBreachType = null;
            } else {
              _tmpBreachType = _cursor.getString(_cursorIndexOfBreachType);
            }
            final int _tmpFrustrationCount;
            _tmpFrustrationCount = _cursor.getInt(_cursorIndexOfFrustrationCount);
            _item = new BreachLog(_tmpId,_tmpTimestamp,_tmpTargetApp,_tmpBreachType,_tmpFrustrationCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getBreachesSince(final long since,
      final Continuation<? super List<BreachLog>> $completion) {
    final String _sql = "SELECT * FROM breach_logs WHERE timestamp > ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BreachLog>>() {
      @Override
      @NonNull
      public List<BreachLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfTargetApp = CursorUtil.getColumnIndexOrThrow(_cursor, "targetApp");
          final int _cursorIndexOfBreachType = CursorUtil.getColumnIndexOrThrow(_cursor, "breachType");
          final int _cursorIndexOfFrustrationCount = CursorUtil.getColumnIndexOrThrow(_cursor, "frustrationCount");
          final List<BreachLog> _result = new ArrayList<BreachLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BreachLog _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpTargetApp;
            if (_cursor.isNull(_cursorIndexOfTargetApp)) {
              _tmpTargetApp = null;
            } else {
              _tmpTargetApp = _cursor.getString(_cursorIndexOfTargetApp);
            }
            final String _tmpBreachType;
            if (_cursor.isNull(_cursorIndexOfBreachType)) {
              _tmpBreachType = null;
            } else {
              _tmpBreachType = _cursor.getString(_cursorIndexOfBreachType);
            }
            final int _tmpFrustrationCount;
            _tmpFrustrationCount = _cursor.getInt(_cursorIndexOfFrustrationCount);
            _item = new BreachLog(_tmpId,_tmpTimestamp,_tmpTargetApp,_tmpBreachType,_tmpFrustrationCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getReportForDate(final String date,
      final Continuation<? super AuditReport> $completion) {
    final String _sql = "SELECT * FROM audit_reports WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AuditReport>() {
      @Override
      @Nullable
      public AuditReport call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfAnalysisText = CursorUtil.getColumnIndexOrThrow(_cursor, "analysisText");
          final int _cursorIndexOfTotalBreaches = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBreaches");
          final int _cursorIndexOfPeakTime = CursorUtil.getColumnIndexOrThrow(_cursor, "peakTime");
          final AuditReport _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            final String _tmpAnalysisText;
            if (_cursor.isNull(_cursorIndexOfAnalysisText)) {
              _tmpAnalysisText = null;
            } else {
              _tmpAnalysisText = _cursor.getString(_cursorIndexOfAnalysisText);
            }
            final int _tmpTotalBreaches;
            _tmpTotalBreaches = _cursor.getInt(_cursorIndexOfTotalBreaches);
            final String _tmpPeakTime;
            if (_cursor.isNull(_cursorIndexOfPeakTime)) {
              _tmpPeakTime = null;
            } else {
              _tmpPeakTime = _cursor.getString(_cursorIndexOfPeakTime);
            }
            _result = new AuditReport(_tmpDate,_tmpAnalysisText,_tmpTotalBreaches,_tmpPeakTime);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AuditReport>> getAllReports() {
    final String _sql = "SELECT * FROM audit_reports ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audit_reports"}, new Callable<List<AuditReport>>() {
      @Override
      @NonNull
      public List<AuditReport> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfAnalysisText = CursorUtil.getColumnIndexOrThrow(_cursor, "analysisText");
          final int _cursorIndexOfTotalBreaches = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBreaches");
          final int _cursorIndexOfPeakTime = CursorUtil.getColumnIndexOrThrow(_cursor, "peakTime");
          final List<AuditReport> _result = new ArrayList<AuditReport>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AuditReport _item;
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            final String _tmpAnalysisText;
            if (_cursor.isNull(_cursorIndexOfAnalysisText)) {
              _tmpAnalysisText = null;
            } else {
              _tmpAnalysisText = _cursor.getString(_cursorIndexOfAnalysisText);
            }
            final int _tmpTotalBreaches;
            _tmpTotalBreaches = _cursor.getInt(_cursorIndexOfTotalBreaches);
            final String _tmpPeakTime;
            if (_cursor.isNull(_cursorIndexOfPeakTime)) {
              _tmpPeakTime = null;
            } else {
              _tmpPeakTime = _cursor.getString(_cursorIndexOfPeakTime);
            }
            _item = new AuditReport(_tmpDate,_tmpAnalysisText,_tmpTotalBreaches,_tmpPeakTime);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
