package com.pgrs.dal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.pgrs.models.CMemberCollection;
import com.pgrs.models.CMembersByLine;
import com.pgrs.models.TMembers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MembersDAL extends DBBaseAdapter {

	private static final String TAG = "MembersDAL";
	private static final String KEY_ROWID = "MemberId";
	private static final String DATABASE_TABLE = "Members";
	private static final String TABLE_SELECT = "Select MemberId," + "LineId,"
			+ "MemberCode," + "MemberName," + "Mobile," + "AltNo,"
			+ "GuaranteedBy," + "DeleteFlag," + "Address," + "CreatedBy," + "Photo,"
			+ "CreatedAt from Members";
	private static final String TABLE_COLLECTION = "Select M.MemberId,M.LineId,M.MemberName,M.MemberCode,M.Address,M.Mobile,C.CollectionDate,C.Amount from Members M left join Receipts C ON M.MemberId=C.MemberId AND M.LineId=C.LineId";

	public MembersDAL(Context context) {
		super(context);
	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	public TMembers GetMemberDetailByMemberId(int mMemberId) {

		TMembers tMembers = null;
		SQLiteDatabase db = openDb();
		Cursor c = db.rawQuery(TABLE_SELECT + " where MemberId == ?",
				new String[] { Integer.toString(mMemberId) });

		while (c.moveToNext()) {

			try {

				tMembers = GetValuesFromCursor(c);
			} catch (Exception e) {
				Log.e(TAG, "Error " + e.toString());
			}
		}
		c.close();
		if (db.isOpen())
			db.close();

		return tMembers;
	}

	public TMembers GetMemberByMemberIdAndCode(int MemberId, String MemberCode) {
		TMembers tMembers = null;

		SQLiteDatabase db = openDb();

		String[] whereArgs;
		String sql = "";

		if (MemberId > 0) {
			whereArgs = new String[] { Integer.toString(MemberId), MemberCode };
			sql = TABLE_SELECT + " where MemberId != ? AND MemberCode=?";
		} else {
			whereArgs = new String[] { MemberCode };
			sql = TABLE_SELECT + " where MemberCode=?";
		}

		Cursor c = db.rawQuery(sql, whereArgs);
		if (c != null) {
			while (c.moveToNext()) {
				tMembers = new TMembers();
				tMembers = GetValuesFromCursor(c);
			}
			c.close();
		}
		if (db.isOpen()) {
			db.close();
		}
		return tMembers;
	}

	private TMembers GetValuesFromCursor(Cursor c) {

		TMembers tMembers = new TMembers();

		tMembers.setLineId(c.getInt(c.getColumnIndex("LineId")));
		tMembers.setMemberId(c.getInt(c.getColumnIndex("MemberId")));
		tMembers.setMemberCode(c.getString(c.getColumnIndex("MemberCode")));
		tMembers.setMemberName(c.getString(c.getColumnIndex("MemberName")));
		tMembers.setMobileNumber(c.getString(c.getColumnIndex("Mobile")));
		tMembers.setAlternateNo(c.getString(c.getColumnIndex("AltNo")));
		tMembers.setGuaranteedBy(c.getString(c.getColumnIndex("GuaranteedBy")));
		tMembers.setAddress(c.getString(c.getColumnIndex("Address")));
		tMembers.setDeleteFlag(c.getString(c.getColumnIndex("DeleteFlag")));
		tMembers.setCreatedBy(c.getString(c.getColumnIndex("CreatedBy")));
		tMembers.setCreatedDate(c.getString(c.getColumnIndex("CreatedAt")));
		tMembers.setPhoto(c.getBlob(c.getColumnIndex("Photo")));
		
		return tMembers;

	}
	private CMemberCollection GetMemberCollectionValuesFromCursor(Cursor c) {

		CMemberCollection cMembers = new CMemberCollection();
		
		cMembers.setLineId(c.getInt(c.getColumnIndex("LineId")));
		cMembers.setMemberId(c.getInt(c.getColumnIndex("MemberId")));
		cMembers.setMemberCode(c.getString(c.getColumnIndex("MemberCode")));
		cMembers.setMemberName(c.getString(c.getColumnIndex("MemberName")));
		cMembers.setMobileNumber(c.getString(c.getColumnIndex("Mobile")));
		cMembers.setAddress(c.getString(c.getColumnIndex("Address")));
		cMembers.setCollectionDate(c.getString(c.getColumnIndex("CollectionDate")));
		cMembers.setAmount(c.getDouble(c.getColumnIndex("Amount")));
		
		return cMembers;

	}
	
	public ArrayList<TMembers> GetMembersListByLineId(int LineId) {

		ArrayList<TMembers> lstMembers = new ArrayList<TMembers>();

		SQLiteDatabase db = openDb();
		Cursor c = db.rawQuery(TABLE_SELECT + " where LineId == ?",
				new String[] { Integer.toString(LineId) });

		if (c != null) {
			TMembers tMembers = null;

			while (c.moveToNext()) {

				tMembers = new TMembers();

				try {

					tMembers = GetValuesFromCursor(c);

					if (tMembers != null) {
						lstMembers.add(tMembers);
					}

				} catch (Exception e) {
					Log.e(TAG, "Error " + e.toString());
				}
			}
		}
		if (db.isOpen())
			db.close();

		return lstMembers;

	}

	public ArrayList<TMembers> GetMembersList() {

		ArrayList<TMembers> lstMembers = new ArrayList<TMembers>();

		SQLiteDatabase db = openDb();
		Cursor c = db.rawQuery(TABLE_SELECT, null);
		TMembers tMembers = null;
		while (c.moveToNext()) {

			tMembers = new TMembers();

			try {

				tMembers = GetValuesFromCursor(c);

				if (tMembers != null) {
					lstMembers.add(tMembers);
				}

			} catch (Exception e) {
				Log.e(TAG, "Error " + e.toString());
			}
		}
		if (db.isOpen())
			db.close();

		return lstMembers;

	}

	
	public ArrayList<CMemberCollection> GetMembersListByCollectionDateAndLineId(String ColDate, int LineId) {

		ArrayList<CMemberCollection> lstMembers = new ArrayList<CMemberCollection>();
		
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String to_date = sdf.format(ColDate);
		SQLiteDatabase db = openDb();
		
		Cursor c = db.rawQuery(TABLE_COLLECTION + " where M.LineId = ? AND (C.CollectionDate=? Or C.CollectionDate IS NULL)",
				new String[] { Integer.toString(LineId) ,ColDate});

		if (c != null) {
			CMemberCollection cMembers = null;

			while (c.moveToNext()) {

				cMembers = new CMemberCollection();

				try {

					cMembers = GetMemberCollectionValuesFromCursor(c);

					if (cMembers != null) {
						lstMembers.add(cMembers);
					}

				} catch (Exception e) {
					Log.e(TAG, "Error " + e.toString());
				}
			}
		}
		if (db.isOpen())
			db.close();

		return lstMembers;

	}

	
	private ContentValues GetAsContentValue(TMembers tMembers) {
		ContentValues values = new ContentValues();
		if (tMembers.getMemberId() > 0)
			values.put("MemberId", tMembers.getMemberId());

		values.put("LineId", tMembers.getLineId());
		values.put("MemberCode", tMembers.getMemberCode());
		values.put("MemberName", tMembers.getMemberName());
		values.put("Mobile", tMembers.getMobileNumber());
		values.put("AltNo", tMembers.getAlternateNo());
		values.put("GuaranteedBy", tMembers.getGuaranteedBy());
		values.put("Address", tMembers.getAddress());
		values.put("CreatedBy", "Admin");
		values.put("CreatedAt", this.getDateTime());
		values.put("DeleteFlag", "N");
		values.put("photo",tMembers.getPhoto());

		return values;
	}

	public long insert(TMembers tMembers) {
		long id = -1;
		if (tMembers != null) {
			SQLiteDatabase db = openDb();

			try {

				ContentValues values = GetAsContentValue(tMembers);

				id = db.insert(DATABASE_TABLE, null, values);

			} catch (Exception ex) {
				Log.w(TAG, ex.getMessage());
			} finally {
				if (db.isOpen())
					db.close();
			}
		}
		return id;
	}

	public long Update(TMembers tMembers) {

		long id = -1;

		if (tMembers != null) {

			SQLiteDatabase db = openDb();
			try {

				ContentValues values = new ContentValues();
				values = GetAsContentValue(tMembers);

				id = db.update(DATABASE_TABLE, values, KEY_ROWID + "="
						+ tMembers.getMemberId(), null);
			} catch (Exception ex) {
				Log.w(TAG, ex.getMessage());
			} finally {
				if (db.isOpen())
					db.close();
			}

		}
		return id;
	}
}