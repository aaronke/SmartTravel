package com.aaron.smarttravel.database;

import android.provider.BaseColumns;

public final class SchoolZoneTable {
	public SchoolZoneTable(){}
	
	public static abstract class SchoolZoneEntry implements BaseColumns{
		public static final String TABLE_NAME="TBL_SCHOOL_ZONE";
		public static final String COLUMN_ID="id";
		public static final String COLUMN_SCHOOL_TYPE="school_type";
		public static final String COLUMN_SCHOOL_NAME="school_name";
		public static final String COLUMN_ADDRESS="address";
		public static final String COLUMN_LONGITUDE="longitude";
		public static final String COLUMN_LATITUDE="latitude";
		public static final String COLUMN_SZ_SEFMENTS="sz_segments";
		public static final String COLUMN_GRADE_LEVEL="grade_level";
		
	}
}
