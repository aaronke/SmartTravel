package com.aaron.smarttravel.utilities;

import java.util.ArrayList;

import com.aaron.smarttravel.database.HotspotsDbHelper;

import android.content.Context;

public class DataHandler {
	
	public LocationReasonObject getHighestPriorityReasonObject(ArrayList<LocationReasonObject> arrayList){
		int temp_index=0,first_priority;
		LocationReasonObject tempLocationReasonObject=new LocationReasonObject();
		if (!arrayList.isEmpty()) {
			first_priority=arrayList.get(0).getWarning_priority();
			for (int i = 0; i < arrayList.size(); i++) {
				
				if (first_priority >arrayList.get(i).getWarning_priority()) {
					first_priority=arrayList.get(i).getWarning_priority();
					temp_index=i;
				}
			}
			
			tempLocationReasonObject=arrayList.get(temp_index);
		}else {
			
		}
		
		
		return tempLocationReasonObject;
		
	}
	public BottomInfoItem getBottomInfoItemByLocaitonName(Context context,String location_name){
		HotspotsDbHelper dbHelper=new HotspotsDbHelper(context);
		
		LocationReasonObject tempReasonObject=getHighestPriorityReasonObject(
				dbHelper.getLocationReasonByLocCode(
						dbHelper.getcolllicionObjectByName(location_name).getLoc_code())
				);
						
		WMReasonConditionObject tempConditionObject=dbHelper.getWMReasonConditionByReasonID(tempReasonObject.getReason_id());
		
		BottomInfoItem tempBottomInfoItem=new BottomInfoItem();
		tempBottomInfoItem.setLocation_name(location_name);
		tempBottomInfoItem.setReason(tempConditionObject.getReason());
		tempBottomInfoItem.setDirection(tempReasonObject.getTravel_direction());
		tempBottomInfoItem.setTotal(tempReasonObject.getTotal());
		
		return tempBottomInfoItem;
	}

}
