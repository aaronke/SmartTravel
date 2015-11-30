package com.aaron.smarttravel.database;

import android.provider.BaseColumns;

public final class ReasonConditionTable {
	
	public ReasonConditionTable(){}
	
	public static abstract class ReasonConditionEntry implements BaseColumns{
		
		public static final String TABLE_NAME="TBL_WM_REASON_CONDITION";
		public static final String COLUMN_WARNING_MESSAGE="warning_message";
		public static final String COLUMN_WEEKDAY="weekday";
		public static final String COLUMN_REASON="reason";
		public static final String COLUMN_WEEKEND="weekend";
		public static final String COLUMN_END_TIME="end_time";
		public static final String COLUMN_REASON_ID="reason_id";
		public static final String COLUMN_MONTH="month";
		public static final String COLUMN_START_TIME="start_time";
		public static final String COLUMN_SCHOLL_DAY="school_day";
		public static final String COLUMN_CATEGORY="category";
		
	}

}
