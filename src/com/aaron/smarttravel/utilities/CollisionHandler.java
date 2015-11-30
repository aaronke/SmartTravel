package com.aaron.smarttravel.utilities;

import java.util.ArrayList;

import android.content.Context;
import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.aaron.smarttravel.main.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CollisionHandler {
	
	private static final String INTERSECTION="INTERSECTION";
	private static final String MID_AVENUE="MID AVENUE";
	private static final String MID_STREET="MID STREET";
	private ArrayList<Marker> collision_markers=new ArrayList<Marker>();
	private ArrayList<MarkerOptions> collision_markerOptions=new ArrayList<MarkerOptions>();
	private GoogleMap googleMap;
	private Context context;
	public CollisionHandler(GoogleMap map,Context context){
		googleMap=map;
		this.context=context;
		intialization();

	}
	
	private void intialization(){
		HotspotsDbHelper hotspotsDbHelper=new HotspotsDbHelper(context);
		setHotSpots( hotspotsDbHelper.getCollisionObjectsByType(INTERSECTION),1);
		setHotSpots( hotspotsDbHelper.getCollisionObjectsByType(MID_AVENUE), 1);
		setHotSpots( hotspotsDbHelper.getCollisionObjectsByType(MID_STREET), 1);
		
	}
	private void setHotSpots(ArrayList<CollisionLocationObject> hotspots_ArrayList,int Type_Flag){
		HotspotsDbHelper dbHelper=new HotspotsDbHelper(context);
		for (int i = 0; i < hotspots_ArrayList.size(); i++) {
			
			CollisionLocationObject temp_object=hotspots_ArrayList.get(i);
			
			LocationReasonObject tempLocationReasonObject=new LocationReasonObject();
					if (dbHelper.getLocationReasonByLocCode(temp_object.getLoc_code())!=null&&
						dbHelper.getLocationReasonByLocCode(temp_object.getLoc_code()).size()!=0) {
						tempLocationReasonObject=dbHelper.getLocationReasonByLocCode(temp_object.getLoc_code()).get(0);
					}
			
			
			int icon_res=R.drawable.hotspot_point;
			int reasonID=tempLocationReasonObject.getReason_id();
			if (reasonID==9||reasonID==10) {
				icon_res=R.drawable.map_cyclist;
			}else if(reasonID==11||reasonID==12){
				icon_res=R.drawable.map_motorcyclist;
			}else if(reasonID==7||reasonID==8){
				icon_res=R.drawable.map_pedstrain;
			}
			LatLng tempLatLng=new LatLng(temp_object.getLatitude(), temp_object.getLongitude());
			MarkerOptions markerOptions=new MarkerOptions().position(tempLatLng).icon(BitmapDescriptorFactory.fromResource(icon_res)).draggable(false);
			markerOptions.title(temp_object.getLocation_name());
			collision_markerOptions.add(markerOptions);
		}
	}
	
	public void addCollisionLayer(){
		for (int i = 0; i < collision_markerOptions.size(); i++) {
			Marker marker=googleMap.addMarker(collision_markerOptions.get(i));
			collision_markers.add(marker);
		}
	}
	public void removeCollisionLayer(){
		for (int i = 0; i < collision_markers.size(); i++) {
			collision_markers.get(i).remove();
		}
	}
}
