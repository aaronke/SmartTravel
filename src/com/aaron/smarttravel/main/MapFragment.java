package com.aaron.smarttravel.main;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.aaron.smarttravel.data.HotspotParse;
import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.aaron.smarttravel.utilities.CollisionLocationObject;
import com.aaron.smarttravel.utilities.HotSpotEntry;
import com.aaron.smarttravel.utilities.LocationReasonObject;
import com.aaron.smarttravel.utilities.TopInfoEntry;
import com.aaron.smarttravel.utilities.WMReasonConditionObject;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressWarnings("deprecation")
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener{

	MapView mapView;
	private GoogleMap googleMap;
	public Context context;
	private ArrayList<HotSpotEntry> hotspots_arraylist= new ArrayList<HotSpotEntry>();
	//private HotSpotEntry near_HotSpotEntry=new HotSpotEntry();
	public static TextToSpeech textToSpeech;
	ArrayList<HotSpotEntry> intersection_arraylist, midblock_arraylist,VRU_arraylist;
	private TextView name_TextView,reason_TextView,distance_TextView;
	public SlidingDrawer slidingDrawer;
	private SharedPreferences sharedPreferences_settings;
	LocationManager locationManager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		View view=inflater.inflate(R.layout.main_map, container, false);
		mapView=(MapView) view.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		
		name_TextView=(TextView)view.findViewById(R.id.info_box_hotspot_name);
		reason_TextView=(TextView)view.findViewById(R.id.info_box_reason);
		distance_TextView=(TextView)view.findViewById(R.id.info_box_distance);
		slidingDrawer=(SlidingDrawer)view.findViewById(R.id.SlidingDrawer);
		//mapView.onResume();
		
		/*
		View location_buttonView=((View)mapView.findViewById(1).getParent()).findViewById(2);
		
		location_buttonView.setBackgroundResource(R.drawable.location_icon);
		
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(54, 66);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
		params.setMargins(80, 0, 0, 80);
		location_buttonView.setLayoutParams(params);*/
		
		
		try {
			MapsInitializer.initialize(context);
		} catch (Exception e) {
			// TODO: handle exception
		}
		googleMap=mapView.getMap();
		CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(new LatLng(53.539150,  -113.496867), 12);
		googleMap.animateCamera(cameraUpdate);
		googleMap.setMyLocationEnabled(true);
	//	googleMap.setTrafficEnabled(true);
		googleMap.getUiSettings().setZoomControlsEnabled(true);
		
		//getActivity();
		locationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		sharedPreferences_settings=context.getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		Location my_location=locationManager.getLastKnownLocation(getBestProvider());
		loadHotSpotsData();
		if (my_location!=null) {	
			
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(my_location.getLatitude(), my_location.getLongitude()), 12));
			/*if (!hotspots_arraylist.isEmpty()) {
				approaching_hotspot_alert(hotspots_arraylist, my_location);
			}*/
			checkForLocationForWarning(my_location);
			onLocationChanged(my_location);
		}
		
		locationManager.requestLocationUpdates(getBestProvider(), 5000, 0, this);
		
		mapView.getMapAsync(this);
		
		return view;
	}
	
	public String getBestProvider(){
		Criteria criteria=new Criteria();
		String best_provider=locationManager.getBestProvider(criteria, true);;
		
		if (!sharedPreferences_settings.getBoolean(getString(R.string.preferences_setting_gps), true)) {
			best_provider=LocationManager.NETWORK_PROVIDER;
		}
		return best_provider;
	}
	
	private void updateInfoBox(TopInfoEntry topInfoEntry){
		if (!slidingDrawer.isOpened()) {
			slidingDrawer.open();
			slidingDrawer.setVisibility(View.VISIBLE);
		}
		
		name_TextView.setText(topInfoEntry.getLocation_name());
		reason_TextView.setText(topInfoEntry.getHotspot_reason());
		distance_TextView.setText(topInfoEntry.getDistance()+"");
		
	}
	

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		context=activity;
	}
	public void setHotSpots(GoogleMap mymap,ArrayList<HotSpotEntry> hotspots_ArrayList,int ICON_FLAG){
		
		for (int i = 0; i < hotspots_ArrayList.size(); i++) {
			LatLng temp_location=hotspots_ArrayList.get(i).getLatLng();
			int icon_res=R.drawable.hotspot_point;
			if (ICON_FLAG==2) {
				icon_res=R.drawable.hotspot_vru_point;
			}else {
				icon_res=R.drawable.hotspot_point;
			}
			MarkerOptions markerOptions=new MarkerOptions().position(temp_location).icon(BitmapDescriptorFactory.fromResource(icon_res)).draggable(false);
			markerOptions.title(hotspots_ArrayList.get(i).getName());
			markerOptions.snippet("Collision Count:"+hotspots_ArrayList.get(i).getCollision_count()+",Rank:"+hotspots_ArrayList.get(i).getRank());
			mymap.addMarker(markerOptions);		
		}
		
	}
	
	
	// add Geofences to geofencelist
	/*public void addGeofences(ArrayList<HotSpotEntry> hotspots_arraylist){
		
		for (int i = 0; i < hotspots_arraylist.size(); i++) {
			Geofence temp_geofence=new Geofence.Builder()
			.setRequestId(hotspots_arraylist.get(i).getName())
			.setCircularRegion(hotspots_arraylist.get(i).getLatLng().latitude, hotspots_arraylist.get(i).getLatLng().longitude, 500)
			.setExpirationDuration(Geofence.NEVER_EXPIRE)
			.setLoiteringDelay(300)
			.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER|Geofence.GEOFENCE_TRANSITION_EXIT).build();
			
			my_geofence_arraylist.add(temp_geofence);
		}
		
		Geofence temp_geofence=new Geofence.Builder()
		.setRequestId("test")
		.setCircularRegion(53.523451, -113.510887, 1500)
		.setExpirationDuration(Geofence.NEVER_EXPIRE)
		.setLoiteringDelay(300)
		.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER|Geofence.GEOFENCE_TRANSITION_DWELL|Geofence.GEOFENCE_TRANSITION_EXIT).build();
		
		my_geofence_arraylist.add(temp_geofence);
	
		
	}*/

    
    
    public void loadHotSpotsData(){
    	HotspotParse my_HotspotParse=new HotspotParse();
		intersection_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("intersection_top_10.json", context),"Intersection");
		midblock_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("midblock_top_10.json", context),"Midblock");
		VRU_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("VRU_top_x.json", context),"VRU");
		
		hotspots_arraylist.addAll(intersection_arraylist);
		hotspots_arraylist.addAll(midblock_arraylist);
		hotspots_arraylist.addAll(VRU_arraylist);
		
    }
    
	@Override
	public void onMapReady(GoogleMap map) {
		// TODO Auto-generated method stub
		
		// set markers;
		if (!hotspots_arraylist.isEmpty()) {
			setHotSpots(map,intersection_arraylist,1);
			setHotSpots(map, midblock_arraylist,1);
			setHotSpots(map, VRU_arraylist ,2);
		}		
		// set geofences;
		// addGeofences(hotspots_arraylist);
		
		/*if (near_HotSpotEntry.getName()=="UnKnown") {
			Log.v("Geofence", "unknown");
		}else {
			Log.v("Geofence", "clear something");
			near_HotSpotEntry=new HotSpotEntry();
		}*/
		
	}
	
	/*public void approaching_hotspot_alert(ArrayList<HotSpotEntry> hotspots_arraylist,Location current_locaiton){
		HotSpotEntry lan_chengEntry=new HotSpotEntry();
		lan_chengEntry.setLatLng(new LatLng(31.146008, 121.513420));
		lan_chengEntry.setName("LanCheng's Home");
		lan_chengEntry.setCollision_count(12);
		lan_chengEntry.setRank(24);
		lan_chengEntry.setType("Intersection");
		hotspots_arraylist.add(lan_chengEntry);
		HotSpotEntry yun_diEntry=new HotSpotEntry();
		yun_diEntry.setLatLng(new LatLng(31.293021, 121.536683));
		yun_diEntry.setName("YunDi Tech");
		yun_diEntry.setCollision_count(45);
		yun_diEntry.setRank(12);
		yun_diEntry.setType("VRU");
		hotspots_arraylist.add(yun_diEntry);
		
		HotSpotEntry aaronEntry=new HotSpotEntry();
		aaronEntry.setLatLng(new LatLng(53.523451, -113.510887));
		aaronEntry.setName("Tester's Home");
		aaronEntry.setCollision_count(8);
		aaronEntry.setRank(23);
		aaronEntry.setType("VRU");
		hotspots_arraylist.add(aaronEntry);
		for (int i = 0; i < hotspots_arraylist.size(); i++) {
			Location temp_location= new Location("temp");
			temp_location.setLatitude(hotspots_arraylist.get(i).getLatLng().latitude);
			temp_location.setLongitude(hotspots_arraylist.get(i).getLatLng().longitude);
			int distance=(int)current_locaiton.distanceTo(temp_location);
			if (distance< 500) {
				
				//updateInfoBox(hotspots_arraylist.get(i), String.valueOf(distance));
				
				//if ( near_HotSpotEntry.getName()==hotspots_arraylist.get(i).getName()) {
					// at the same hotspot, do nothing
				//	Log.v("Geofence", "near hotspot123456");
				//}else {
					Log.v("Geofence", "near hotspot");
					near_HotSpotEntry=hotspots_arraylist.get(i);
					if (sharedPreferences_settings.getBoolean(getString(R.string.preferences_setting_notification), true)) {
						Toast.makeText(context, "Attention, High Collision Rate Area", Toast.LENGTH_SHORT ).show();
					}
					
					if (sharedPreferences_settings.getBoolean(getString(R.string.preferences_setting_voice_message), true)) {
						textToSpeech=new TextToSpeech(context, new TextToSpeech.OnInitListener() {			
							@Override
							public void onInit(int status) {
								// TODO Auto-generated method stub
								if (status==TextToSpeech.SUCCESS) {
									textToSpeech.setLanguage(Locale.UK);	
									speakToText("Attention,High Collision Rate Area!");
								}
							}
						});
					}
					
				
				//}
				
			}
		}
		
	}*/
	
	public void checkForLocationForWarning(Location currentLocation){

		final TopInfoEntry temp_topinfoEntry=getWarningMessage(currentLocation);
		if (temp_topinfoEntry.getLocation_name()!="unknown") {
			updateInfoBox(temp_topinfoEntry);
			if (sharedPreferences_settings.getBoolean(getString(R.string.preferences_setting_notification), true)) {
				Toast.makeText(context, temp_topinfoEntry.getWarning_message(), Toast.LENGTH_SHORT ).show();
			}
			
			if (sharedPreferences_settings.getBoolean(getString(R.string.preferences_setting_voice_message), true)) {
				textToSpeech=new TextToSpeech(context, new TextToSpeech.OnInitListener() {			
					@Override
					public void onInit(int status) {
						// TODO Auto-generated method stub
						if (status==TextToSpeech.SUCCESS) {
							textToSpeech.setLanguage(Locale.UK);	
							speakToText(temp_topinfoEntry.getWarning_message());
						}
					}
				});
			}
		}
		
		
	}
	private TopInfoEntry getWarningMessage(Location currentLocation){
			HotspotsDbHelper dbHelper=new HotspotsDbHelper(context);
			CollisionLocationObject temp_collision_location=dbHelper.getNearbyLocation(currentLocation);
			TopInfoEntry temp_TopInfoEntry=new TopInfoEntry();
			if (temp_collision_location.getLoc_code()!="unknown") {
			LocationReasonObject temp_location_reason=dbHelper.getLocationReasonByLocCode(temp_collision_location.getLoc_code());
			WMReasonConditionObject temp_location_condition=dbHelper.getWMReasonConditionByReasonID(temp_location_reason.getReason_id());
				temp_TopInfoEntry.setLocation_name(temp_collision_location.getLocation_name());
				temp_TopInfoEntry.setHotspot_reason(temp_location_condition.getReason());
				temp_TopInfoEntry.setWarning_message(temp_location_condition.getWarning_message());
				Location temp_hotspotLocation=new Location("temp");
				temp_hotspotLocation.setLatitude(temp_collision_location.getLatitude());
				temp_hotspotLocation.setLongitude(temp_collision_location.getLongitude());
				int distance=(int)currentLocation.distanceTo(temp_hotspotLocation);
				temp_TopInfoEntry.setDistance(distance);
			}
			
		return temp_TopInfoEntry;
	}
	 public static void speakToText(String string){
	    	
	    	textToSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, null);
	    }
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		Log.v("Test", "mapfragment getview");
		return super.getView();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
		Log.v("Test", "mapfragment onpause");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
		Log.v("Test", "mapfragment onresume");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
	
		super.onStop();
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12));
		
		if (!hotspots_arraylist.isEmpty()) {	
			// do nothing
		}else {
			loadHotSpotsData();
		}
		//approaching_hotspot_alert(hotspots_arraylist, location);
		Log.v("STTest", "location updated");
		checkForLocationForWarning(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.v("Test", provider+"is disabled");
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.v("Test", provider+"is abled");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	

}
