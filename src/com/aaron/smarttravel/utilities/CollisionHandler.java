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
	
	public CollisionHandler(GoogleMap map,Context context){
		googleMap=map;
		
		intialization(context);

	}
	
	private void intialization(Context context){
		HotspotsDbHelper hotspotsDbHelper=new HotspotsDbHelper(context);
		setHotSpots( hotspotsDbHelper.getCollisionObjectsByType(INTERSECTION),1);
		setHotSpots( hotspotsDbHelper.getCollisionObjectsByType(MID_AVENUE), 1);
		setHotSpots( hotspotsDbHelper.getCollisionObjectsByType(MID_STREET), 1);
		
	}
	private void setHotSpots(ArrayList<CollisionLocationObject> hotspots_ArrayList,int Type_Flag){
		for (int i = 0; i < hotspots_ArrayList.size(); i++) {
			
			CollisionLocationObject temp_object=hotspots_ArrayList.get(i);
			int icon_res=R.drawable.hotspot_point;
			if (Type_Flag==1) {
				icon_res=R.drawable.hotspot_point;
			}else {
				icon_res=R.drawable.hotspot_vru_point;
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
