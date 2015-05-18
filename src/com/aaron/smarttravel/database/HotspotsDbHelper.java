package com.aaron.smarttravel.database;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import com.aaron.smarttravel.database.CollisionLocationTable.CollisionLocationEntry;
import com.aaron.smarttravel.database.DataTypeTable.DayTypeEntry;
import com.aaron.smarttravel.database.LocationReasonTable.LocationReasonEntry;
import com.aaron.smarttravel.database.NewVersionTable.NewVersionEntry;
import com.aaron.smarttravel.database.ReasonConditionTable.ReasonConditionEntry;
import com.aaron.smarttravel.utilities.CollisionLocationObject;
import com.aaron.smarttravel.utilities.DayTypeObject;
import com.aaron.smarttravel.utilities.LocationReasonObject;
import com.aaron.smarttravel.utilities.NewVersionObject;
import com.aaron.smarttravel.utilities.WMReasonConditionObject;

public class HotspotsDbHelper extends SQLiteOpenHelper{
	public static final int DATABASE_VERSION=1;
	public static final String DATABASE_NAME="Hotspots.db";
	private static final String TEXT_TYPE=" TEXT";
	private static final String REAL_TYPE=" REAL";
	private static final String INTEGER_TYPE=" INTEGER";
	private static final String COMMA_SEP=",";
	private static final String SQL_CREATE_COLLISION_LOCATION_TABLE=
			"CREATE TABLE "+ CollisionLocationEntry.TABLE_NAME +" (" +CollisionLocationEntry._ID +" INTEGER PRIMARY KEY,"+
					CollisionLocationEntry.COLUMN_NAME_LOCATION_NAME +TEXT_TYPE+COMMA_SEP+
					CollisionLocationEntry.COLUMN_NAME_LOC_CODE+TEXT_TYPE+COMMA_SEP+
					CollisionLocationEntry.COLUMN_NAME_ROADWAY_PORTION+TEXT_TYPE+COMMA_SEP+
					CollisionLocationEntry.COLUMN_NAME_LONGITUDE+REAL_TYPE+COMMA_SEP+
					CollisionLocationEntry.COLUMN_NAME_LATITUDE+REAL_TYPE
					+" )";
	private static final String SQL_DELETE_COLLISION_LOCATION_TABLE="DROP TABLE IF EXISTS "+ CollisionLocationEntry.TABLE_NAME;
	
	private static final String SQL_CREATE_DATATYPE_TABLE="CREATE TABLE "+DayTypeEntry.TABLE_NAME +" ("+
			DayTypeEntry._ID +" INTEGER PRIMARY KEY, "+
			DayTypeEntry.COLUMN_WEEKDAY+INTEGER_TYPE+COMMA_SEP+
			DayTypeEntry.COLUMN_WEEKEND+INTEGER_TYPE+COMMA_SEP+
			DayTypeEntry.COLUMN_SCHOLL_DAY+INTEGER_TYPE+COMMA_SEP+
			DayTypeEntry.COLUMN_DATE+TEXT_TYPE
			+" )";
	private static final String SQL_DELETE_DATATYPE_TABLE="DROP TABLE IF EXISTS "+DayTypeEntry.TABLE_NAME;
	
	
	private static final String SQL_CREATE_LOCATION_REASON_TABLE=
			"CREATE TABLE "+ LocationReasonEntry.TABLE_NAME+" (" +LocationReasonEntry._ID +" INTEGER PRIMARY KEY, "+
					LocationReasonEntry.COLUMN_TOTAL+INTEGER_TYPE+COMMA_SEP+
					LocationReasonEntry.COLUMN_WARNING_PRIORITY+INTEGER_TYPE+COMMA_SEP+
					LocationReasonEntry.COLUMN_REASON_ID+INTEGER_TYPE+COMMA_SEP+
					LocationReasonEntry.COLUMN_TRAVEL_DIRECTION+TEXT_TYPE+COMMA_SEP+
					LocationReasonEntry.COLUMN_LOC_CODE+TEXT_TYPE
					+" )";
	private static final String SQL_DELETE_LOCATION_REASON_TABLE="DROP TABLE　IF EXISTS "+ LocationReasonEntry.TABLE_NAME;
	
	
	private static final String SQL_CREATE_REASON_CONDITION_TABLE=
			"CREATE TABLE "+ ReasonConditionEntry.TABLE_NAME+" ("+ReasonConditionEntry._ID +" INTEGER PRIMARY KEY, "+
					ReasonConditionEntry.COLUMN_WARNING_MESSAGE +TEXT_TYPE +COMMA_SEP+
					ReasonConditionEntry.COLUMN_WEEKDAY+INTEGER_TYPE+COMMA_SEP+
					ReasonConditionEntry.COLUMN_REASON+TEXT_TYPE+COMMA_SEP+
					ReasonConditionEntry.COLUMN_WEEKEND+INTEGER_TYPE+COMMA_SEP+
					ReasonConditionEntry.COLUMN_END_TIME+TEXT_TYPE+COMMA_SEP+
					ReasonConditionEntry.COLUMN_REASON_ID+INTEGER_TYPE+COMMA_SEP+
					ReasonConditionEntry.COLUMN_MONTH+TEXT_TYPE+COMMA_SEP+
					ReasonConditionEntry.COLUMN_START_TIME+TEXT_TYPE+COMMA_SEP+
					ReasonConditionEntry.COLUMN_SCHOLL_DAY+INTEGER_TYPE
					+" )";
	private static final String SQL_DELETE_REASON_CONDITION_TABLE="DROP IF EXISTS "+ ReasonConditionEntry.TABLE_NAME;
	
	
	private static final String SQL_CREATE_NEW_VERSION_TABLE=
			"CREATE TABLE "+ NewVersionEntry.TABLE_NAME+" ("+ NewVersionEntry._ID+" INTEGER PRIMARY KEY, "+
					NewVersionEntry.COLUMN_VERSION+TEXT_TYPE
					+" )";
	private static final String SQL_DELETE_NEW_VERSION_TABLE="DROP IF EXISTS "+ NewVersionEntry.TABLE_NAME;

