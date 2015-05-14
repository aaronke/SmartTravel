package com.aaron.smarttravel.database;

import android.provider.BaseColumns;

public final class CollisionLocationTable {
	
	public CollisionLocationTable(){}
	
	public static abstract class CollisionLocationEntry implements BaseColumns{
		public static final String TABLE_NAME="TBL_COLLISION_LOCATION";
		public static final String COLUMN_NAME_LOCATION_NAME="location_name";
		public static final String COLUMN_NAME_ROADWAY_PORTION="roadway_portion";
		public static final String COLUMN_NAME_LONGITUDE="longitude";
		public static final String COLUMN_NAME_LATITUDE="latitude";
		public static final String COLUMN_NAME_LOC_CODE="loc_code";
	}

}
