package com.aaron.smarttravel.main;

import java.util.ArrayList;
import com.aaron.smarttravel.data.DataParseFromJson;
import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.aaron.smarttravel.utilities.CollisionLocationObject;
import com.aaron.smarttravel.utilities.DayTypeObject;
import com.aaron.smarttravel.utilities.LocationReasonObject;
import com.aaron.smarttravel.utilities.WMReasonConditionObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class WelcomePage extends Activity {
	 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
	private SharedPreferences my_sharedPreferences;
	private Intent new_intent;
	private static final String LOCATION_URL="http://101.231.116.154:8080/STRESTWeb/collisionLocation/jsonOfList";
	private static final String LOCATION_REASON_URL="http://101.231.116.154:8080/STRESTWeb/locationReason/jsonOfList";
	private static final String DAYTYPE_URL="http://101.231.116.154:8080/STRESTWeb/wmDayType/jsonOfList";
	private static final String REASON_CONDITION_URL="http://101.231.116.154:8080/STRESTWeb/wmReasonCondition/jsonOfList";
	private static final String NEW_VERSION_URL="http://101.231.116.154:8080/STRESTWeb/newVersion/json";
	private static final String URL_FLAG_COLLISION_LOCATION="collisionLocation";
	private static final String URL_FLAG_LOCATION_REASON="locationReason";
	private static final String URL_FLAG_WMREASON_CONDITION="wmReasonCondition";
	private static final String URL_FLAG_WMDAY_TYPE="wmDayType";
	private static final String URL_FLAG_NEW_VERSION="newVersion";
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        getActionBar().hide();
        
        my_sharedPreferences=getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		
		Boolean first_timeBoolean=my_sharedPreferences.getBoolean(getString(R.string.preferences_first_time), true);
		
		
		/*ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
		if (networkInfo!=null && networkInfo.isConnected()) {
			new DownloadWebpageTask().execute(LOCATION_URL);
			new DownloadWebpageTask().execute(LOCATION_REASON_URL);
			new DownloadWebpageTask().execute(DAYTYPE_URL);
			new DownloadWebpageTask().execute(REASON_CONDITION_URL);
		}*/
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
							new_intent=new Intent(WelcomePage.this,TermOfUseActivity.class);
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
    
    private void loadData(){
    	Context context=getApplicationContext();
    	DataParseFromJson test_dataParseFromJson=new DataParseFromJson();
		ArrayList<CollisionLocationObject> collisionLocationObjects_temp=test_dataParseFromJson.getCollisionLocationObjects(test_dataParseFromJson.loadJsonString("locations.json", context));
		ArrayList<LocationReasonObject> locationReasonObjects_temp=test_dataParseFromJson.getLocationReasonObjects(test_dataParseFromJson.loadJsonString("location_reason.json", context));
		ArrayList<WMReasonConditionObject> locationConditionObjects_temp=test_dataParseFromJson.getReasonConditionObjects(test_dataParseFromJson.loadJsonString("reason_condition.json", context));
		ArrayList<DayTypeObject> dayTypeObjects_temp=test_dataParseFromJson.getDataTypeObjects(test_dataParseFromJson.loadJsonString("days.json", context));
		
		HotspotsDbHelper dbHelper=new HotspotsDbHelper(context);
		dbHelper.insertCollisionLocationTableData(collisionLocationObjects_temp);
		dbHelper.insertLocationReasonTableData(locationReasonObjects_temp);
		dbHelper.insertReasonConditionTableData(locationConditionObjects_temp);
		dbHelper.insertDayTypeTableData(dayTypeObjects_temp);
    }
    
    
    private class DownloadWebpageTask extends AsyncTask<String, Void, String>{
    	DataParseFromJson tempDataParseFromJson=new DataParseFromJson();
    	String FLAG="";
		@Override
		protected String doInBackground(String... urlsStrings) {
			// TODO Auto-generated method stub
			try {
				if (urlsStrings[0].contains(URL_FLAG_COLLISION_LOCATION)) {
					FLAG=URL_FLAG_COLLISION_LOCATION;
				}else if(urlsStrings[0].contains(URL_FLAG_LOCATION_REASON)) {
					FLAG=URL_FLAG_LOCATION_REASON;
				}else if (urlsStrings[0].contains(URL_FLAG_WMREASON_CONDITION)) {
					FLAG=URL_FLAG_WMREASON_CONDITION;
				}else if (urlsStrings[0].contains(URL_FLAG_WMDAY_TYPE)) {
					FLAG=URL_FLAG_WMDAY_TYPE;
				}else {
					FLAG=URL_FLAG_NEW_VERSION;
				}
				return tempDataParseFromJson.getJsonStringFromURL(urlsStrings[0]);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result){
		
			if (result!=null) {
				Context context=getApplicationContext();
				DataParseFromJson test_dataParseFromJson=new DataParseFromJson();
				HotspotsDbHelper dbHelper=new HotspotsDbHelper(context);
				switch (FLAG) {
				case URL_FLAG_COLLISION_LOCATION:
					ArrayList<CollisionLocationObject> collisionLocationObjects_temp=test_dataParseFromJson.getCollisionLocationObjects(result);
					dbHelper.insertCollisionLocationTableData(collisionLocationObjects_temp);
					break;
				case URL_FLAG_LOCATION_REASON:
					ArrayList<LocationReasonObject> locationReasonObjects_temp=test_dataParseFromJson.getLocationReasonObjects(result);
					dbHelper.insertLocationReasonTableData(locationReasonObjects_temp);
					break;
				case URL_FLAG_WMREASON_CONDITION:
					ArrayList<WMReasonConditionObject> locationConditionObjects_temp=test_dataParseFromJson.getReasonConditionObjects(result);
					dbHelper.insertReasonConditionTableData(locationConditionObjects_temp);
					break;
				case URL_FLAG_WMDAY_TYPE:
					ArrayList<DayTypeObject> dayTypeObjects_temp=test_dataParseFromJson.getDataTypeObjects(result);
					dbHelper.insertDayTypeTableData(dayTypeObjects_temp);
					break;
				case URL_FLAG_NEW_VERSION:
					String new_versionString=test_dataParseFromJson.getNewVersionString(result);
					dbHelper.insertNewVersionTableData(new_versionString);
					break;
				default:
					break;
				}
			}
		}
    	
    }
}