package com.aaron.smarttravel.database;

import android.provider.BaseColumns;

public final class LocationReasonTable {
	
	public LocationReasonTable(){}
	
	public static abstract class LocationReasonEntry implements BaseColumns{
		public static final String TABLE_NAME="TBL_LOCATION_REASON";
		public static final String COLUMN_TOTAL="total";
		public static final String COLUMN_WARNING_PRIORITY="warning_priority";
		public static final String COLUMN_REASON_ID="reason_id";
		public static final String COLUMN_TRAVEL_DIRECTION="travel_direction";
		public static final String COLUMN_LOC_CODE="loc_code";
	}

}
