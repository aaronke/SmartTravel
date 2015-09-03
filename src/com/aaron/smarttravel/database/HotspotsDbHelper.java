package com.aaron.smarttravel.database;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import com.aaron.smarttravel.database.CollisionLocationTable.CollisionLocationEntry;
import com.aaron.smarttravel.database.DateTypeTable.DayTypeEntry;
import com.aaron.smarttravel.database.LocationReasonTable.LocationReasonEntry;
import com.aaron.smarttravel.database.NewVersionTable.NewVersionEntry;
import com.aaron.smarttravel.database.ReasonConditionTable.ReasonConditionEntry;
import com.aaron.smarttravel.utilities.CollisionLocationObject;
import com.aaron.smarttravel.utilities.DataHandler;
import com.aaron.smarttravel.utilities.DayTypeObject;
import com.aaron.smarttravel.utilities.LocationReasonObject;
import com.aaron.smarttravel.utilities.NavDrawerItem;
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
			double distance=currentLocation.distanceTo(tempLocation);
			double current_speed=currentLocation.getSpeed();
			int warning_distance=150;
			//Log.v("STTest", "speed:"+current_speed);
			if (current_speed>=12) {
				warning_distance=(int) (10*current_speed);
			}
			if (distance<warning_distance) {
				temp_CollisionLocationObject.setLatitude(tempLocation.getLatitude());
				temp_CollisionLocationObject.setLongitude(tempLocation.getLongitude());
				temp_CollisionLocationObject.setLoc_code(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LOC_CODE)));
				temp_CollisionLocationObject.setLocation_name(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LOCATION_NAME)));
				temp_CollisionLocationObject.setRoadway_portion(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_ROADWAY_PORTION)));	
				
				break;
			}
			cursor.moveToNext();
		}
		cursor.close();
		return temp_CollisionLocationObject;
	}
	
	public ArrayList<LocationReasonObject> getLocationReasonByLocCode(String loc_codeString){
		
		ArrayList<LocationReasonObject> temp_arraylist_LocationReasonObjects=new ArrayList<LocationReasonObject>();
		
		SQLiteDatabase db=this.getReadableDatabase();
		
		Cursor cursor=db.rawQuery("select * from "+LocationReasonEntry.TABLE_NAME+" where "+LocationReasonEntry.COLUMN_LOC_CODE+"=?", new String[] {loc_codeString});
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()) {
			LocationReasonObject temp_locationReasonObject=new LocationReasonObject();
			temp_locationReasonObject.setLoc_code(loc_codeString);
			temp_locationReasonObject.setReason_id(cursor.getInt(cursor.getColumnIndex(LocationReasonEntry.COLUMN_REASON_ID)));
			temp_locationReasonObject.setTotal(cursor.getInt(cursor.getColumnIndex(LocationReasonEntry.COLUMN_TOTAL)));
			temp_locationReasonObject.setTravel_direction(cursor.getString(cursor.getColumnIndex(LocationReasonEntry.COLUMN_TRAVEL_DIRECTION)));
			temp_locationReasonObject.setWarning_priority(cursor.getInt(cursor.getColumnIndex(LocationReasonEntry.COLUMN_WARNING_PRIORITY)));
			temp_arraylist_LocationReasonObjects.add(temp_locationReasonObject);
			cursor.moveToNext();
		}
		cursor.close();
		return temp_arraylist_LocationReasonObjects;
	}
	public WMReasonConditionObject getWMReasonConditionByReasonID(int reason_id){
		WMReasonConditionObject temp_WMReasonConditionObject=new WMReasonConditionObject();
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from "+ReasonConditionEntry.TABLE_NAME+" where "+ ReasonConditionEntry.COLUMN_REASON_ID+"= "+
		reason_id, null);
		if (cursor.moveToFirst()) {
			temp_WMReasonConditionObject.setEnd_time(cursor.getString(cursor.getColumnIndex(ReasonConditionEntry.COLUMN_END_TIME)));
			temp_WMReasonConditionObject.setMonth(cursor.getString(cursor.getColumnIndex(ReasonConditionEntry.COLUMN_MONTH)));
			temp_WMReasonConditionObject.setReason(cursor.getString(cursor.getColumnIndex(ReasonConditionEntry.COLUMN_REASON)));
			temp_WMReasonConditionObject.setReason_id(reason_id);
			temp_WMReasonConditionObject.setScholl_day(cursor.getInt(cursor.getColumnIndex(ReasonConditionEntry.COLUMN_SCHOLL_DAY))==1 ? true: false);
			temp_WMReasonConditionObject.setStart_time(cursor.getString(cursor.getColumnIndex(ReasonConditionEntry.COLUMN_START_TIME)));
			temp_WMReasonConditionObject.setWarning_message(cursor.getString(cursor.getColumnIndex(ReasonConditionEntry.COLUMN_WARNING_MESSAGE)));
			temp_WMReasonConditionObject.setWeekday(cursor.getInt(cursor.getColumnIndex(ReasonConditionEntry.COLUMN_WEEKDAY))==1? true: false);
			
			temp_WMReasonConditionObject.setWeekend(cursor.getInt(cursor.getColumnIndex(ReasonConditionEntry.COLUMN_WEEKEND))==1? true: false);
		}
		cursor.close();
		return temp_WMReasonConditionObject;
	}
	
	public DayTypeObject getDayTypeObjectByDay(String day_of_year){
		DayTypeObject temp_dayTypeObject=new DayTypeObject();
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from "+ DayTypeEntry.TABLE_NAME +" where "+ DayTypeEntry.COLUMN_DATE+"=?"
		, new String[]{day_of_year});
		if (cursor.moveToFirst()) {
			temp_dayTypeObject.setDate(cursor.getString(cursor.getColumnIndex(DayTypeEntry.COLUMN_DATE)));
			temp_dayTypeObject.setSchool_day(cursor.getInt(cursor.getColumnIndex(DayTypeEntry.COLUMN_SCHOLL_DAY))==1? true:false);
			temp_dayTypeObject.setWeekday(cursor.getInt(cursor.getColumnIndex(DayTypeEntry.COLUMN_WEEKDAY))==1? true:false);
			temp_dayTypeObject.setWeekend(cursor.getInt(cursor.getColumnIndex(DayTypeEntry.COLUMN_WEEKEND))==1? true:false);
		}
		cursor.close();
		
		return temp_dayTypeObject;
	}
	
	public ArrayList<NavDrawerItem> getAllObjectsByType(String typeString,Boolean is_at_shanghai){
			ArrayList<NavDrawerItem> navDrawerItems=new ArrayList<NavDrawerItem>();
			SQLiteDatabase db=this.getReadableDatabase();
			Cursor cursor=db.rawQuery("select * from "+ CollisionLocationEntry.TABLE_NAME+" where " +
			CollisionLocationEntry.COLUMN_NAME_ROADWAY_PORTION +"= ? ORDER BY "+CollisionLocationEntry.COLUMN_NAME_LOCATION_NAME, new String[]{typeString});
			String temp_loc_code;
		//	LocationReasonObject temp_locationReasonObject=new LocationReasonObject();
			cursor.moveToFirst();
			DataHandler dataHandler=new DataHandler();
			
			while (!cursor.isAfterLast()) {
				if (is_at_shanghai==false && Double.parseDouble(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LONGITUDE)))>0) {
					// do nothing, the test location in Shanghai, no need to show in Edmonton.
				}else {
					NavDrawerItem temp_navDrawerItem=new NavDrawerItem();
					temp_navDrawerItem.setName_hotspot(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LOCATION_NAME)));
					temp_navDrawerItem.setType_hotspot(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_ROADWAY_PORTION)));
					temp_loc_code=cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LOC_CODE));
					
					temp_navDrawerItem.setCount_collisions(dataHandler.getTotalCollisionCount(getLocationReasonByLocCode(temp_loc_code)));
					navDrawerItems.add(temp_navDrawerItem);
				}

				cursor.moveToNext();
			}
			cursor.close();
			return navDrawerItems;
	}
	
	
	public ArrayList<CollisionLocationObject> getCollisionObjectsByType(String typeString){
		ArrayList<CollisionLocationObject> tempArrayList=new ArrayList<CollisionLocationObject>();
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from "+ CollisionLocationEntry.TABLE_NAME+" where " +
				CollisionLocationEntry.COLUMN_NAME_ROADWAY_PORTION +"= ?", new String[]{typeString});
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CollisionLocationObject temp_object=new CollisionLocationObject();
			temp_object.setLocation_name(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LOCATION_NAME)));
			temp_object.setLatitude(cursor.getDouble(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LATITUDE)));
			temp_object.setLongitude(cursor.getDouble(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LONGITUDE)));
			temp_object.setLoc_code(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LOC_CODE)));
			temp_object.setRoadway_portion(typeString);
			
			tempArrayList.add(temp_object);
			cursor.moveToNext();
		}
		cursor.close();
		return tempArrayList;
	}
	
	public CollisionLocationObject getcolllicionObjectByName(String locaiton_name){
		CollisionLocationObject temp_object=new CollisionLocationObject();
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from "+ CollisionLocationEntry.TABLE_NAME+" where " +
				CollisionLocationEntry.COLUMN_NAME_LOCATION_NAME +"= ?", new String[]{locaiton_name});
		
		if (cursor.moveToFirst()) {
			temp_object.setLocation_name(locaiton_name);
			temp_object.setLatitude(cursor.getDouble(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LATITUDE)));
			temp_object.setLongitude(cursor.getDouble(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LONGITUDE)));
			temp_object.setLoc_code(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LOC_CODE)));
			temp_object.setRoadway_portion(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_ROADWAY_PORTION)));
		}
		return temp_object;
	}
	public String getVersionString(){
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery(" select * from "+NewVersionEntry.TABLE_NAME, null);
		if (cursor.moveToFirst()) {
			return cursor.getString(cursor.getColumnIndex(NewVersionEntry.COLUMN_VERSION));
		}else {
			return null;
		}		
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
		if (!versionString.isEmpty()) {
			db.delete(NewVersionEntry.TABLE_NAME, null, null);
			ContentValues temp_contentValues=new ContentValues();
			NewVersionObject temp_object=new NewVersionObject();
			temp_object.setNew_version(versionString);
			temp_contentValues.put(NewVersionEntry.COLUMN_VERSION, temp_object.getNew_version());
			db.insert(NewVersionEntry.TABLE_NAME, null, temp_contentValues);
		}
	}
	
	
}
