package com.aaron.smarttravel.database;

import android.provider.BaseColumns;

public final class DataTypeTable {
	
	public DataTypeTable(){}
	
	public static abstract class DayTypeEntry implements BaseColumns{
		
		public static final String TABLE_NAME="TBL_WM_DATATYPE";
		public static final String COLUMN_WEEKDAY="weekday";
		public static final String COLUMN_WEEKEND="weekend";
		public static final String COLUMN_DATE="date";
		public static final String COLUMN_SCHOLL_DAY="school_day";
	}

}
