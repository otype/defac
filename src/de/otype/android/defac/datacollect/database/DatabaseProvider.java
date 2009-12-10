package de.otype.android.defac.datacollect.database;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import de.otype.android.defac.datacollect.database.Database.Category;
import de.otype.android.defac.datacollect.database.Database.Methods;


public class DatabaseProvider extends ContentProvider{

	private static final String DATABASE_NAME = "datacollectors.db";
	private static final String TABLE_CATEGORY = "CATEGORY";
	private static final String TABLE_METHODS = "METHODS";
	private static final int CATEGORY = 1;
	private static final int METHODS = 2;
	private static final int DATABASE_VERSION = 1;
	private static final UriMatcher sUriMatcher;
	private static final HashMap<String, String> sProjectionMap;
	private DatabaseHelper mOpenHelper;
	
	/**
	 * 
	 * @author hgschmidt
	 *
	 */
	private class DatabaseHelper extends SQLiteOpenHelper {

		/**
		 * 
		 * @param context
		 */
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/**
		 * 
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " ("
					+ Category._ID + " INTEGER PRIMARY KEY, "
					+ Category.NAME + " TEXT, "
					+ Category.CHECKED + " BOOLEAN default 0"
					+ ");");
			db.execSQL("CREATE TABLE " + TABLE_METHODS + " ("
					+ Methods._ID + " INTEGER PRIMARY KEY, "
					+ Methods.NAME + " TEXT, "
					+ Methods.CID + " INTEGER, "
					+ Methods.CHECKED + "BOOLEAN default 0"
					+ ");");
		}

		/**
		 * 
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("Database Upgrade", "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY + "," + TABLE_METHODS);
			onCreate(db);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	/**
	 * 
	 */
	@Override
	//query() returns the content of a table
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		case CATEGORY:
			qb.setTables(TABLE_CATEGORY);
			qb.setProjectionMap(sProjectionMap);
			break;

		case METHODS:
			qb.setTables(TABLE_METHODS);
			qb.setProjectionMap(sProjectionMap);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// Get the database and run the query
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

		// Tell the cursor what uri to watch, so it knows when its source data changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	/**
	 * 
	 */
	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case CATEGORY:
			return Category.CONTENT_TYPE;

		case METHODS:
			return Methods.CONTENT_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	/**
	 * 
	 */
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// Validate the requested uri
		if (sUriMatcher.match(uri) != CATEGORY && sUriMatcher.match(uri) != METHODS) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}


		SQLiteDatabase db = mOpenHelper.getWritableDatabase();

		long rowId;
		
		switch (sUriMatcher.match(uri)) {
		case CATEGORY:
			rowId = db.insert(TABLE_CATEGORY, Category.NAME, values);
			if (rowId > 0) {
				Uri noteUri = ContentUris.withAppendedId(Category.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(noteUri, null);
				return noteUri;
			}
		case METHODS:
			rowId = db.insert(TABLE_METHODS, Methods.NAME, values);
			if (rowId > 0) {
				Uri noteUri = ContentUris.withAppendedId(Methods.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(noteUri, null);
				return noteUri;
			}
			
			default:
			throw new SQLException("Failed to insert row into " + uri);
		}
	}

	/**
	 * 
	 */
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case CATEGORY:
			count = db.delete(TABLE_CATEGORY, where, whereArgs);
			break;

		case METHODS:
			count = db.delete(TABLE_METHODS, where, whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	/**
	 * 
	 */
	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case CATEGORY:
			count = db.update(TABLE_CATEGORY, values, where, whereArgs);
			break;

		case METHODS:
			count = db.update(TABLE_METHODS, values, where, whereArgs);		            
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	/**
	 * 
	 */
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(Database.AUTHORITY, TABLE_CATEGORY, CATEGORY);
		sUriMatcher.addURI(Database.AUTHORITY, TABLE_METHODS, METHODS);

		sProjectionMap = new HashMap<String, String>();
		sProjectionMap.put(Category._ID, Category._ID);
		sProjectionMap.put(Category.NAME, Category.NAME);
		sProjectionMap.put(Category.CHECKED, Category.CHECKED);
		sProjectionMap.put(Methods._ID, Methods._ID);
		sProjectionMap.put(Methods.NAME, Methods.NAME);
		sProjectionMap.put(Methods.CID, Methods.CID);
		sProjectionMap.put(Methods.CHECKED, Methods.CHECKED);
	}
}
