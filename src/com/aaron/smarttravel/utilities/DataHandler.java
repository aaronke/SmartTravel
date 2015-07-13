package com.aaron.smarttravel.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Location;

public class DataHandler {
	
	public LocationReasonObject getHighestPriorityReasonObject(ArrayList<LocationReasonObject> arrayList){
		int temp_index=0,first_priority;
		LocationReasonObject tempLocationReasonObject=new LocationReasonObject();
		if (!arrayList.isEmpty()) {
			first_priority=100;
			for (int i = 0; i < arrayList.size(); i++) {
				if (first_priority >= arrayList.get(i).getWarning_priority()) {
					first_priority=arrayList.get(i).getWarning_priority();
					temp_index=i;
				}
			}
				tempLocationReasonObject=arrayList.get(temp_index);
		}else {
		}
		return tempLocationReasonObject;
	}
	
	public LocationReasonObject getHighestPriorityMatchedReasonObject(ArrayList<LocationReasonObject> arrayList,Location currentLocation,Context context){
		int temp_index=0,first_priority;
		LocationReasonObject tempLocationReasonObject=new LocationReasonObject();
		if (!arrayList.isEmpty()) {
			first_priority=100;
			for (int i = 0; i < arrayList.size(); i++) {
				
				if (first_priority >= arrayList.get(i).getWarning_priority() && checkReasonConditionDate(arrayList.get(i), context) && checkDirectionCondition(arrayList.get(i), currentLocation)) {
					first_priority=arrayList.get(i).getWarning_priority();
					temp_index=i;
				}
			}
			if (first_priority!=100) {
				tempLocationReasonObject=arrayList.get(temp_index);
			}
		}else {
		}
		return tempLocationReasonObject;
	}
	public Boolean checkDirectionCondition(LocationReasonObject locationReasonObject,Location currentLocation){
		Boolean directionBoolean=true;
		Float bearingFloat=currentLocation.getBearing();
		String current_directionString="NORTH";
		if (bearingFloat<45||bearingFloat >315) {
			current_directionString="NORTH";
		}else if (bearingFloat>45 && bearingFloat<135) {
			current_directionString="EAST";
		}else if (bearingFloat >135 && bearingFloat <225) {
			current_directionString="SOUTH";
		}else if (bearingFloat>225 && bearingFloat<315) {
			current_directionString="WEST";
		}
		if (locationReasonObject.getTravel_direction()!="ALL" && locationReasonObject.getTravel_direction()!="unknown") {
			if (locationReasonObject.getTravel_direction()!=current_directionString) {
				directionBoolean=false;
			}
		}
		
		return directionBoolean;
	}
	public Boolean checkReasonConditionDate(LocationReasonObject locationReasonObject, Context context){
		
		HotspotsDbHelper dbHelper=new HotspotsDbHelper(context);
		WMReasonConditionObject reasonConditionObject=dbHelper.getWMReasonConditionByReasonID(locationReasonObject.getReason_id());
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat format_day_of_year=new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
		String day_of_year=format_day_of_year.format(calendar.getTime());
		DayTypeObject temp_DayTypeObject=dbHelper.getDayTypeObjectByDay(day_of_year);
		
		if (temp_DayTypeObject.getWeekday()==reasonConditionObject.getWeekday() || temp_DayTypeObject.getWeekend()==reasonConditionObject.getWeekend()) {
			
			if (temp_DayTypeObject.getSchool_day()==reasonConditionObject.getScholl_day()) {
				String start_time=reasonConditionObject.getStart_time();
				String end_time=reasonConditionObject.getEnd_time();
				SimpleDateFormat time_of_day=new SimpleDateFormat("HH:mm", Locale.CANADA);
				String current_time=time_of_day.format(calendar.getTime());
				try {
					Date startDate=time_of_day.parse(start_time);
					Date endDate=time_of_day.parse(end_time);
					Date currentDate=time_of_day.parse(current_time);
					if (startDate.before(currentDate) && endDate.after(currentDate)) {
						return true;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		return false;
	}
	public ArrayList<BottomInfoItem> getBottomInfoItemByLocaitonName(Context context,String location_name){
		ArrayList<BottomInfoItem> arrayList_items=new ArrayList<BottomInfoItem>();
		HotspotsDbHelper dbHelper=new HotspotsDbHelper(context);
		CollisionLocationObject temp_collisionLocationObject=dbHelper.getcolllicionObjectByName(location_name);
		ArrayList<LocationReasonObject> temp_location_list=dbHelper.getLocationReasonByLocCode(
				temp_collisionLocationObject.getLoc_code());
		LatLng temp_latLng=new LatLng(temp_collisionLocationObject.getLatitude(), temp_collisionLocationObject.getLongitude());
		for (int i = 0; i < temp_location_list.size(); i++) {
			LocationReasonObject tempReasonObject= temp_location_list.get(i);
			WMReasonConditionObject tempConditionObject=dbHelper.getWMReasonConditionByReasonID(tempReasonObject.getReason_id());
			BottomInfoItem tempBottomInfoItem=new BottomInfoItem();
			tempBottomInfoItem.setLocation_name(location_name);
			tempBottomInfoItem.setReason(tempConditionObject.getReason());
			tempBottomInfoItem.setDirection(tempReasonObject.getTravel_direction());
			tempBottomInfoItem.setTotal(tempReasonObject.getTotal());
			tempBottomInfoItem.setLocationLatLng(temp_latLng);
			arrayList_items.add(tempBottomInfoItem);
		}
		
						
		
		
		return arrayList_items;
	}

}
