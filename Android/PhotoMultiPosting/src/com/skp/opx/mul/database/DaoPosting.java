package com.skp.opx.mul.database;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

/**
 * @설명 : Posting DAO 
 * @클래스명 : DaoPosting 
 *
 */
public class DaoPosting {

	private static DaoPosting sTheInstance = null;
	private DaoPosting() {}

	public static synchronized DaoPosting getInstance() {

		if(sTheInstance == null) {
			sTheInstance = new DaoPosting();
		}

		return sTheInstance;
	}

	public void deleteAllData(Context context) throws SQLiteException {

		String sql = "delete from " + EntityPostingDB.class.getSimpleName();
		Helper.getInstance(context).getWritableDatabase().execSQL(sql, new String[0]);
		Helper.getInstance(context).close();
	}	

	public void deleteData(Context context, long id) {

		String sql ="delete from " + EntityPostingDB.class.getSimpleName() + " where ID = " + id;
		Helper.getInstance(context).getWritableDatabase().execSQL(sql);
		Helper.getInstance(context).close();
	}		

	public long insertPostingInfo(Context context, EntityPostingDB entity) throws Exception {

		ContentValues values = new ContentValues();

		Field fieldArray[] = entity.getClass().getFields();
		for(int count = 0, maxCount = fieldArray.length; count < maxCount; count++) {
			if(fieldArray[count].getType() == int.class) {
				values.put(fieldArray[count].getName(), (Integer)fieldArray[count].get(entity));
			} else if(fieldArray[count].getType() == long.class) {
				values.put(fieldArray[count].getName(), (Long)fieldArray[count].get(entity));
			} else if(fieldArray[count].getType() == String.class) {
				values.put(fieldArray[count].getName(), (String)fieldArray[count].get(entity));
			}
		}

		long extraID = Helper.getInstance(context).getWritableDatabase().insert(entity.getClass().getSimpleName(), null, values);
		Helper.getInstance(context).close();
		return extraID;
	}

		public ArrayList<EntityPostingDB> getPostingInfoList(Context context) throws SQLiteException {
			String sql = "select * from " + EntityPostingDB.class.getSimpleName();
			return selectPostingInfoList(context, sql);
		}

	public ArrayList<EntityPostingDB> getPostingInfoListSocialType(Context context, String social) throws Exception {

		String sql = "select * from " + EntityPostingDB.class.getSimpleName() + " where social = " + "'" + social + "'";
		return selectPostingInfoList(context, sql);
	}

	protected ArrayList<EntityPostingDB> selectPostingInfoList(Context context, String sql) throws SQLiteException {

		ArrayList<EntityPostingDB> rowList = new ArrayList<EntityPostingDB>();
		Cursor cursor = null;

		try {
			cursor = Helper.getInstance(context).getWritableDatabase().rawQuery(sql, null);

			if(cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				EntityPostingDB postingInfo = null;

				while(!cursor.isAfterLast()) {
					postingInfo = setPostingInfo(cursor);
					rowList.add(postingInfo);
					cursor.moveToNext();
				}

			} 
		} catch(Exception e) 	{
			throw new SQLiteException(e.getMessage());
		} finally {
			if(cursor != null) {
				cursor.deactivate();
				cursor.close();
				Helper.getInstance(context).close();
			}
		}

		return rowList;
	}


	private EntityPostingDB setPostingInfo(Cursor cursor) throws Exception {

		EntityPostingDB entity = new EntityPostingDB();
		Field fieldArray[] = entity.getClass().getFields();

		for(int index = 0, maxIndex = fieldArray.length; index < maxIndex; index++) {
			int colIndex = 0;
			colIndex = cursor.getColumnIndex(fieldArray[index].getName());

			if(colIndex >= 0){
				if(fieldArray[index].getType() == int.class) {
					fieldArray[index].set(entity, cursor.getInt(colIndex));
				} else if(fieldArray[index].getType() == long.class) {
					fieldArray[index].set(entity, cursor.getLong(colIndex));
				} else if(fieldArray[index].getType() == String.class) {
					fieldArray[index].set(entity, cursor.getString(colIndex));
				}
			}
		}

		return entity;
	}

	protected int selectPostingInfoCount(Context context, String sql) throws SQLiteException {

		Cursor cursor = null;

		try{
			cursor = Helper.getInstance(context).getWritableDatabase().rawQuery(sql, null);
			int count  = 0;
			if(cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				count = cursor.getInt(cursor.getColumnIndex("RowCount"));
			}

			return count;
		} catch(Exception e) 	{
			throw new SQLiteException(e.getMessage());
		} finally {
			if(cursor != null) {
				cursor.deactivate();
				cursor.close();
				Helper.getInstance(context).close();
			}
		}
	}

}