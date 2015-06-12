package com.pgrs.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBBaseAdapter {

	private static final String TAG = "DBBaseAdapter";

	protected static final String DATABASE_NAME = "db.Brief";
	protected static final int DATABASE_VERSION = 2;

	protected Context mContext;
	protected static DatabaseHelper mDbHelper;

	private static final String TABLE_CREATE_LINE = "create table LineMaster (LineId integer primary key autoincrement, "
			+ "LineCode text not null,"
			+ "LineName text not null,"
			+ "DeleteFlag text not null,"
			+ "CreatedBy text,"
			+ "CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";

	private static final String TABLE_CREATE_MEMBERS = "create table Members(MemberId integer primary key autoincrement, "
			+ "LineId integer not null,"
			+ "MemberCode text not null,"
			+ "MemberName text not null,"
			+ "Mobile text not null,"
			+ "AltNo text,"
			+ "GuaranteedBy text not null,"
			+ "Address text,"
			+ "CreatedBy text,"
			+ "DeleteFlag text not null,"
			+ "Photo BLOB,"
			+ "CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP)";

	private static final String TABLE_CREATE_RECEIPTS = "create table Receipts(ReceiptId integer primary key autoincrement, "
			+ "CollectionDate DATETIME not null,"
			+ "Memberid integer not null,"
			+ "LineId integer not null,"
			+ "TypeId integer not null,"
			+ "CollectedBy integer not null,"
			+ "Amount NUMERIC(10,2) not null,"
			+ "Notes text)";
			
	public DBBaseAdapter(Context context) {
		mContext = context.getApplicationContext();
	}

	public SQLiteDatabase openDb() {
		if (mDbHelper == null) {
			mDbHelper = new DatabaseHelper(mContext);
		}
		return mDbHelper.getWritableDatabase();
	}

	public void closeDb() {
		mDbHelper.close();
	}

	protected static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//db.execSQL("DROP TABLE " + TABLE_CREATE_MEMBERS);
			//db.execSQL("DROP TABLE " + TABLE_CREATE_LINE);
			db.execSQL(TABLE_CREATE_LINE);
			db.execSQL(TABLE_CREATE_MEMBERS);
			db.execSQL(TABLE_CREATE_RECEIPTS);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS routes");
			onCreate(db);
		}
	}
}