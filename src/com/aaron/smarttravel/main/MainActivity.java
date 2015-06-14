package com.aaron.smarttravel.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.aaron.smarttravel.data.DataParseFromJson;
import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.aaron.smarttravel.main.SampleListFragment.OnSampleListFragmentListener;
import com.aaron.smarttravel.utilities.BottomInfoItem;
import com.aaron.smarttravel.utilities.CollisionLocationObject;
import com.aaron.smarttravel.utilities.DayTypeObject;
import com.aaron.smarttravel.utilities.LocationReasonObject;
import com.aaron.smarttravel.utilities.WMReasonConditionObject;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends BaseActivity implements OnSampleListFragmentListener {

	private Fragment mContent;
	private MapFragment map_fragment;
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
	
	public MainActivity() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		// setSlidingActionBarEnabled(true);
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");

		if (mContent == null){
			mContent = new MapFragment();	
			map_fragment=(MapFragment)mContent;
		}
		SharedPreferences sharedPreferences_settings=getApplicationContext().getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		Boolean check_updateBoolean=sharedPreferences_settings.getBoolean(getString(R.string.preferences_setting_check_update), true);
		if (check_updateBoolean) {
			ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
			if (networkInfo!=null && networkInfo.isConnected()) {
				new DownloadWebpageTask().execute(NEW_VERSION_URL);
			}
		}
		
		getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
	//	getSlidingMenu().setSlidingEnabled(false);
		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		
		getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
		getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_two, new SampleListFragmentRight()).commit();
		
		
		getApplicationContext().getSharedPreferences(getString(R.string.preferences_settings),
				Context.MODE_PRIVATE);
		
		LocationManager locationManager=(LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			AlertDialog.Builder location_settingBuilder=new AlertDialog.Builder(this);
			
			location_settingBuilder.setTitle(R.string.location_setting_alert_title)
			.setPositiveButton(R.string.location_setting_positive, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(viewIntent);
				}
			});
			location_settingBuilder.setNegativeButton(R.string.location_setting_nagetive, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			
			AlertDialog location_Dialog=location_settingBuilder.create();
			location_Dialog.show();
		}	
	}

	@Override
	public void onitemselected(ArrayList<BottomInfoItem> arrayList) {
		// TODO Auto-generated method stub
		
		MapFragment temp_mapFragment=(MapFragment)getSupportFragmentManager().findFragmentById(map_fragment.getId());
		
		if (temp_mapFragment!=null) {
			map_fragment.UpdateBottomInfoUI(arrayList);
			toggle();
		}else {
			Log.v("STTest", "fail to transfer message");
		}
	}
	
	/*public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}*/
	
	private class DownloadWebpageTask extends AsyncTask<String, Void, String>{
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
				
				return getJsonStringFromURL(urlsStrings[0]);
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
					Log.v("STTest", "collisionLocation:"+collisionLocationObjects_temp.size());
					break;
				case URL_FLAG_LOCATION_REASON:
					ArrayList<LocationReasonObject> locationReasonObjects_temp=test_dataParseFromJson.getLocationReasonObjects(result);
					dbHelper.insertLocationReasonTableData(locationReasonObjects_temp);
					Log.v("STTest", "LocationReason:"+locationReasonObjects_temp.size());
					break;
				case URL_FLAG_WMREASON_CONDITION:
					ArrayList<WMReasonConditionObject> ReasonConditionObjects_temp=test_dataParseFromJson.getReasonConditionObjects(result);
					dbHelper.insertReasonConditionTableData(ReasonConditionObjects_temp);
					Log.v("STTest", "LocationCondition:"+ReasonConditionObjects_temp.size());
					break;
				case URL_FLAG_WMDAY_TYPE:
					ArrayList<DayTypeObject> dayTypeObjects_temp=test_dataParseFromJson.getDataTypeObjects(result);
					dbHelper.insertDayTypeTableData(dayTypeObjects_temp);
					Log.v("STTest", "DayType:"+dayTypeObjects_temp.size());
					break;
				case URL_FLAG_NEW_VERSION:
					String new_versionString=test_dataParseFromJson.getNewVersionString(result);
					String old_versionString=dbHelper.getVersionString();
					Log.v("STTest", "oldversion:"+old_versionString+"newversion:"+new_versionString);
					if (!old_versionString.contentEquals(new_versionString)) {
						new DownloadWebpageTask().execute(REASON_CONDITION_URL);
						new DownloadWebpageTask().execute(LOCATION_URL);
						new DownloadWebpageTask().execute(LOCATION_REASON_URL);
						new DownloadWebpageTask().execute(DAYTYPE_URL);
						dbHelper.insertNewVersionTableData(new_versionString);
					}	
					break;
				default:
					break;
				}
			}
		}
    }
	
	public String getJsonStringFromURL(String urlString) throws IOException{
		String jsonString="";
		InputStream inputStream = null;
		try {
			URL url=new URL(urlString);
			HttpURLConnection connection=(HttpURLConnection)url.openConnection();
			connection.setReadTimeout(20000);
			connection.setConnectTimeout(25000);
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.connect();
				inputStream=connection.getInputStream();
				BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
				String line=null;
				StringBuilder sBuilder=new StringBuilder();
				while ((line=reader.readLine())!=null) {
					sBuilder.append(line);
				}
				
			jsonString= sBuilder.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO: handle exception
			e.printStackTrace();
		}catch (ClientProtocolException e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if (inputStream!=null) {
				inputStream.close();
			}
		}
		return jsonString;
	}
}
