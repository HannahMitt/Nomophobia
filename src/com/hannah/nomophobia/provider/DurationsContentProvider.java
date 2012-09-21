package com.hannah.nomophobia.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class DurationsContentProvider extends ContentProvider {

	public static final String AUTHORITY = "com.hannah.nomophobia.provider.durations";

	private DatabaseHelper mDatabaseHelper;

	private static final String SCHEME = "content://";
	private static final String PATH_RESULTS = "/durations";

	private static final String TYPE_DURATIONS_STRING = "vnd.android.cursor.dir/" + AUTHORITY + ".duration";

	private static final String DEFAULT_SORT_ORDER = Contract.Columns.TIME + " ASC";

	public static final class Contract {
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_RESULTS);

		public static final class Columns implements BaseColumns {
			/**
			 * duration of ignore time - type: LONG
			 */
			public static final String DURATION = "DURATION";

			/**
			 * time ignore was recorded - type: LONG
			 */
			public static final String TIME = "TIME";

			public static final int INDEX_DURATION = 1;
			public static final int INDEX_TIME = 2;
		}
	}

	private static final class DatabaseHelper extends SQLiteOpenHelper {
		private static final String DB_NAME = "durations.db";
		private static final String TABLE_NAME = "durations";
		private static final int DB_VERSION = 3;

		private static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + Contract.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Contract.Columns.DURATION
				+ " LONG, " + Contract.Columns.TIME + " LONG UNIQUE );";

		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_STATEMENT);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}

	}

	@Override
	public boolean onCreate() {
		mDatabaseHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		return TYPE_DURATIONS_STRING;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		long id = db.insertWithOnConflict(DatabaseHelper.TABLE_NAME, Contract.Columns.TIME, values, SQLiteDatabase.CONFLICT_REPLACE);

		if (id < 0) {
			throw new SQLException("Failed to insert row into " + uri);
		} else {
			Uri resultUri = ContentUris.withAppendedId(Contract.CONTENT_URI, id);
			getContext().getContentResolver().notifyChange(resultUri, null);
			return resultUri;
		}
	}

	/**
	 * NOTE this bulk insert also clears the database
	 */
	@Override
	public int bulkInsert(Uri uri, ContentValues[] valuesList) {

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		int inserted = 0;
		long id = -1;

		try {
			db.beginTransaction();
			db.delete(DatabaseHelper.TABLE_NAME, null, null);

			for (ContentValues values : valuesList) {
				id = db.insertWithOnConflict(DatabaseHelper.TABLE_NAME, Contract.Columns.TIME, values, SQLiteDatabase.CONFLICT_REPLACE);
				if (id >= 0)
					inserted++;
			}
			db.setTransactionSuccessful();

		} catch (SQLException e) {
			Log.e("ContentProvider", "bulkInsert(..) e: " + e);

		} finally {
			db.endTransaction();
		}

		return inserted;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		if (sortOrder == null) {
			sortOrder = DEFAULT_SORT_ORDER;
		}
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
		cursor = db.query(DatabaseHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
		if (cursor != null) {
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}

		return cursor;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		int count = db.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return mDatabaseHelper.getReadableDatabase().update(DatabaseHelper.TABLE_NAME, values, selection, selectionArgs);
	}

	public long sumColumn(String column, String whereClause) {
		Cursor cursor = mDatabaseHelper.getReadableDatabase().rawQuery("SELECT SUM(" + column + ") as sum FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + whereClause, null);
		cursor.moveToFirst();
		long sum = cursor.getLong(0);
		cursor.close();

		return sum;
	}
	
	public long minColumn(String column, int columnIndex) {
		Cursor cursor = mDatabaseHelper.getReadableDatabase().rawQuery("SELECT MIN(" + column + ") FROM " + DatabaseHelper.TABLE_NAME, null);
		cursor.moveToFirst();
		long min = cursor.getLong(0);
		cursor.close();

		return min;
	}
}
