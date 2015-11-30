package com.aaron.smarttravel.database;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import com.aaron.smarttravel.database.CollisionLocationTable.CollisionLocationEntry;
import com.aaron.smarttravel.database.DateTypeTable.DayTypeEntry;
import com.aaron.smarttravel.database.LocationReasonTable.LocationReasonEntry;
import com.aaron.smarttravel.database.NewVersionTable.NewVersionEntry;
import com.aaron.smarttravel.database.ReasonConditionTable.ReasonConditionEntry;
import com.aaron.smarttravel.database.SchoolZoneTable.SchoolZoneEntry;
import com.aaron.smarttravel.utilities.CollisionLocationObject;
import com.aaron.smarttravel.utilities.DataHandler;
import com.aaron.smarttravel.utilities.DayTypeObject;
import com.aaron.smarttravel.utilities.LocationReasonObject;
import com.aaron.smarttravel.utilities.NavDrawerItem;
import com.aaron.smarttravel.utilities.NewVersionObject;
import com.aaron.smarttravel.utilities.SchoolZoneObject;
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
					ReasonConditionEntry.COLUMN_SCHOLL_DAY+ INTEGER_TYPE+COMMA_SEP+
					ReasonConditionEntry.COLUMN_CATEGORY+ TEXT_TYPE
					+" )";
	private static final String SQL_DELETE_REASON_CONDITION_TABLE="DROP IF EXISTS "+ ReasonConditionEntry.TABLE_NAME;
	
	private static final String SQL_CREATE_REASON_SCHOOL_TABLE=
			"CREATE TABLE "+ SchoolZoneEntry.TABLE_NAME+" ("+SchoolZoneEntry._ID +" INTEGER PRIMARY KEY, "+ 
					SchoolZoneEntry.COLUMN_ID +INTEGER_TYPE+COMMA_SEP+
					SchoolZoneEntry.COLUMN_SCHOOL_TYPE +TEXT_TYPE+COMMA_SEP+
					SchoolZoneEntry.COLUMN_SCHOOL_NAME +TEXT_TYPE+COMMA_SEP+
					SchoolZoneEntry.COLUMN_ADDRESS +TEXT_TYPE+COMMA_SEP+
					SchoolZoneEntry.COLUMN_LONGITUDE +REAL_TYPE+COMMA_SEP+
					SchoolZoneEntry.COLUMN_LATITUDE +REAL_TYPE+COMMA_SEP+
					SchoolZoneEntry.COLUMN_SZ_SEFMENTS+ TEXT_TYPE+COMMA_SEP+
					SchoolZoneEntry.COLUMN_GRADE_LEVEL+ TEXT_TYPE
					+" )";
	private static final String SQL_DELETE_SCHOOL_ZONES_TABLE="DROP IF EXISTS "+ SchoolZoneEntry.TABLE_NAME;
	
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
		db.execSQL(SQL_CREATE_REASON_SCHOOL_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_DELETE_COLLISION_LOCATION_TABLE);
		db.execSQL(SQL_DELETE_DATATYPE_TABLE);
		db.execSQL(SQL_DELETE_LOCATION_REASON_TABLE);
		db.execSQL(SQL_DELETE_NEW_VERSION_TABLE);
		db.execSQL(SQL_DELETE_REASON_CONDITION_TABLE);
		db.execSQL(SQL_DELETE_SCHOOL_ZONES_TABLE);
		onCreate(db);
	}

	public void insertCollisionLocationTableData(ArrayList<CollisionLocationObject> arrayList){
		
		SQLiteDatabase db=this.getWritableDatabase();
		
		if (!arrayList.isEmpty()) {
			db.delete(CollisionLocationEntry.TABLE_NAME, null, null);
			db.beginTransaction();
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
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
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
			int warning_distance=120;
			//Log.v("STTest", "speed:"+current_speed);
			if (current_speed>=12) {
				warning_distance=(int) (9*current_speed);
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
		Cursor cursor=db.rawQuery("select * from "+ DayTypeEntry.TABLE_NAME +" where "+ DayTypeEntry.COLUMN_DATE+"=? COLLATE NOCASE"
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
	
	public HashMap<String,ArrayList<Integer>> getReasonList(){
		HashMap<String,ArrayList<Integer>> lisHeaderArrayList=new HashMap<String,ArrayList<Integer>>();
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from "+ReasonConditionEntry.TABLE_NAME, null);
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			String category_temp=cursor.getString(cursor.getColumnIndex(ReasonConditionEntry.COLUMN_CATEGORY));
			int reason_id=cursor.getInt(cursor.getColumnIndex(ReasonConditionEntry.COLUMN_REASON_ID));
			if (lisHeaderArrayList.containsKey(category_temp)&& category_temp!=null) 
				lisHeaderArrayList.get(category_temp).add(reason_id);
			else {
				ArrayList<Integer> arrayList=new ArrayList<>();
				arrayList.add(reason_id);
				lisHeaderArrayList.put(category_temp, arrayList);
			}
			cursor.moveToNext();
		}
			cursor.close();
		return lisHeaderArrayList;
		
	}
	
	public ArrayList<SchoolZoneObject> getSchoolZoneObjects(){
		ArrayList<SchoolZoneObject> arrayList=new ArrayList<SchoolZoneObject>();
			SQLiteDatabase db=this.getReadableDatabase();
			Cursor cursor=db.rawQuery("select  * from "+ SchoolZoneEntry.TABLE_NAME, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				SchoolZoneObject temp_school_zoneObject=new SchoolZoneObject();
				temp_school_zoneObject.setId(cursor.getInt(cursor.getColumnIndex(SchoolZoneEntry.COLUMN_ID)));
				temp_school_zoneObject.setSchool_type(cursor.getString(cursor.getColumnIndex(SchoolZoneEntry.COLUMN_SCHOOL_TYPE)));
				temp_school_zoneObject.setSchool_name(cursor.getString(cursor.getColumnIndex(SchoolZoneEntry.COLUMN_SCHOOL_NAME)));
				temp_school_zoneObject.setLongitude(cursor.getDouble(cursor.getColumnIndex(SchoolZoneEntry.COLUMN_LONGITUDE)));
				temp_school_zoneObject.setLatitude(cursor.getDouble(cursor.getColumnIndex(SchoolZoneEntry.COLUMN_LATITUDE)));
				temp_school_zoneObject.setSz_segments(cursor.getString(cursor.getColumnIndex(SchoolZoneEntry.COLUMN_SZ_SEFMENTS)));
				temp_school_zoneObject.setAddress(cursor.getString(cursor.getColumnIndex(SchoolZoneEntry.COLUMN_ADDRESS)));
				temp_school_zoneObject.setGrade_level(cursor.getString(cursor.getColumnIndex(SchoolZoneEntry.COLUMN_GRADE_LEVEL)));
				
				arrayList.add(temp_school_zoneObject);	
				cursor.moveToNext();
			}
			cursor.close();
		return arrayList;
	}
	public ArrayList<NavDrawerItem> getAllObjectByReasonId(int reason_id,Boolean is_at_shanghai){
		ArrayList<NavDrawerItem> navDrawerItems=new ArrayList<NavDrawerItem>();
		SQLiteDatabase db=this.getReadableDatabase();
		String temp_loc_code;
		Cursor cursor=db.rawQuery("select * from "+ LocationReasonEntry.TABLE_NAME +" where "+LocationReasonEntry.COLUMN_REASON_ID +
				"="+reason_id, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
				NavDrawerItem temp_navDrawerItem=new NavDrawerItem();
				CollisionLocationObject collisionLocationObject=new CollisionLocationObject();
				temp_navDrawerItem.setCount_collisions(cursor.getShort(cursor.getColumnIndex(LocationReasonEntry.COLUMN_TOTAL)));
				temp_loc_code=cursor.getString(cursor.getColumnIndex(LocationReasonEntry.COLUMN_LOC_CODE));
				
				collisionLocationObject=getcolllicionObjectByLocCode(temp_loc_code);
				temp_navDrawerItem.setName_hotspot(collisionLocationObject.getLocation_name());
				temp_navDrawerItem.setType_hotspot(collisionLocationObject.getRoadway_portion());
				if (is_at_shanghai==false && collisionLocationObject.getLongitude()>0) {
					// do nothing, filter the test locations in shanghai 
				}else {
					navDrawerItems.add(temp_navDrawerItem);
				}
				
			cursor.moveToNext();
		}
			cursor.close();
			// sort the school zone by school name
		if (reason_id==23) {
			Collections.sort(navDrawerItems, new Comparator<NavDrawerItem>() {
				@Override
				public int compare(NavDrawerItem lhs,NavDrawerItem  rhs) {
					// TODO Auto-generated method stub
					return lhs.getName_hotspot().compareToIgnoreCase(rhs.getName_hotspot());
				}
			});
		}
		return navDrawerItems;
		
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
		cursor.close();
		return temp_object;
	}
	
	public SchoolZoneObject getSchoolZoneObjectByName(String school_name){
			SchoolZoneObject temp_schoolZoneObject=new SchoolZoneObject();
			SQLiteDatabase db=this.getReadableDatabase();
			Cursor cursor=db.rawQuery("select * from "+SchoolZoneEntry.TABLE_NAME+" where "+ 
			SchoolZoneEntry.COLUMN_SCHOOL_NAME +"= ?", new String[]{school_name});
			if (cursor.moveToFirst()) {
				temp_schoolZoneObject.setSchool_name(school_name);
				temp_schoolZoneObject.setLatitude(cursor.getDouble(cursor.getColumnIndex(SchoolZoneEntry.COLUMN_LATITUDE)));
				temp_schoolZoneObject.setLongitude(cursor.getDouble(cursor.getColumnIndex(SchoolZoneEntry.COLUMN_LONGITUDE)));
			}
		return temp_schoolZoneObject;
	}

	public CollisionLocationObject getcolllicionObjectByLocCode(String loc_code){
		CollisionLocationObject temp_object=new CollisionLocationObject();
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from "+ CollisionLocationEntry.TABLE_NAME+" where " +
				CollisionLocationEntry.COLUMN_NAME_LOC_CODE +"= ?", new String[]{loc_code});
		
		if (cursor.moveToFirst()) {
			temp_object.setLocation_name(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LOCATION_NAME)));
			temp_object.setLatitude(cursor.getDouble(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LATITUDE)));
			temp_object.setLongitude(cursor.getDouble(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_LONGITUDE)));
			temp_object.setLoc_code(loc_code);
			temp_object.setRoadway_portion(cursor.getString(cursor.getColumnIndex(CollisionLocationEntry.COLUMN_NAME_ROADWAY_PORTION)));
		}
		cursor.close();
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
			db.beginTransaction();
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
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
	}
	
	public void insertSchoolZones(ArrayList<SchoolZoneObject> arrayList){
		
		SQLiteDatabase db=this.getWritableDatabase();
		if (!arrayList.isEmpty()) {
			db.delete(SchoolZoneEntry.TABLE_NAME, null, null);
			db.beginTransaction();
			for (int i = 0; i < arrayList.size(); i++) {
				ContentValues temp_contentValues=new ContentValues();
				SchoolZoneObject temp_schoolZoneObject=arrayList.get(i);
				
				temp_contentValues.put(SchoolZoneEntry.COLUMN_ID, temp_schoolZoneObject.getId());
				temp_contentValues.put(SchoolZoneEntry.COLUMN_SCHOOL_TYPE, temp_schoolZoneObject.getSchool_type());
				temp_contentValues.put(SchoolZoneEntry.COLUMN_SCHOOL_NAME, temp_schoolZoneObject.getSchool_name());
				temp_contentValues.put(SchoolZoneEntry.COLUMN_ADDRESS, temp_schoolZoneObject.getAddress());
				temp_contentValues.put(SchoolZoneEntry.COLUMN_LONGITUDE, temp_schoolZoneObject.getLongitude());
				temp_contentValues.put(SchoolZoneEntry.COLUMN_LATITUDE, temp_schoolZoneObject.getLatitude());
				temp_contentValues.put(SchoolZoneEntry.COLUMN_SZ_SEFMENTS, temp_schoolZoneObject.getSz_segments());
				temp_contentValues.put(SchoolZoneEntry.COLUMN_GRADE_LEVEL, temp_schoolZoneObject.getGrade_level());
				
				db.insert(SchoolZoneEntry.TABLE_NAME, null, temp_contentValues);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
		
	}
	public void insertReasonConditionTableData(ArrayList<WMReasonConditionObject> arrayList){
		SQLiteDatabase db=this.getWritableDatabase();
		
		if (!arrayList.isEmpty()) {
			db.delete(ReasonConditionEntry.TABLE_NAME, null, null);
			db.beginTransaction();
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
				temp_contentValues.put(ReasonConditionEntry.COLUMN_CATEGORY, temp_object.getCategory());
				db.insert(ReasonConditionEntry.TABLE_NAME, null, temp_contentValues);	
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
			Log.v("life", "reason condition size:"+arrayList.size());
		}
		
	}
	
	public void insertDayTypeTableData(ArrayList<DayTypeObject> arrayList){
		SQLiteDatabase db=this.getWritableDatabase();
		
		if (!arrayList.isEmpty()) {
			db.delete(DayTypeEntry.TABLE_NAME, null, null);
			db.beginTransaction();
			for (int i = 0; i < arrayList.size(); i++) {
				ContentValues temp_conValues=new ContentValues();
				DayTypeObject temp_object=arrayList.get(i);
				
				temp_conValues.put(DayTypeEntry.COLUMN_WEEKDAY, temp_object.getWeekday()?1:0);
				temp_conValues.put(DayTypeEntry.COLUMN_WEEKEND, temp_object.getWeekend()?1:0);
				temp_conValues.put(DayTypeEntry.COLUMN_DATE, temp_object.getDate());
				temp_conValues.put(DayTypeEntry.COLUMN_SCHOLL_DAY, temp_object.getSchool_day()?1:0);
				
				db.insert(DayTypeEntry.TABLE_NAME, null, temp_conValues);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
			
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
