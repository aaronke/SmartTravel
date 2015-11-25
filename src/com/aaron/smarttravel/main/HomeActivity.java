package com.aaron.smarttravel.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.inject.Inject;

import org.apache.http.client.ClientProtocolException;

import com.aaron.smarttravel.data.DataParseFromJson;
import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.aaron.smarttravel.injection.SmartTravelApplication;
import com.aaron.smarttravel.utilities.CollisionLocationObject;
import com.aaron.smarttravel.utilities.DayTypeObject;
import com.aaron.smarttravel.utilities.LocationReasonObject;
import com.aaron.smarttravel.utilities.SchoolZoneObject;
import com.aaron.smarttravel.utilities.WMReasonConditionObject;
import com.squareup.otto.Bus;

import butterknife.ButterKnife;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class HomeActivity extends Activity implements OnClickListener{
	private static final String LOCATION_URL="http://101.231.116.154:8080/STRESTWeb/collisionLocation/jsonOfList";
	private static final String LOCATION_REASON_URL="http://101.231.116.154:8080/STRESTWeb/locationReason/jsonOfList";
	private static final String DAYTYPE_URL="http://101.231.116.154:8080/STRESTWeb/wmDayType/jsonOfList";
	private static final String REASON_CONDITION_URL="http://101.231.116.154:8080/STRESTWeb/wmReasonCondition/jsonOfList";
	private static final String NEW_VERSION_URL="http://101.231.116.154:8080/STRESTWeb/newVersion/json";
	private static final String SCHOOL_ZONE_URL="http://101.231.116.154:8080/STRESTWeb/school/jsonOfList";
	private static final String URL_FLAG_COLLISION_LOCATION="collisionLocation";
	private static final String URL_FLAG_LOCATION_REASON="locationReason";
	private static final String URL_FLAG_WMREASON_CONDITION="wmReasonCondition";
	private static final String URL_FLAG_WMDAY_TYPE="wmDayType";
	private static final String URL_FLAG_SCHOOL_ZONE="school";
	private static final String URL_FLAG_NEW_VERSION="newVersion";
	SharedPreferences sharedPreferences_settings;
	private SharedPreferences.Editor sharEditor;
	@Inject 
	Bus bus;
	ImageButton imageButton_school,imageButton_collision,imageButton_info,imageButton_about;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		getActionBar().hide();
		((SmartTravelApplication) getApplication()).inject(this);
		bus.register(this);
		ButterKnife.bind(this);
		imageButton_school=(ImageButton)findViewById(R.id.button_school);
		imageButton_school.setOnClickListener(this);
		imageButton_collision=(ImageButton)findViewById(R.id.button_collision);
		imageButton_collision.setOnClickListener(this);
		imageButton_info=(ImageButton)findViewById(R.id.button_info);
		imageButton_info.setOnClickListener(this);
		imageButton_about=(ImageButton)findViewById(R.id.button_about);
		imageButton_about.setOnClickListener(this);
		sharedPreferences_settings=getApplicationContext().getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		sharEditor=sharedPreferences_settings.edit();
		Boolean check_updateBoolean=sharedPreferences_settings.getBoolean(getString(R.string.preferences_setting_check_update), true);
		if (check_updateBoolean) {
			ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
			if (networkInfo!=null && networkInfo.isConnected()) {
				new DownloadWebpageTask().execute(NEW_VERSION_URL);
			}
		}
	}

	
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
				}
				else if(urlsStrings[0].contains(URL_FLAG_SCHOOL_ZONE)){
					FLAG=URL_FLAG_SCHOOL_ZONE;
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
				case URL_FLAG_SCHOOL_ZONE:
					ArrayList<SchoolZoneObject> schoolZoneObjects=test_dataParseFromJson.getSchoolZoneObjects(result);
					dbHelper.insertSchoolZones(schoolZoneObjects);
					Log.v("STTest", "schoolZoneObjects:"+schoolZoneObjects.size());
					break;
				case URL_FLAG_WMDAY_TYPE:
					ArrayList<DayTypeObject> dayTypeObjects_temp=test_dataParseFromJson.getDataTypeObjects(result);
					dbHelper.insertDayTypeTableData(dayTypeObjects_temp);
					Log.v("STTest", "DayType:"+dayTypeObjects_temp.size());
					AlertDialog.Builder data_updated=new AlertDialog.Builder(HomeActivity.this);
					data_updated.setTitle(R.string.data_update_dialog)
					.setPositiveButton(R.string.data_update_positive, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent restart_intent=new Intent(HomeActivity.this, WelcomePage.class);
							startActivity(restart_intent);
							finish();
							
						}
					}).setNegativeButton(R.string.data_update_nagetive, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					AlertDialog data_updateAlertDialog=data_updated.create();
					data_updateAlertDialog.show();
					
					break;
				case URL_FLAG_NEW_VERSION:
					String new_versionString=test_dataParseFromJson.getNewVersionString(result);
					String old_versionString=dbHelper.getVersionString();
					Log.v("STTest", "oldversion:"+old_versionString+"newversion:"+new_versionString);
					if (old_versionString!=null) {
						if (new_versionString!=null && !new_versionString.contentEquals(old_versionString)) {
							new DownloadWebpageTask().execute(REASON_CONDITION_URL);
							new DownloadWebpageTask().execute(LOCATION_URL);
							new DownloadWebpageTask().execute(LOCATION_REASON_URL);
							new DownloadWebpageTask().execute(DAYTYPE_URL);
							new DownloadWebpageTask().execute(SCHOOL_ZONE_URL);
							dbHelper.insertNewVersionTableData(new_versionString);
						}	
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Bundle bundle=new Bundle();
		Intent intent = null;
		int id=0;
		switch (v.getId()) {
		case R.id.button_school:
			id=1;
			intent=new Intent(this,MainActivity.class);
			bundle.putInt("ID", id);
			intent.putExtras(bundle);
			break;
		case R.id.button_collision:
			id=2;
			intent=new Intent(this,MainActivity.class);
			bundle.putInt("ID", id);
			intent.putExtras(bundle);
			break;
		case R.id.button_info:
			intent=new Intent(this, InfoActivity.class);
			break;
		case R.id.button_about:
			intent=new Intent(this,AboutActivity.class);
			break;
		default:
			break;
		}
		
		startActivity(intent);
	}


}
