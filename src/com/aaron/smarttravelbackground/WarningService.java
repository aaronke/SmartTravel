package com.aaron.smarttravelbackground;

import java.util.Locale;
import javax.inject.Inject;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;
import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.aaron.smarttravel.injection.SmartTravelApplication;
import com.aaron.smarttravel.injection.event.DrivingModeDismissEvent;
import com.aaron.smarttravel.injection.event.DrivingModeEvent;
import com.aaron.smarttravel.injection.event.SlidingDrawerUpdateEvent;
import com.aaron.smarttravel.injection.event.UpdateInfoBoxEvent;
import com.aaron.smarttravel.main.R;
import com.aaron.smarttravel.utilities.CollisionLocationObject;
import com.aaron.smarttravel.utilities.DataHandler;
import com.aaron.smarttravel.utilities.LocationReasonObject;
import com.aaron.smarttravel.utilities.TopInfoEntry;
import com.aaron.smarttravel.utilities.WMReasonConditionObject;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class WarningService extends Service implements  LocationListener ,OnCompletionListener{
	
	@Inject
	Bus bus;
	
	private LocationManager locationManager;
	private static int VOICE_MESSAGE_INDICATOR=0,NOTIFICATION_MESSAGE_INDICATOR=0,LOCATION_COUNT=0;
	private Location my_location;
	private CollisionLocationObject temp_collision_location;
	private Boolean messageBoolean=true;
	private Boolean driving_modeBoolean=false;
	private static final int DEFUALT_VOICE_MESSAGE=15;
	public static TextToSpeech textToSpeech;
	MediaPlayer mediaPlayer = new MediaPlayer();
	private SharedPreferences sharedPreferences_settings;
	private TopInfoEntry currentTopInfoEntry=new TopInfoEntry();
	//private ArrayList<HotSpotEntry> hotspots_arraylist= new ArrayList<HotSpotEntry>();
	private int[] voice_matched_reason_ID={R.raw.morning_rush_hour,R.raw.morning_rush_hour,R.raw.afternoon_rush_hour,R.raw.afternoon_rush_hour
			,R.raw.weekend_early_morning,R.raw.weekend_early_morning,R.raw.pedestrians,R.raw.pedestrians,R.raw.cyclist,R.raw.cyclist
			,R.raw.motorcyclist,R.raw.motorcyclist,R.raw.increase_the_gap,R.raw.increase_the_gap,R.raw.left_turn,R.raw.red_light_running
			,R.raw.stop_sign_violation,R.raw.improper_lane_change,R.raw.improper_lane_change,R.raw.ran_off_road,R.raw.attention_high_risk_collision_area
			,R.raw.attention_high_risk_collision_area,R.raw.school_zone,R.raw.school_zone};
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		((SmartTravelApplication) getApplication()).inject(this);
		bus.register(this);
		
		sharedPreferences_settings=this.getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);

		Log.v("life", "service create");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		bus.unregister(this);
		Log.v("life", "service destoryed");
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.v("life", "service onstart");
		bus.post(new TestEvent("Hello EventBus"));
		requestLocation();
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	public String getBestProvider(){
		Criteria criteria=new Criteria();
		String best_provider=locationManager.getBestProvider(criteria, true);
		
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			best_provider=LocationManager.NETWORK_PROVIDER;
		}
		return best_provider;
	}
	
	public void requestLocation(){
		locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		my_location=locationManager.getLastKnownLocation(getBestProvider());
	//	loadHotSpotsData();
		if (my_location!=null) {	
			
			//googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(my_location.getLatitude(), my_location.getLongitude()), 12));
			/*if (!hotspots_arraylist.isEmpty()) {
				approaching_hotspot_alert(hotspots_arraylist, my_location);
			}*/
			//checkForLocationForWarning(my_location);
		//	onLocationChanged(my_location);
		}
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
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
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Subscribe
	public void DrivingModeDismissHandler(DrivingModeDismissEvent event){
		driving_modeBoolean=false;
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		Boolean is_drivingBoolean=sharedPreferences_settings.getBoolean(this.getString(R.string.preferences_is_driving), true);
		if (location.getSpeed()>6 && is_drivingBoolean) {			
			bus.post(new DrivingModeEvent());	
			driving_modeBoolean=true;
		}
		
		double distance=0;
		if (my_location!=null) {
				distance=location.distanceTo(my_location);
			}
			
			if (distance<100) {
				
				LOCATION_COUNT++;
				if (LOCATION_COUNT>1800) {
					setMode(false);
				}
			}else {
				my_location=location;
				LOCATION_COUNT=0;
			}
			
			checkForLocationForWarning(location);
		
	}
	
	private void voiceMessage( final TopInfoEntry temp_topinfoEntry){
		if (!driving_modeBoolean) {
			bus.post(new UpdateInfoBoxEvent(temp_topinfoEntry));
		}
		
		if (sharedPreferences_settings.getBoolean(this.getString(R.string.preferences_setting_notification), true)) {
			NOTIFICATION_MESSAGE_INDICATOR+=1;
			if (NOTIFICATION_MESSAGE_INDICATOR==1||NOTIFICATION_MESSAGE_INDICATOR==6 && !driving_modeBoolean) {
				Toast.makeText(this, temp_topinfoEntry.getWarning_message(), Toast.LENGTH_SHORT ).show();
			}
			
		}		
			if (sharedPreferences_settings.getBoolean(this.getString(R.string.preferences_setting_voice_message), true)) {
				VOICE_MESSAGE_INDICATOR+=1;
				
				if (VOICE_MESSAGE_INDICATOR==1||VOICE_MESSAGE_INDICATOR==6) {
					messageBoolean=false;
					if (voice_matched_reason_ID[temp_topinfoEntry.getReason_id()-1]==DEFUALT_VOICE_MESSAGE) {
						textToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {			
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
						try {
							 mediaPlayer=MediaPlayer.create(this, voice_matched_reason_ID[temp_topinfoEntry.getReason_id()-1]);
						} catch (Exception e) {
							// TODO: handle exception
						}

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
				if (VOICE_MESSAGE_INDICATOR>6) {
					
					bus.post(new SlidingDrawerUpdateEvent());
				}				
		}
	}
public void checkForLocationForWarning(Location currentLocation){
		
		TopInfoEntry temp_topinfoEntry=getWarningMessage(currentLocation);
		if (temp_topinfoEntry.getLocation_name()!="unknown") {
			if (!currentTopInfoEntry.getLocation_name().equals(temp_topinfoEntry.getLocation_name())) {
				initialUIafterWarning();
			}
			currentTopInfoEntry=temp_topinfoEntry;
		}else {
			currentTopInfoEntry=new TopInfoEntry();
		}
		if (currentTopInfoEntry.getLocation_name()!="unknown") {
			voiceMessage(currentTopInfoEntry);
		}
			
	}

	private void initialUIafterWarning(){
		NOTIFICATION_MESSAGE_INDICATOR=0;
		VOICE_MESSAGE_INDICATOR=0;
		messageBoolean=true;	
		bus.post(new SlidingDrawerUpdateEvent());
}

	public static void speakToText(String string){
   	
   	textToSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, null);
   }

	@Override
	public void onCompletion(MediaPlayer mp) {
	// TODO Auto-generated method stub
	mp.stop();
	mp.release();
}


private TopInfoEntry getWarningMessage(Location currentLocation){
	HotspotsDbHelper dbHelper=new HotspotsDbHelper(this);
	if (messageBoolean) {
		temp_collision_location=dbHelper.getNearbyLocation(currentLocation);
	}
	TopInfoEntry temp_TopInfoEntry=new TopInfoEntry();
	DataHandler dataHandler=new DataHandler();
	
	if (temp_collision_location.getLoc_code()!="unknown") {
	Location dest_location=new Location(LocationManager.GPS_PROVIDER);
	dest_location.setLatitude(temp_collision_location.getLatitude());
	dest_location.setLongitude(temp_collision_location.getLongitude());
	LocationReasonObject temp_location_reason=dataHandler.getHighestPriorityMatchedReasonObject(dbHelper.getLocationReasonByLocCode(temp_collision_location.getLoc_code()),currentLocation,dest_location,this);
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


	@Override
	public void onProviderDisabled(String arg0) {
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

}
