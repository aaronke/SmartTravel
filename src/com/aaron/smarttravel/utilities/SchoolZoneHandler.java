package com.aaron.smarttravel.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;

import com.aaron.smarttravel.data.DataParseFromJson;
import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.aaron.smarttravel.main.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class SchoolZoneHandler {
	
	private ArrayList<Marker> school_markers=new ArrayList<Marker>();
	private ArrayList<MarkerOptions> schoolzone_markerOptions=new ArrayList<MarkerOptions>();
	private GoogleMap googleMap;
	private ArrayList<SchoolZoneObject> schoolZoneObjects=new ArrayList<SchoolZoneObject>();
	private ArrayList<PolylineOptions> school_segemeents_polylineOptions=new ArrayList<PolylineOptions>();
	private ArrayList<Polyline> school_sgements_Polylines=new ArrayList<Polyline>();
	private DataParseFromJson dataParseFromJson;
	
	public SchoolZoneHandler(GoogleMap map,Context context){
		googleMap=map;
		
		intialization(context);
		setupSchoolMarkers();
		setupSchoolZonePolylines();
	}
	
	private void intialization(Context context){
		HotspotsDbHelper hotspotsDbHelper=new HotspotsDbHelper(context);
		schoolZoneObjects=hotspotsDbHelper.getSchoolZoneObjects();
		dataParseFromJson=new DataParseFromJson();
	}
	private void setupSchoolMarkers(){
		
		for (int i = 0; i < schoolZoneObjects.size(); i++) {
			LatLng latLng=new LatLng(schoolZoneObjects.get(i).getLatitude(), schoolZoneObjects.get(i).getLongitude());
			
			MarkerOptions options= new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.school))
					.draggable(false).title(schoolZoneObjects.get(i).getSchool_name());
			
			schoolzone_markerOptions.add(options);
		}
	}
	
	public void addSchoolZoneMarkers(){
		
		for (int i = 0; i < schoolzone_markerOptions.size(); i++) {
			Marker marker=googleMap.addMarker(schoolzone_markerOptions.get(i));
			
			school_markers.add(marker);
		}
	}
	
	public void removeSchoolZoneMarkers(){
		for (int i = 0; i < school_markers.size(); i++) {
			school_markers.get(i).remove();
		}
	}
	private void setupSchoolZonePolylines(){
		for (int i = 0; i < schoolZoneObjects.size(); i++) {
			HashMap<Integer, ArrayList<LatLng>> temp_hashMap=new HashMap<Integer,ArrayList<LatLng>>();
			temp_hashMap=dataParseFromJson.getSchoolZoneSegmentsLatLngs(schoolZoneObjects.get(i).getSz_segments());
			for (int j = 0; j < temp_hashMap.size(); j++) {
				PolylineOptions temp_polyOptions=new PolylineOptions().addAll(temp_hashMap.get(j)).color(Color.RED);
				school_segemeents_polylineOptions.add(temp_polyOptions);
			}
		}
	}
	public void addSchoolZoneSegments(){
		for (int i = 0; i < school_segemeents_polylineOptions.size(); i++) {
			Polyline temp_Polyline=googleMap.addPolyline(school_segemeents_polylineOptions.get(i));
			
			school_sgements_Polylines.add(temp_Polyline);
		}
	}
	
	public void removeSchoolZoneSegments(){
		for (int i = 0; i < school_sgements_Polylines.size(); i++) {
			school_sgements_Polylines.get(i).remove();
		}
	}
}
