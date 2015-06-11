package com.aaron.smarttravel.data;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.aaron.smarttravel.utilities.HotSpotEntry;
import com.google.android.gms.maps.model.LatLng;

public class HotspotParse {
	
		public ArrayList<HotSpotEntry> getHotspotEntries(String jsonString,String typeString){
			ArrayList<HotSpotEntry> hotspot_ArrayList=new ArrayList<HotSpotEntry>();
			
			try {
				JSONObject json_Object=new JSONObject(jsonString);
				JSONArray hotspot_JsonArray=json_Object.getJSONArray("hotSpots");
				for (int i = 0; i < hotspot_JsonArray.length(); i++) {
					HotSpotEntry temp_HotSpot_Entry=new HotSpotEntry();
					JSONObject temp_jsonObject= hotspot_JsonArray.getJSONObject(i);
					
					temp_HotSpot_Entry.setType(typeString);
					temp_HotSpot_Entry.setName(temp_jsonObject.getString("LOCATION"));
					temp_HotSpot_Entry.setCollision_count(temp_jsonObject.getInt("COUNT"));
					temp_HotSpot_Entry.setFatal_count(temp_jsonObject.getInt("FATAL"));
					temp_HotSpot_Entry.setInjury_count(temp_jsonObject.getInt("INJURY"));
					temp_HotSpot_Entry.setPDO_value(temp_jsonObject.getInt("PDO"));
					temp_HotSpot_Entry.setRank(temp_jsonObject.getInt("RANK"));
					temp_HotSpot_Entry.setLatLng(new LatLng(temp_jsonObject.getDouble("LAT"), temp_jsonObject.getDouble("LONG")));
					hotspot_ArrayList.add(temp_HotSpot_Entry);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return hotspot_ArrayList;
	}

	public  String loadJsonString(String filename,Context mycontext){
		String json=null;
		try {
			InputStream iStream=mycontext.getAssets().open(filename); 
			int size=iStream.available();
			byte[] buffer=new byte[size];
			iStream.read(buffer);
			json=new String(buffer,"UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		return json;
		
	}
}
