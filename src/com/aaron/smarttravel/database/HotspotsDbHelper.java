package com.aaron.smarttravel.database;

import com.aaron.smarttravel.database.CollisionLocationTable.CollisionLocationEntry;
import com.aaron.smarttravel.database.DataTypeTable.DataTypeEntry;
import com.aaron.smarttravel.database.LocationReasonTable.LocationReasonEntry;
import com.aaron.smarttravel.database.NewVersionTable.NewVersionEntry;
import com.aaron.smarttravel.database.ReasonConditionTable.ReasonConditionEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
					CollisionLocationEntry.COLUMN_NAME_LATITUDE+REAL_TYPE+COMMA_SEP
					+" )";
	private static final String SQL_DELETE_COLLISION_LOCATION_TABLE="DROP TABLE IF EXISTS "+ CollisionLocationEntry.TABLE_NAME;
	
	private static final String SQL_CREATE_DATATYPE_TABLE="CREATE TABLE "+DataTypeEntry.TABLE_NAME +" ("+
			DataTypeEntry._ID +" INTEGER PRIMARY KEY, "+
			DataTypeEntry.COLUMN_WEEKDAY+INTEGER_TYPE+COMMA_SEP+
			DataTypeEntry.COLUMN_WEEKEND+INTEGER_TYPE+COMMA_SEP+
			DataTypeEntry.COLUMN_SCHOLL_DAY+INTEGER_TYPE+COMMA_SEP+
			DataTypeEntry.COLUMN_DATE+TEXT_TYPE+COMMA_SEP
			+" )";
	private static final String SQL_DELETE_DATATYPE_TABLE="DROP TABLE IF EXISTS "+DataTypeEntry.TABLE_NAME;
	
	
	private static final String SQL_CREATE_LOCATION_REASON_TABLE=
			"CREATE TABLE "+ LocationReasonEntry.TABLE_NAME+" (" +LocationReasonEntry._ID +" INTEGER PRIMARY KEY, "+
					LocationReasonEntry.COLUMN_TOTAL+INTEGER_TYPE+COMMA_SEP+
					LocationReasonEntry.COLUMN_WARNING_PRIORITY+INTEGER_TYPE+COMMA_SEP+
					LocationReasonEntry.COLUMN_REASON_ID+INTEGER_TYPE+COMMA_SEP+
					LocationReasonEntry.COLUMN_TRAVEL_DIRECTION+TEXT_TYPE+COMMA_SEP+
					LocationReasonEntry.COLUMN_LOC_CODE+TEXT_TYPE+COMMA_SEP
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
					ReasonConditionEntry.COLUMN_SCHOLL_DAY+INTEGER_TYPE+COMMA_SEP
					+" )";
	private static final String SQL_DELETE_REASON_CONDITION_TABLE="DROP IF EXISTS "+ ReasonConditionEntry.TABLE_NAME;
	
	
	private static final String SQL_CREATE_NEW_VERSION_TABLE=
			"CREATE TABLE "+ NewVersionEntry.TABLE_NAME+" ("+ NewVersionEntry._ID+" INTEGER PRIMARY KEY, "+
					NewVersionEntry.COLUMN_VERSION+TEXT_TYPE+COMMA_SEP
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

}
