package com.aaron.smarttravel.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import com.aaron.smarttravel.utilities.CollisionLocationObject;
import com.aaron.smarttravel.utilities.DayTypeObject;
import com.aaron.smarttravel.utilities.LocationReasonObject;
import com.aaron.smarttravel.utilities.SchoolZoneObject;
import com.aaron.smarttravel.utilities.WMReasonConditionObject;
import com.google.android.gms.maps.model.LatLng;


public class DataParseFromJson {
	
	public ArrayList<CollisionLocationObject> getCollisionLocationObjects(String jsonString){
		ArrayList<CollisionLocationObject> objects_arrayList=new ArrayList<CollisionLocationObject>();
		
		try {
			
			JSONArray array=new JSONArray(jsonString);
			
			Log.v("STTest", array.length()+"Orinal");
			for (int i = 0; i < array.length(); i++) {
				
				CollisionLocationObject temp_CollisionLocationObject=new CollisionLocationObject();
				JSONObject temp_jsonObject=array.getJSONObject(i);
				
				if (temp_jsonObject.has("roadway_portion")) {
					temp_CollisionLocationObject.setLocation_name(temp_jsonObject.getString("location_name"));
					temp_CollisionLocationObject.setLatitude(temp_jsonObject.getDouble("latitude"));
					temp_CollisionLocationObject.setLongitude(temp_jsonObject.getDouble("longitude"));
					temp_CollisionLocationObject.setLoc_code(temp_jsonObject.getString("loc_code"));
					temp_CollisionLocationObject.setRoadway_portion(temp_jsonObject.getString("roadway_portion"));

					objects_arrayList.add(temp_CollisionLocationObject);
				}
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return objects_arrayList;
	}
	
	
	public ArrayList<WMReasonConditionObject> getReasonConditionObjects(String jsonString){
		
		ArrayList<WMReasonConditionObject> objects_ArrayList=new ArrayList<WMReasonConditionObject>();
		
		try {
			
			JSONArray array=new JSONArray(jsonString);
			
			Log.v("STTest", "ReasonConditionList:"+array.length()+"");
			
			for (int i = 0; i < array.length(); i++) {
				WMReasonConditionObject temp_wmReasonConditionObject=new WMReasonConditionObject();
				JSONObject temp_JsonObject=array.getJSONObject(i);
				
				temp_wmReasonConditionObject.setWarning_message(temp_JsonObject.getString("warning_message"));
				temp_wmReasonConditionObject.setWeekday(temp_JsonObject.getString("weekday").startsWith("TRUE")? true:false);
				temp_wmReasonConditionObject.setReason(temp_JsonObject.getString("reason"));
				temp_wmReasonConditionObject.setWeekend(temp_JsonObject.getString("weekend").startsWith("TRUE")? true: false);
				temp_wmReasonConditionObject.setEnd_time(temp_JsonObject.getString("end_time"));
				temp_wmReasonConditionObject.setReason_id(temp_JsonObject.getInt("reason_id"));
				temp_wmReasonConditionObject.setMonth(temp_JsonObject.getString("month"));
				temp_wmReasonConditionObject.setStart_time(temp_JsonObject.getString("start_time"));
				temp_wmReasonConditionObject.setScholl_day(temp_JsonObject.getString("school_day").startsWith("TRUE")? true: false);
				temp_wmReasonConditionObject.setCategory(temp_JsonObject.getString("category"));
				objects_ArrayList.add(temp_wmReasonConditionObject);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return objects_ArrayList;
	}
	
	
	public ArrayList<LocationReasonObject> getLocationReasonObjects(String jsonString){
		ArrayList<LocationReasonObject> objects_ArrayList=new ArrayList<LocationReasonObject>();
			
		try {
			
			JSONArray array=new JSONArray(jsonString);
			
			Log.v("STTest", array.length()+"");
			for (int i = 0; i < array.length(); i++) {
				LocationReasonObject temp_locationReasonObject=new LocationReasonObject();
				JSONObject temp_jsonObject=array.getJSONObject(i);
				
				temp_locationReasonObject.setTotal(temp_jsonObject.getInt("total"));
				temp_locationReasonObject.setWarning_priority(temp_jsonObject.getInt("warning_priority"));
				temp_locationReasonObject.setReason_id(temp_jsonObject.getInt("reason_id"));
				if (temp_jsonObject.has("travel_direction") ) {
					temp_locationReasonObject.setTravel_direction(temp_jsonObject.getString("travel_direction"));
				}else {
					temp_locationReasonObject.setTravel_direction("unknown");
				}
				
				temp_locationReasonObject.setLoc_code(temp_jsonObject.getString("loc_code"));
				
				objects_ArrayList.add(temp_locationReasonObject);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("STTest", "problems");
		}
		Log.v("STTest", objects_ArrayList.size()+"");
		return objects_ArrayList;
	}
	
	
	public ArrayList<DayTypeObject> getDataTypeObjects(String jsonString){
		ArrayList<DayTypeObject> object_ArrayList=new ArrayList<DayTypeObject>();
		try {
			
			JSONArray array=new JSONArray(jsonString);
			
			Log.v("STTest", array.length()+"");
			for (int i = 0; i < array.length(); i++) {
				DayTypeObject temp_DayTypeObject=new DayTypeObject();
				JSONObject temp_jsonObject=array.getJSONObject(i);
				
				temp_DayTypeObject.setWeekday(temp_jsonObject.getString("weekday").startsWith("TRUE")? true: false);
				temp_DayTypeObject.setWeekend(temp_jsonObject.getString("weekend").startsWith("TRUE")? true: false);
				temp_DayTypeObject.setDate(temp_jsonObject.getString("date"));
				temp_DayTypeObject.setSchool_day(temp_jsonObject.getString("school_day").startsWith("TRUE")? true: false);
				
				object_ArrayList.add(temp_DayTypeObject);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
			
		return object_ArrayList;
	}
	
	public ArrayList<SchoolZoneObject> getSchoolZoneObjects(String jsonString){
		ArrayList<SchoolZoneObject> object_ArrayList=new ArrayList<SchoolZoneObject>();
		
		try {
			JSONArray jsonArray=new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				SchoolZoneObject schoolZoneObject=new SchoolZoneObject();
				JSONObject temp_jsonObject=jsonArray.getJSONObject(i);
				schoolZoneObject.setId(temp_jsonObject.getInt("id"));
				schoolZoneObject.setSchool_name(temp_jsonObject.getString("school_name"));
				schoolZoneObject.setSchool_type(temp_jsonObject.getString("school_type"));
				schoolZoneObject.setAddress(temp_jsonObject.getString("address"));
				schoolZoneObject.setLongitude(temp_jsonObject.getDouble("longitude"));
				schoolZoneObject.setLatitude(temp_jsonObject.getDouble("latitude"));
				schoolZoneObject.setSz_segments(temp_jsonObject.getString("sz_segments"));
				schoolZoneObject.setGrade_level(temp_jsonObject.getString("grade_level"));
				
				object_ArrayList.add(schoolZoneObject);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return object_ArrayList;
	}
	
	public HashMap<Integer, ArrayList<LatLng>>getSchoolZoneSegmentsLatLngs(String jsonString){
		
		HashMap<Integer, ArrayList<LatLng>> hashMap=new HashMap<Integer,ArrayList<LatLng>>();
		try {
			JSONArray segmentsJsonArray=new JSONArray(jsonString);
			for (int i = 0; i < segmentsJsonArray.length(); i++) {
				JSONArray temp_jsonaJsonArray=new JSONArray(segmentsJsonArray.get(i).toString());
				ArrayList<LatLng> temp_latlngArrayList=new ArrayList<LatLng>();
				for (int j = 0; j < temp_jsonaJsonArray.length(); j+=2) {
					LatLng temp_LatLng=new LatLng(temp_jsonaJsonArray.getDouble(j+1), temp_jsonaJsonArray.getDouble(j));
					
					temp_latlngArrayList.add(temp_LatLng);
					
				}
				hashMap.put(i, temp_latlngArrayList);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		 return hashMap;
		
	}
	public String getNewVersionString(String jsonString){
		String versionString=null;
			try {
				JSONObject temp_jsonObject=new JSONObject(jsonString);
				versionString=temp_jsonObject.getString("version");
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		return versionString;
	}
	
	
	public String loadJsonString(String filename,Context context){
			String json=null;
			
			try {
				InputStream inputStream=context.getAssets().open(filename);
				int size=inputStream.available();
				byte[] buffer=new byte[size];
				inputStream.read(buffer);
				json=new String(buffer,"UTF-8");
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
			
		return json;
	}
	
}
