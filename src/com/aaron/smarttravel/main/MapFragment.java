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
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.aaron.smarttravel.drawer.BottomListAdapter;
import com.aaron.smarttravel.utilities.BottomInfoItem;
import com.aaron.smarttravel.utilities.CollisionLocationObject;
import com.aaron.smarttravel.utilities.DataHandler;
import com.aaron.smarttravel.utilities.HotSpotEntry;
import com.aaron.smarttravel.utilities.LocationReasonObject;
import com.aaron.smarttravel.utilities.TopInfoEntry;
import com.aaron.smarttravel.utilities.WMReasonConditionObject;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressWarnings("deprecation")
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener,OnMarkerClickListener,View.OnClickListener,OnCompletionListener{

	MapView mapView;
	private GoogleMap googleMap;
	public Context context;
	private ArrayList<HotSpotEntry> hotspots_arraylist= new ArrayList<HotSpotEntry>();
	//private HotSpotEntry near_HotSpotEntry=new HotSpotEntry();
	public static TextToSpeech textToSpeech;
	ArrayList<HotSpotEntry> intersection_arraylist, midblock_arraylist,VRU_arraylist;
	private TextView name_TextView,reason_TextView,distance_TextView;
	public SlidingDrawer slidingDrawer,bottom_sldingDrawer;
	private SharedPreferences sharedPreferences_settings;
	private SharedPreferences.Editor preferencesEditor;
	LocationManager locationManager;
	//private static final String SCHOOL_ZONE="SCHOOL ZONE";
	private static final String INTERSECTION="INTERSECTION";
	private static final String MID_AVENUE="MID AVENUE";
	private static final String MID_STREET="MID STREET";
	HotspotsDbHelper dbHelper;
	private TextView bottom_location_name_textview;
	private LinearLayout slidinghanderLayout,bottom_slidinghanderLayout;
	private ListView bottom_list;
	private RelativeLayout driving_modeLayout;
	private Button driving_mode_button;
	private BottomListAdapter bottomListAdapter;
	private CollisionLocationObject temp_collision_location;
	private static int VOICE_MESSAGE_INDICATOR=0,NOTIFICATION_MESSAGE_INDICATOR=0,LOCATION_COUNT=0;
	private Boolean messageBoolean=true;
	private Boolean driving_modeBoolean=false;
	private Location my_location;
	MediaPlayer mediaPlayer = new MediaPlayer();
	private static final int DEFUALT_VOICE_MESSAGE=15;
	private int[] voice_matched_reason_ID={R.raw.morning_rush_hour,R.raw.morning_rush_hour,R.raw.afternoon_rush_hour,R.raw.afternoon_rush_hour
			,R.raw.weekend_early_morning,R.raw.weekend_early_morning,R.raw.pedestrians,R.raw.pedestrians,R.raw.cyclist,R.raw.cyclist
			,R.raw.motorcyclist,R.raw.motorcyclist,R.raw.increase_the_gap,R.raw.increase_the_gap,R.raw.left_turn,R.raw.red_light_running
			,R.raw.stop_sign_violation,R.raw.improper_lane_change,R.raw.improper_lane_change,R.raw.ran_off_road,R.raw.attention_high_risk_collision_area
			,R.raw.attention_high_risk_collision_area,R.raw.school_zone,R.raw.school_zone};
	
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
		bottom_sldingDrawer=(SlidingDrawer)view.findViewById(R.id.bottom_SlidingDrawer);
		bottom_location_name_textview=(TextView)view.findViewById(R.id.bottom_info_location_name);
		bottom_list=(ListView)view.findViewById(R.id.bottom_list);
		
		slidinghanderLayout=(LinearLayout)view.findViewById(R.id.slideHandle);
		bottom_slidinghanderLayout=(LinearLayout)view.findViewById(R.id.bottom_slideHandle);
		driving_modeLayout=(RelativeLayout)view.findViewById(R.id.driving_mode_layout);
		driving_mode_button=(Button)view.findViewById(R.id.not_driving_button);
		driving_mode_button.setOnClickListener(this);
		
		slidinghanderLayout.setOnClickListener(this);
		bottom_slidinghanderLayout.setOnClickListener(this);
		
		sharedPreferences_settings=context.getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		preferencesEditor=sharedPreferences_settings.edit();
		//mapView.onResume();
		
		dbHelper=new HotspotsDbHelper(context);
		
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
		googleMap.setOnMarkerClickListener(this);
		
		//getActivity();
		locationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		my_location=locationManager.getLastKnownLocation(getBestProvider());
	//	loadHotSpotsData();
		if (my_location!=null) {	
			
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(my_location.getLatitude(), my_location.getLongitude()), 12));
			/*if (!hotspots_arraylist.isEmpty()) {
				approaching_hotspot_alert(hotspots_arraylist, my_location);
			}*/
			//checkForLocationForWarning(my_location);
		//	onLocationChanged(my_location);
		}
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
		
		mapView.getMapAsync(this);
		
		return view;
	}
	
	public String getBestProvider(){
		Criteria criteria=new Criteria();
		String best_provider=locationManager.getBestProvider(criteria, true);
		
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			best_provider=LocationManager.NETWORK_PROVIDER;
		}
		return best_provider;
	}
	
	private void updateInfoBox(TopInfoEntry topInfoEntry){
		if (slidingDrawer.getVisibility()==View.INVISIBLE) {
			slidingDrawer.setVisibility(View.VISIBLE);
		}
		
		
		if (!slidingDrawer.isOpened()) {
			slidingDrawer.open();
			
			
		}
		bottom_sldingDrawer.close();
		bottom_sldingDrawer.setVisibility(View.INVISIBLE);
		name_TextView.setText(topInfoEntry.getLocation_name());
		reason_TextView.setText(topInfoEntry.getHotspot_reason());
		distance_TextView.setText(topInfoEntry.getDistance()+"");
		
	}
	

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		context=(SherlockFragmentActivity)activity;
	}
	public void setHotSpots(GoogleMap mymap,ArrayList<CollisionLocationObject> hotspots_ArrayList,int Type_Flag){
		
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

    
    
  /*  public void loadHotSpotsData(){
    	HotspotParse my_HotspotParse=new HotspotParse();
		intersection_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("intersection_top_10.json", context),"Intersection");
		midblock_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("midblock_top_10.json", context),"Midblock");
		VRU_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("VRU_top_x.json", context),"VRU");
		
		hotspots_arraylist.addAll(intersection_arraylist);
		hotspots_arraylist.addAll(midblock_arraylist);
		hotspots_arraylist.addAll(VRU_arraylist);
		
    }*/
    
	
	@Override
	public void onMapReady(GoogleMap map) {
		// TODO Auto-generated method stub
		
		// set markers;		

		setHotSpots(map, dbHelper.getCollisionObjectsByType(INTERSECTION),1);
		setHotSpots(map, dbHelper.getCollisionObjectsByType(MID_AVENUE), 1);
		setHotSpots(map, dbHelper.getCollisionObjectsByType(MID_STREET), 1);
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
			if (!driving_modeBoolean) {
				updateInfoBox(temp_topinfoEntry);
			}
			
			if (sharedPreferences_settings.getBoolean(context.getString(R.string.preferences_setting_notification), true)) {
				NOTIFICATION_MESSAGE_INDICATOR+=1;
				if (NOTIFICATION_MESSAGE_INDICATOR==1||NOTIFICATION_MESSAGE_INDICATOR==6||NOTIFICATION_MESSAGE_INDICATOR==12 && !driving_modeBoolean) {
					Toast.makeText(context, temp_topinfoEntry.getWarning_message(), Toast.LENGTH_SHORT ).show();
				}
				
			}		
				Log.v("STTest", "message:"+currentLocation.getProvider()+VOICE_MESSAGE_INDICATOR);
				if (sharedPreferences_settings.getBoolean(context.getString(R.string.preferences_setting_voice_message), true)) {
					VOICE_MESSAGE_INDICATOR+=1;
					
					if (VOICE_MESSAGE_INDICATOR==1||VOICE_MESSAGE_INDICATOR==6||VOICE_MESSAGE_INDICATOR==12) {
						messageBoolean=false;
						if (voice_matched_reason_ID[temp_topinfoEntry.getReason_id()-1]==DEFUALT_VOICE_MESSAGE) {
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
						}else {
							 mediaPlayer=MediaPlayer.create(getActivity(), voice_matched_reason_ID[temp_topinfoEntry.getReason_id()-1]);

							if (mediaPlayer!=null) {
								 mediaPlayer.setOnCompletionListener(this);
								 mediaPlayer.start();
							}else {
								Log.v("STTest", "mediaplayer create fails");
							}
							
						}	
						//messageBoolean=false;
						//VOICE_MESSAGE_INDICATOR=0;
						messageBoolean=true;
				}

				
			}
		}else {
			NOTIFICATION_MESSAGE_INDICATOR=0;
			VOICE_MESSAGE_INDICATOR=0;
			//if (VOICE_MESSAGE_INDICATOR>=5) {
				messageBoolean=true;
			//}
			
			//VOICE_MESSAGE_INDICATOR+=1;
			
			
			if (slidingDrawer.isOpened()) {
				slidingDrawer.close();
				slidingDrawer.setVisibility(View.INVISIBLE);
			}
		}
		
		
	}
	private TopInfoEntry getWarningMessage(Location currentLocation){
			HotspotsDbHelper dbHelper=new HotspotsDbHelper(context);
			if (messageBoolean) {
				temp_collision_location=dbHelper.getNearbyLocation(currentLocation);
			}
			TopInfoEntry temp_TopInfoEntry=new TopInfoEntry();
			DataHandler dataHandler=new DataHandler();
			
			if (temp_collision_location.getLoc_code()!="unknown") {
			Location dest_location=new Location(LocationManager.GPS_PROVIDER);
			dest_location.setLatitude(temp_collision_location.getLatitude());
			dest_location.setLongitude(temp_collision_location.getLongitude());
			LocationReasonObject temp_location_reason=dataHandler.getHighestPriorityMatchedReasonObject(dbHelper.getLocationReasonByLocCode(temp_collision_location.getLoc_code()),currentLocation,dest_location,context);
			WMReasonConditionObject temp_location_condition=dbHelper.getWMReasonConditionByReasonID(temp_location_reason.getReason_id());
				if (temp_location_reason.getReason_id()!=-1) {
					temp_TopInfoEntry.setLocation_name(temp_collision_location.getLocation_name());
					temp_TopInfoEntry.setHotspot_reason(temp_location_condition.getReason());
					temp_TopInfoEntry.setWarning_message(temp_location_condition.getWarning_message());
					Location temp_hotspotLocation=new Location("temp");
					temp_hotspotLocation.setLatitude(temp_collision_location.getLatitude());
					temp_hotspotLocation.setLongitude(temp_collision_location.getLongitude());
					int distance=(int)currentLocation.distanceTo(temp_hotspotLocation);
					temp_TopInfoEntry.setDistance(distance);
					temp_TopInfoEntry.setReason_id(temp_location_condition.getReason_id());
				}
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
		FlurryAgent.onStartSession(getActivity(), getString(R.string.Flurry_API_Key));
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
		
		
	//	locationManager.removeUpdates(this);
		FlurryAgent.onEndSession(getActivity());
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
		
		if (!hotspots_arraylist.isEmpty()) {	
			
			// do nothing
		}else {
		//	loadHotSpotsData();
		}
		//approaching_hotspot_alert(hotspots_arraylist, location);
		
		Boolean is_drivingBoolean=sharedPreferences_settings.getBoolean(context.getString(R.string.preferences_is_driving), true);
		if (location.getSpeed()>6 && is_drivingBoolean) {
			//setMode(true);
			/*Intent drivingIntent=new Intent(context, DrivingModeActivity.class);
			startActivity(drivingIntent);
			getActivity().finish();*/
			driving_modeLayout.setVisibility(
					View.VISIBLE);
			((SherlockFragmentActivity) context)
			.getSupportActionBar().hide();
			if (slidingDrawer!=null && slidingDrawer.isOpened()) {
				slidingDrawer.close();
				slidingDrawer.setVisibility(View.INVISIBLE);
			}
			if (bottom_sldingDrawer!=null &&bottom_sldingDrawer.isOpened()) {
				bottom_sldingDrawer.close();
				bottom_sldingDrawer.setVisibility(View.INVISIBLE);
			}
			
			driving_modeBoolean=true;
			
		}
		double distance=0;
		if (my_location!=null) {
				distance=location.distanceTo(my_location);
			}
			
			Log.v("STTest", "Location count:"+LOCATION_COUNT+location.getProvider());
			if (distance<100) {
				
				LOCATION_COUNT++;
				if (LOCATION_COUNT>1800) {
					setMode(false);
				}
			}else {
				my_location=location;
				LOCATION_COUNT=0;
			}
			
			// check locations
			
			/*Location location_test=new Location(LocationManager.GPS_PROVIDER);
			location_test.setLatitude(53.467798);
			location_test.setLongitude(-113.492080);*/
			checkForLocationForWarning(location);
	}
		
	private void setMode(Boolean isActive){
		if (isActive) {
			locationManager.removeUpdates(this);
			locationManager.requestLocationUpdates(getBestProvider(), 1000, 0, this);
			//Toast.makeText(context, "Active Mode", Toast.LENGTH_SHORT).show();
		}else {
			locationManager.removeUpdates(this);
			locationManager.requestLocationUpdates(getBestProvider(), 300000, 100, this);
		//	Toast.makeText(context, "DeActive", Toast.LENGTH_SHORT).show();
		}
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

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		String location_name=marker.getTitle();
		DataHandler dHandler=new DataHandler();
		
		UpdateBottomInfoUI(dHandler.getBottomInfoItemByLocaitonName(context, location_name));
		return true;
	}
	
	public void UpdateBottomInfoUI(ArrayList<BottomInfoItem> arrayList_items){
		
		slidingDrawer.close();
		if (arrayList_items.size()>0) {
			bottom_location_name_textview.setText(arrayList_items.get(0).getLocation_name());
			CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(arrayList_items.get(0).getLocationLatLng(), 14);
			googleMap.animateCamera(cameraUpdate);
			
			bottomListAdapter=new BottomListAdapter(context, arrayList_items);
			bottom_list.setAdapter(bottomListAdapter);
			bottom_sldingDrawer.open();
			bottom_sldingDrawer.setVisibility(View.VISIBLE);
		}else {
			Toast.makeText(getActivity(), "no reason available for this location", Toast.LENGTH_SHORT).show();
		}
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.slideHandle:
			if (slidingDrawer.isOpened()) {
				slidingDrawer.close();
			}else {
				slidingDrawer.open();
				bottom_sldingDrawer.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.bottom_slideHandle:
			if (bottom_sldingDrawer.isOpened()) {
				bottom_sldingDrawer.close();
			}else {
				bottom_sldingDrawer.open();
			}
			break;
		case R.id.not_driving_button:
			preferencesEditor.putBoolean(getString(R.string.preferences_is_driving), false);
			preferencesEditor.commit();
			driving_modeLayout.setVisibility(View.INVISIBLE);
			((SherlockFragmentActivity) context).getSupportActionBar().show();
			driving_modeBoolean=false;
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.stop();
		mp.release();
	}

}
