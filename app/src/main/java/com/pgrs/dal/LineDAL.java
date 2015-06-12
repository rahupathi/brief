package com.pgrs.dal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.Locale;

import com.pgrs.models.TLine;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LineDAL extends DBBaseAdapter {

	private static final String TAG = "LineDAL";
	private static final String KEY_ROWID = "LineId";
	private static final String DATABASE_TABLE = "LineMaster";
	private static final String TABLE_SELECT = "Select Lineid, LineCode, LineName, CreatedAt,CreatedBy,DeleteFlag from LineMaster";

	public LineDAL(Context context) {
		super(context);
	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	public ArrayList<TLine> GetLineList() {

		ArrayList<TLine> lstLine = new ArrayList<TLine>();

		SQLiteDatabase db = openDb();
		Cursor c = db.rawQuery(TABLE_SELECT, null);

		if (c != null) {
			while (c.moveToNext()) {
				TLine tLine = new TLine();
				// String date = c.getString(c.getColumnIndex("date"));
				try {

					tLine.setLineId(c.getInt(c.getColumnIndex("LineId")));
					tLine.setLineCode(c.getString(c.getColumnIndex("LineCode")));
					tLine.setLineName(c.getString(c.getColumnIndex("LineName")));
					tLine.setCreatedDate(c.getString(c
							.getColumnIndex("CreatedAt")));
					tLine.setDeleteFlag(c.getString(c
							.getColumnIndex("DeleteFlag")));
					/*
					 * Calendar t = new GregorianCalendar(); SimpleDateFormat
					 * sdf = new
					 * SimpleDateFormat("dd.MM.yyyy",Locale.getDefault());
					 * java.util.Date dt =
					 * sdf.parse(c.getString(c.getColumnIndex("CreatedAt")));
					 * t.setTime(dt); tLine.setCreatedDate(t.toString());
					 */

					lstLine.add(tLine);

				} catch (Exception e) {
					Log.e(TAG, "Error " + e.toString());
				}
			}
			c.close();
		}

		if (db.isOpen())
			db.close();

		return lstLine;

	}

	public long insert(TLine tLine) {
		long id = -1;
		if (tLine != null) {
			SQLiteDatabase db = openDb();

			try {

				ContentValues values = new ContentValues();

				values.put("LineCode", tLine.getLineCode());
				values.put("LineName", tLine.getLineName());
				values.put("DeleteFlag", "N");
				values.put("CreatedBy", "Admin");
				values.put("CreatedAt", this.getDateTime());

				id = db.insert(DATABASE_TABLE, null, values);

			} catch (Exception ex) {
				Log.w(TAG, ex.getMessage());
			}
			closeDb();
		}
		return id;
	}

	public TLine GetLineByLineIdAndLineCode(int LineId, String LineCode) {
		TLine tLine = null;

		SQLiteDatabase db = openDb();

		String[] whereArgs;
		String sql = "";

		if (LineId > 0) {
			whereArgs = new String[] { Integer.toString(LineId), LineCode };
			sql = TABLE_SELECT + " where LineId != ? AND LineCode=?";
		} else {
			whereArgs = new String[] { LineCode };
			sql = TABLE_SELECT + " where LineCode=?";
		}

		Cursor c = db.rawQuery(sql, whereArgs);
		if (c != null) {
			while (c.moveToNext()) {
				tLine = new TLine();
				tLine.setLineId(c.getInt(c.getColumnIndex("LineId")));
				tLine.setLineCode(c.getString(c.getColumnIndex("LineCode")));
				tLine.setLineName(c.getString(c.getColumnIndex("LineName")));
				tLine.setCreatedDate(c.getString(c.getColumnIndex("CreatedAt")));
				tLine.setDeleteFlag(c.getString(c.getColumnIndex("DeleteFlag")));
			}
			c.close();
		}

		if (db.isOpen()) {
			db.close();
		}
		return tLine;
	}

	public TLine GetLineByLineId(int LineId) {
		TLine tLine = null;

		SQLiteDatabase db = openDb();

		Cursor c = db.rawQuery(TABLE_SELECT + " where LineId = ?",
				new String[] { Integer.toString(LineId) });
		if (c != null) {
			while (c.moveToNext()) {
				tLine = new TLine();
				tLine.setLineId(c.getInt(c.getColumnIndex("LineId")));
				tLine.setLineCode(c.getString(c.getColumnIndex("LineCode")));
				tLine.setLineName(c.getString(c.getColumnIndex("LineName")));
				tLine.setCreatedDate(c.getString(c.getColumnIndex("CreatedAt")));
				tLine.setDeleteFlag(c.getString(c.getColumnIndex("DeleteFlag")));
			}
			c.close();
		}

		if (db.isOpen()) {
			db.close();
		}
		return tLine;
	}
	public long Update(TLine tLine) {

		long id = -1;

		if (tLine != null) {

			SQLiteDatabase db = openDb();
			ContentValues values = new ContentValues();

			values.put("LineCode", tLine.getLineCode());
			values.put("LineName", tLine.getLineName());
			values.put("CreatedBy", "Admin");
			values.put("CreatedAt", this.getDateTime());

			id = db.update(DATABASE_TABLE, values,
					KEY_ROWID + "=" + tLine.getLineId(), null);
		}
		return id;
	}
}