	public HotspotsDbHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE_COLLISION_LOCATION_TABLE);
		db.execSQL(SQL_CREATE_DATATYPE_TABLE);
		db.execSQL(SQL_CREATE_LOCATION_REASON_TABLE);
		db.execSQL(SQL_CREATE_NEW_VERSION_TABLE);
		db.execSQL(SQL_CREATE_REASON_CONDITION_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_DELETE_COLLISION_LOCATION_TABLE);
		db.execSQL(SQL_DELETE_DATATYPE_TABLE);
		db.execSQL(SQL_DELETE_LOCATION_REASON_TABLE);
		db.execSQL(SQL_DELETE_NEW_VERSION_TABLE);
		db.execSQL(SQL_DELETE_REASON_CONDITION_TABLE);
		onCreate(db);
	}

	public void insertCollisionLocationTableData(ArrayList<CollisionLocationObject> arrayList){
		
		SQLiteDatabase db=this.getWritableDatabase();
		
		if (!arrayList.isEmpty()) {
			db.delete(CollisionLocationEntry.TABLE_NAME, null, null);
			for (int i = 0; i < arrayList.size(); i++) {
				ContentValues temp_contentValues=new ContentValues();
				CollisionLocationObject temp_Object=arrayList.get(i);
				temp_contentValues.put(CollisionLocationEntry.COLUMN_NAME_LOCATION_NAME, temp_Object.getLocation_name());
				temp_contentValues.put(CollisionLocationEntry.COLUMN_NAME_ROADWAY_PORTION, temp_Object.getRoadway_portion());
				temp_contentValues.put(CollisionLocationEntry.COLUMN_NAME_LOC_CODE, temp_Object.getLoc_code());
				temp_contentValues.put(CollisionLocationEntry.COLUMN_NAME_LATITUDE, temp_Object.getLatitude());
				temp_contentValues.put(CollisionLocationEntry.COLUMN_NAME_LONGITUDE, temp_Object.getLongitude());
				
				db.insert(CollisionLocationEntry.TABLE_NAME, null, temp_contentValues);				
			}
		}
	}
	
	public CollisionLocationObject getNearbyLocation(Location currentLocation){
		CollisionLocationObject temp_CollisionLocationObject=new CollisionLocationObject();
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from "+CollisionLocationEntry.TABLE_NAME, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Location tempLocation=new Location("temp");
			tempLocation.setLatitude(cursor.getDouble(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LATITUDE)));
			tempLocation.setLongitude(cursor.getDouble(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LONGITUDE)));
			if (currentLocation.distanceTo(tempLocation)<500) {
				temp_CollisionLocationObject.setLatitude(tempLocation.getLatitude());
				temp_CollisionLocationObject.setLongitude(tempLocation.getLongitude());
				temp_CollisionLocationObject.setLoc_code(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LOC_CODE)));
				temp_CollisionLocationObject.setLocation_name(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LOCATION_NAME)));
				temp_CollisionLocationObject.setRoadway_portion(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_ROADWAY_PORTION)));	
				break;
			}
			cursor.moveToNext();
		}
		return temp_CollisionLocationObject;
	}
	
	public LocationReasonObject getLocationReasonByLocCode(String loc_codeString){
		LocationReasonObject temp_locationReasonObject=new LocationReasonObject();
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from "+LocationReasonEntry.TABLE_NAME, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			if (cursor.getString(cursor.getColumnIndex(LocationReasonEntry.COLUMN_LOC_CODE))==loc_codeString) {
				
			}
		}
		return temp_locationReasonObject;
	}
	
	public void insertLocationReasonTableData(ArrayList<LocationReasonObject> arrayList){
		
		SQLiteDatabase db=this.getWritableDatabase();
		if (!arrayList.isEmpty()) {
			db.delete(LocationReasonEntry.TABLE_NAME, null, null);
			for (int i = 0; i < arrayList.size(); i++) {
				ContentValues temp_contentValues=new ContentValues();
				LocationReasonObject temp_object=arrayList.get(i);
				temp_contentValues.put(LocationReasonEntry.COLUMN_TOTAL, temp_object.getTotal());
				temp_contentValues.put(LocationReasonEntry.COLUMN_REASON_ID, temp_object.getReason_id());
				temp_contentValues.put(LocationReasonEntry.COLUMN_LOC_CODE, temp_object.getLoc_code());
				temp_contentValues.put(LocationReasonEntry.COLUMN_TRAVEL_DIRECTION, temp_object.getTravel_direction());
				temp_contentValues.put(LocationReasonEntry.COLUMN_WARNING_PRIORITY, temp_object.getWarning_priority());
				
				db.insert(LocationReasonEntry.TABLE_NAME, null, temp_contentValues);
			}
		}
	}
	
	
	public void insertReasonConditionTableData(ArrayList<WMReasonConditionObject> arrayList){
		SQLiteDatabase db=this.getWritableDatabase();
		
		if (!arrayList.isEmpty()) {
			db.delete(ReasonConditionEntry.TABLE_NAME, null, null);
			for (int i = 0; i < arrayList.size(); i++) {
				ContentValues temp_contentValues=new ContentValues();
				WMReasonConditionObject temp_object=arrayList.get(i);
				
				temp_contentValues.put(ReasonConditionEntry.COLUMN_WARNING_MESSAGE, temp_object.getWarning_message());
				temp_contentValues.put(ReasonConditionEntry.COLUMN_WEEKDAY, temp_object.getWeekday()?1:0);
				temp_contentValues.put(ReasonConditionEntry.COLUMN_REASON, temp_object.getReason());
				temp_contentValues.put(ReasonConditionEntry.COLUMN_WEEKEND, temp_object.getWeekend()?1:0);
				temp_contentValues.put(ReasonConditionEntry.COLUMN_END_TIME, temp_object.getEnd_time());
				temp_contentValues.put(ReasonConditionEntry.COLUMN_REASON_ID, temp_object.getReason_id());
				temp_contentValues.put(ReasonConditionEntry.COLUMN_MONTH, temp_object.getMonth());
				temp_contentValues.put(ReasonConditionEntry.COLUMN_START_TIME, temp_object.getStart_time());
				temp_contentValues.put(ReasonConditionEntry.COLUMN_SCHOLL_DAY, temp_object.getScholl_day()?1:0);
				db.insert(ReasonConditionEntry.TABLE_NAME, null, temp_contentValues);	
			}
		}
		
	}
	
	public void insertDayTypeTableData(ArrayList<DayTypeObject> arrayList){
		SQLiteDatabase db=this.getWritableDatabase();
		
		if (!arrayList.isEmpty()) {
			db.delete(DayTypeEntry.TABLE_NAME, null, null);
			for (int i = 0; i < arrayList.size(); i++) {
				ContentValues temp_conValues=new ContentValues();
				DayTypeObject temp_object=arrayList.get(i);
				
				temp_conValues.put(DayTypeEntry.COLUMN_WEEKDAY, temp_object.getWeekday()?1:0);
				temp_conValues.put(DayTypeEntry.COLUMN_WEEKEND, temp_object.getWeekend()?1:0);
				temp_conValues.put(DayTypeEntry.COLUMN_DATE, temp_object.getDate());
				temp_conValues.put(DayTypeEntry.COLUMN_SCHOLL_DAY, temp_object.getSchool_day()?1:0);
				
				db.insert(DayTypeEntry.TABLE_NAME, null, temp_conValues);
			}
			
		}
	}
	
	public void insertNewVersionTableData(String versionString){
		
		SQLiteDatabase db=this.getWritableDatabase();
		if (versionString.isEmpty()) {
			db.delete(NewVersionEntry.TABLE_NAME, null, null);
			ContentValues temp_contentValues=new ContentValues();
			NewVersionObject temp_object=new NewVersionObject();
			temp_object.setNew_version(versionString);
			temp_contentValues.put(NewVersionEntry.COLUMN_VERSION, temp_object.getNew_version());
			db.insert(NewVersionEntry.TABLE_NAME, null, temp_contentValues);
		}
	}
	
}
