package com.aaron.smarttravel.main;

import java.util.ArrayList;

import com.aaron.smarttravel.data.DataParseFromJson;
import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.aaron.smarttravel.utilities.CollisionLocationObject;
import com.aaron.smarttravel.utilities.DayTypeObject;
import com.aaron.smarttravel.utilities.LocationReasonObject;
import com.aaron.smarttravel.utilities.WMReasonConditionObject;
import com.flurry.android.FlurryAgent;
import com.flurry.sdk.is;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class WelcomePage extends Activity {
	 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
	private SharedPreferences my_sharedPreferences;
	private Intent new_intent;
	private LocationManager locationManager;
	private SharedPreferences.Editor shareEditor;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        getActionBar().hide();
        FlurryAgent.setLogEnabled(false);
        FlurryAgent.init(this, getString(R.string.Flurry_API_Key));
        
        
        
        
        my_sharedPreferences=getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		
		Boolean first_timeBoolean=my_sharedPreferences.getBoolean(getString(R.string.preferences_first_time), true);
		
		
		// get current location to check test city;
		locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Location currentLocation=locationManager.getLastKnownLocation(getBestProvider());
		shareEditor=my_sharedPreferences.edit();
		Boolean is_at_shanghai=false;
		if (currentLocation!=null) {
			if (currentLocation.getLongitude()>0) {
				is_at_shanghai=true;
			}
		}
		
		shareEditor.putBoolean(getString(R.string.preferences_is_at_shanghai), is_at_shanghai);
		shareEditor.commit();
		
	//	Toast.makeText(getApplicationContext(), "Active Mode", Toast.LENGTH_SHORT).show();
		
		AudioManager audioManager=(AudioManager)getSystemService(AUDIO_SERVICE);
		int volume_level=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		if (volume_level<6) {
			//audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)-6, AudioManager.FLAG_SHOW_UI);
		}
		Log.v("STTest", "current music volume"+volume_level);
		if (first_timeBoolean) {
			Toast.makeText(getApplicationContext(), "loading data...please wait few seconds", Toast.LENGTH_LONG ).show();
			
			Thread loadDataThread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					loadData();
					Message msgobjMessage=handler.obtainMessage();
					Bundle bundle=new Bundle();
					bundle.putCharSequence("message", "data_loaded");
					msgobjMessage.setData(bundle);
					handler.sendMessage(msgobjMessage);
				}
				
				@SuppressLint("HandlerLeak")
				private final Handler handler=new Handler(){
					public void handleMessage(Message msg){
						String messageString=msg.getData().getString("message");
						if (messageString=="data_loaded") {
							SharedPreferences.Editor editor_first_time=my_sharedPreferences.edit();
							editor_first_time.putBoolean(getString(R.string.preferences_first_time), false);
							editor_first_time.commit();
							new_intent=new Intent(WelcomePage.this,IntroductionActivity.class);
							startActivity(new_intent);
							finish();
						}
					}
				};
				
			});	
			loadDataThread.start();
		}else {
			new_intent = new Intent(WelcomePage.this, MainActivity.class);
			
			new Handler().postDelayed(new Runnable() {
				 
	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */
	 
	            @Override
	            public void run() {
	                // This method will be executed once the timer is over
	                // Start your app main activity
	               
	                startActivity(new_intent);
	 
	                // close this activity
	                finish();
	            }
	        }, SPLASH_TIME_OUT);
		}
        
    }
    
    public String getBestProvider(){
		Criteria criteria=new Criteria();
		String best_provider=locationManager.getBestProvider(criteria, true);
		
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			best_provider=LocationManager.NETWORK_PROVIDER;
		}
		return best_provider;
	}
    private void loadData(){
    	Context context=getApplicationContext();
    	DataParseFromJson test_dataParseFromJson=new DataParseFromJson();
		ArrayList<CollisionLocationObject> collisionLocationObjects_temp=test_dataParseFromJson.getCollisionLocationObjects(test_dataParseFromJson.loadJsonString("locations.json", context));
		ArrayList<LocationReasonObject> locationReasonObjects_temp=test_dataParseFromJson.getLocationReasonObjects(test_dataParseFromJson.loadJsonString("location_reason.json", context));
		ArrayList<WMReasonConditionObject> locationConditionObjects_temp=test_dataParseFromJson.getReasonConditionObjects(test_dataParseFromJson.loadJsonString("reason_condition.json", context));
		ArrayList<DayTypeObject> dayTypeObjects_temp=test_dataParseFromJson.getDataTypeObjects(test_dataParseFromJson.loadJsonString("days.json", context));
		String versionString=test_dataParseFromJson.getNewVersionString(test_dataParseFromJson.loadJsonString("new_version.json", context));
		
		HotspotsDbHelper dbHelper=new HotspotsDbHelper(context);
		dbHelper.insertCollisionLocationTableData(collisionLocationObjects_temp);
		dbHelper.insertLocationReasonTableData(locationReasonObjects_temp);
		dbHelper.insertReasonConditionTableData(locationConditionObjects_temp);
		dbHelper.insertDayTypeTableData(dayTypeObjects_temp);
		dbHelper.insertNewVersionTableData(versionString);
    }

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		FlurryAgent.onStartSession(WelcomePage.this, getString(R.string.Flurry_API_Key));
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		FlurryAgent.onEndSession(WelcomePage.this);
	}
    
   
}