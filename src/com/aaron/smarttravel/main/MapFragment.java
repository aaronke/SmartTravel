package com.aaron.smarttravel.main;

import java.util.ArrayList;
import java.util.Locale;

import com.aaron.smarttravel.data.HotspotParse;
import com.aaron.smarttravel.geofence.GeofenceErrorMessages;
import com.aaron.smarttravel.geofence.GeofenceTransitionsIntentService;
import com.aaron.smarttravel.utilities.HotSpotEntry;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener, ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<Status>{

	MapView mapView;
	private GoogleMap googleMap;
	public Context context;
	private ArrayList<Geofence> my_geofence_arraylist=new ArrayList<Geofence>();
	private PendingIntent geofenceIntent;
	private GoogleApiClient geofenApiClient;
	private ArrayList<HotSpotEntry> hotspots_arraylist= new ArrayList<HotSpotEntry>();
	private HotSpotEntry near_HotSpotEntry=new HotSpotEntry();
	public static TextToSpeech textToSpeech;
	ArrayList<HotSpotEntry> intersection_arraylist, midblock_arraylist,VRU_arraylist;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		View view=inflater.inflate(R.layout.main_map, container, false);
		mapView=(MapView) view.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		//mapView.onResume();
		
		/*
		View location_buttonView=((View)mapView.findViewById(1).getParent()).findViewById(2);
		
		location_buttonView.setBackgroundResource(R.drawable.location_icon);
		
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(54, 66);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
		params.setMargins(80, 0, 0, 80);
		location_buttonView.setLayoutParams(params);*/
		buildGoogleApiClient();
		
		
		try {
			MapsInitializer.initialize(context);
		} catch (Exception e) {
			// TODO: handle exception
		}
		googleMap=mapView.getMap();
		CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(new LatLng(53.539150,  -113.496867), 10);
		googleMap.animateCamera(cameraUpdate);
		googleMap.setMyLocationEnabled(true);
	//	googleMap.setTrafficEnabled(true);
		googleMap.getUiSettings().setZoomControlsEnabled(true);
		
		//getActivity();
		LocationManager locationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria=new Criteria();
		String best_location_provider=locationManager.getBestProvider(criteria, true);
		
		Location my_location=locationManager.getLastKnownLocation(best_location_provider);
		loadHotSpotsData();
		if (my_location!=null) {	
			
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(my_location.getLatitude(), my_location.getLongitude()), 12));
			if (!hotspots_arraylist.isEmpty()) {
				approaching_hotspot_alert(hotspots_arraylist, my_location);
			}
			onLocationChanged(my_location);
		}
		
		locationManager.requestLocationUpdates(best_location_provider, 7000, 0, this);
		
		mapView.getMapAsync(this);
		
		return view;
	}
	
	private void buildGoogleApiClient() {
		// TODO Auto-generated method stub
		geofenApiClient=new GoogleApiClient.Builder(context)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)
		.addApi(LocationServices.API)
		.build();
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
			mymap.addMarker(markerOptions);		
		}
		
	}
	
	// Geofence request;
	private GeofencingRequest getGeofencingRequest(){
		
		GeofencingRequest.Builder builder=new GeofencingRequest.Builder();
		builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER|GeofencingRequest.INITIAL_TRIGGER_DWELL);
		builder.addGeofences(my_geofence_arraylist);
		return builder.build();
	}
	
	// add Geofences to geofencelist
	public void addGeofences(ArrayList<HotSpotEntry> hotspots_arraylist){
		
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
	//	geofenceHandler=new GeofenceHandler(getActivity(), my_geofence_arraylist);
		
		try {
			LocationServices.GeofencingApi.addGeofences(geofenApiClient, 
					getGeofencingRequest(), getGeofencePendingIntent()).setResultCallback(this);
				Log.v("Geofence", "test");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofenceIntent != null) {
            return geofenceIntent;
        }
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    
    public void loadHotSpotsData(){
    	HotspotParse my_HotspotParse=new HotspotParse();
		intersection_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("intersection_top_10.json", context));
		midblock_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("midblock_top_10.json", context));
		VRU_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("VRU_top_x.json", context));
		
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
		
		if (near_HotSpotEntry.getName()=="UnKnown") {
			Log.v("Geofence", "unknown");
		}else {
			Log.v("Geofence", "clear something");
			near_HotSpotEntry=new HotSpotEntry();
		}
		
	}
	
	public void approaching_hotspot_alert(ArrayList<HotSpotEntry> hotspots_arraylist,Location current_locaiton){
		HotSpotEntry lan_chengEntry=new HotSpotEntry();
		lan_chengEntry.setLatLng(new LatLng(31.146008, 121.513420));
		lan_chengEntry.setName("LanCheng's Home");
		hotspots_arraylist.add(lan_chengEntry);
		HotSpotEntry yun_diEntry=new HotSpotEntry();
		yun_diEntry.setLatLng(new LatLng(31.293021, 121.536683));
		yun_diEntry.setName("YunDi Tech");
		hotspots_arraylist.add(yun_diEntry);
		
		HotSpotEntry aaronEntry=new HotSpotEntry();
		aaronEntry.setLatLng(new LatLng(53.523451, -113.510887));
		aaronEntry.setName("Tester's Home");
		hotspots_arraylist.add(aaronEntry);
		for (int i = 0; i < hotspots_arraylist.size(); i++) {
			Location temp_location= new Location("temp");
			temp_location.setLatitude(hotspots_arraylist.get(i).getLatLng().latitude);
			temp_location.setLongitude(hotspots_arraylist.get(i).getLatLng().longitude);
			float distance=current_locaiton.distanceTo(temp_location);
			if (distance< 700) {
				//if ( near_HotSpotEntry.getName()==hotspots_arraylist.get(i).getName()) {
					// at the same hotspot, do nothing
				//	Log.v("Geofence", "near hotspot123456");
				//}else {
					Log.v("Geofence", "near hotspot");
					near_HotSpotEntry=hotspots_arraylist.get(i);
					Toast.makeText(context, "Attention, High Collision Rate Area", Toast.LENGTH_SHORT ).show();
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
				
				//}
				
			}
		}
		
	}
	
	 public static void speakToText(String string){
	    	
	    	textToSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, null);
	    }
	@Override
	public View getView() {
		// TODO Auto-generated method stub
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
		geofenApiClient.connect();
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
		geofenApiClient.disconnect();
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
		approaching_hotspot_alert(hotspots_arraylist, location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResult(Status status) {
		// TODO Auto-generated method stub
		if (status.isSuccess()) {
			Log.v("Geofence", "success");
		}else {
			String errorString=GeofenceErrorMessages.getErrorString(context, status.getStatusCode());
			Log.v("Geofence", errorString);
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		Log.v("Geofence", "connection failed");
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Log.v("Geofence", "connected");
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		Log.v("Geofence", "connection suspended");
	}
		
	

}
