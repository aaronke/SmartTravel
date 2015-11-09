package com.aaron.smarttravel.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.maps.model.LatLng;
import android.content.Context;
import android.location.Location;
import android.util.Log;

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
	
	public int getTotalCollisionCount(ArrayList<LocationReasonObject> arrayList){
		int total_collision=0;
		if (!arrayList.isEmpty()) {
			for (int i = 0; i < arrayList.size(); i++) {
				total_collision+=arrayList.get(i).getTotal();
			}
		}
		return total_collision;
	}
	
	public LocationReasonObject getHighestPriorityMatchedReasonObject(ArrayList<LocationReasonObject> arrayList,Location currentLocation, Location destLocation,Context context){
		int temp_index=0,first_priority;
		LocationReasonObject tempLocationReasonObject=new LocationReasonObject();
		if (!arrayList.isEmpty()) {
			first_priority=100;
			
			for (int i = 0; i < arrayList.size(); i++) {
			//	Log.v("STTest", "check date:"+checkReasonConditionDate(arrayList.get(i),context));
			//	Log.v("STTest", "direction:"+checkDirectionCondition(arrayList.get(i), currentLocation,destLocation));
				if (first_priority >= arrayList.get(i).getWarning_priority() && checkReasonConditionDate(arrayList.get(i), context) && checkDirectionCondition(arrayList.get(i), currentLocation,destLocation)) {
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
	public Boolean checkDirectionCondition(LocationReasonObject locationReasonObject,Location currentLocation,Location destLocation){
		Boolean directionBoolean=true;
		Float bearingFloat=currentLocation.bearingTo(destLocation);
		String current_directionString=returnDirection(bearingFloat);
		String running_bearingString=returnDirection(currentLocation.getBearing());
		
		Log.v("STTest", "current direction:"+current_directionString+"running direction:"+running_bearingString+"Travel Direction:"+locationReasonObject.getTravel_direction());
			if (!locationReasonObject.getTravel_direction().startsWith("ALL") && !locationReasonObject.getTravel_direction().startsWith("unknown")) {
				if (!locationReasonObject.getTravel_direction().startsWith(running_bearingString) || running_bearingString=="unknow") {

					directionBoolean=false;
					// add logs to Flurry
					Map<String, String> direction_params=new HashMap<String,String>();
					direction_params.put("location_name", locationReasonObject.getLoc_code());
					direction_params.put("reason_id", ""+locationReasonObject.getReason_id());
					direction_params.put("location", ""+currentLocation.getLongitude()+"-"+currentLocation.getLatitude());
					direction_params.put("direction", locationReasonObject.getTravel_direction());
					direction_params.put("current_direction", current_directionString);
					FlurryAgent.logEvent("direction_filter", direction_params);
				}
			}
		
		return directionBoolean;
	}
	
	private String returnDirection(float bearingFloat){
		String current_directionString="unknow";
		if (bearingFloat!=0.0) {
			if (bearingFloat<45||bearingFloat >315) {
				current_directionString="NORTH";
			}else if (bearingFloat>45 && bearingFloat<135) {
				current_directionString="EAST";
			}else if (bearingFloat >135 && bearingFloat <225) {
				current_directionString="SOUTH";
			}else if (bearingFloat>225 && bearingFloat<315) {
				current_directionString="WEST";
			}
		}
		return current_directionString;
	}
	public Boolean checkReasonConditionDate(LocationReasonObject locationReasonObject, Context context){
		Boolean checkBoolean=false;
		HotspotsDbHelper dbHelper=new HotspotsDbHelper(context);
		WMReasonConditionObject reasonConditionObject=dbHelper.getWMReasonConditionByReasonID(locationReasonObject.getReason_id());
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat format_day_of_year=new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
		String day_of_year=format_day_of_year.format(calendar.getTime());
		DayTypeObject temp_DayTypeObject=dbHelper.getDayTypeObjectByDay(day_of_year);
		
		if (temp_DayTypeObject.getWeekday()==reasonConditionObject.getWeekday() || temp_DayTypeObject.getWeekend()==reasonConditionObject.getWeekend()) {
			
			if (reasonConditionObject.getScholl_day()==false || temp_DayTypeObject.getSchool_day()==reasonConditionObject.getScholl_day()) {
				String start_time=reasonConditionObject.getStart_time();
				String end_time=reasonConditionObject.getEnd_time();
				SimpleDateFormat time_of_day=new SimpleDateFormat("HH:mm", Locale.CANADA);
				String current_time=time_of_day.format(calendar.getTime());
				try {
					Date startDate=time_of_day.parse(start_time);
					Date endDate=time_of_day.parse(end_time);
					Date currentDate=time_of_day.parse(current_time);
					if (startDate.before(currentDate) && endDate.after(currentDate)) {
						checkBoolean= true;
					}else {
						Map<String, String> date_time_params=new HashMap<String,String>();
						date_time_params.put("location_name", locationReasonObject.getLoc_code());
						date_time_params.put("reason_id", ""+locationReasonObject.getReason_id());
						date_time_params.put("start_time", reasonConditionObject.getStart_time());
						date_time_params.put("end_time", reasonConditionObject.getEnd_time());
						date_time_params.put("current_time",current_time);
						FlurryAgent.logEvent("date_time_filter", date_time_params);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
				
			}
		}
		
		return checkBoolean;
	}
	public ArrayList<BottomInfoItem> getBottomInfoItemByLocaitonName(Context context,String location_name){
		ArrayList<BottomInfoItem> arrayList_items=new ArrayList<BottomInfoItem>();
		HotspotsDbHelper dbHelper=new HotspotsDbHelper(context);
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat format_day_of_year=new SimpleDateFormat("dd-MM-yy", Locale.CANADA);
		String day_of_year=format_day_of_year.format(calendar.getTime());
		DayTypeObject temp_DayTypeObject=dbHelper.getDayTypeObjectByDay(day_of_year);
		
		CollisionLocationObject temp_collisionLocationObject=dbHelper.getcolllicionObjectByName(location_name);
		
		if (temp_collisionLocationObject.getLocation_name()!="unknown") {
			ArrayList<LocationReasonObject> temp_location_list=dbHelper.getLocationReasonByLocCode(
					temp_collisionLocationObject.getLoc_code());
			LatLng temp_latLng=new LatLng(temp_collisionLocationObject.getLatitude(), temp_collisionLocationObject.getLongitude());
			for (int i = 0; i < temp_location_list.size(); i++) {
				LocationReasonObject tempReasonObject= temp_location_list.get(i);
				WMReasonConditionObject tempConditionObject=dbHelper.getWMReasonConditionByReasonID(tempReasonObject.getReason_id());
				BottomInfoItem tempBottomInfoItem=new BottomInfoItem();
				tempBottomInfoItem.setLocation_name(location_name);
				tempBottomInfoItem.setType(temp_collisionLocationObject.getRoadway_portion());
				tempBottomInfoItem.setSchoolDay(temp_DayTypeObject.getSchool_day());
				tempBottomInfoItem.setReason(tempConditionObject.getReason());
				tempBottomInfoItem.setDirection(tempReasonObject.getTravel_direction());
				tempBottomInfoItem.setTotal(tempReasonObject.getTotal());
				tempBottomInfoItem.setLocationLatLng(temp_latLng);
				arrayList_items.add(tempBottomInfoItem);
			}
		}else {
			SchoolZoneObject schoolZoneObject=dbHelper.getSchoolZoneObjectByName(location_name);
			if (schoolZoneObject.getSchool_name()!="unknown") {
				BottomInfoItem tempBottomInfoItem=new BottomInfoItem();
				LatLng latLng=new LatLng(schoolZoneObject.getLatitude(), schoolZoneObject.getLongitude());
				tempBottomInfoItem.setLocation_name(location_name);
				tempBottomInfoItem.setType("SCHOOL ZONE");
				tempBottomInfoItem.setSchoolDay(temp_DayTypeObject.getSchool_day());
				tempBottomInfoItem.setLocationLatLng(latLng);
				arrayList_items.add(tempBottomInfoItem);
			}
		}
		
		
						
		
		
		return arrayList_items;
	}

}
