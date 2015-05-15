package com.aaron.smarttravel.database;

import android.provider.BaseColumns;

public final class NewVersionTable {
	
	public NewVersionTable(){}
	
	public static abstract class NewVersionEntry implements BaseColumns{
		
		public static final String TABLE_NAME="TBL_NEW_VERSION";
		public static final String COLUMN_VERSION="version";
	}

}
