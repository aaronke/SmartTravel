package com.aaron.smarttravel.main;

import java.util.ArrayList;

import javax.inject.Inject;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.aaron.smarttravel.injection.SmartTravelApplication;
import com.aaron.smarttravel.main.SampleListFragmentLeft.OnSampleListFragmentLeftListener;
import com.aaron.smarttravel.utilities.BottomInfoItem;
import com.aaron.smarttravel.utilities.Constants;
import com.aaron.smarttravelbackground.WarningService;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.otto.Bus;

public class MainActivity extends BaseActivity implements OnSampleListFragmentLeftListener {

	private Fragment mContent;
	private MapFragment map_fragment;
	
	SharedPreferences sharedPreferences_settings;
	private SharedPreferences.Editor sharEditor;
	@Inject
	Bus bus;
	private int id=0;
	public MainActivity() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_frame);
		
		id=getIntent().getExtras().getInt("ID");
		
		
		((SmartTravelApplication) getApplication()).inject(this);
		bus.register(this);
		
		Intent intent=new Intent(this, WarningService.class);
		intent.setAction(Constants.NOTIFCATION_ACTION_MAIN);
		startService(intent);
		
		// setSlidingActionBarEnabled(true);
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");

		if (mContent == null){
			mContent = new MapFragment();
			Bundle bundle=new Bundle();
			bundle.putInt("ID", id);
			map_fragment=(MapFragment)mContent;
			map_fragment.setArguments(bundle);
		}
		
		sharedPreferences_settings=getApplicationContext().getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		sharEditor=sharedPreferences_settings.edit();
		getSlidingMenu().setMode(SlidingMenu.RIGHT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
	//	getSlidingMenu().setSlidingEnabled(false);
		// set the Above View
		
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
		getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
		
		
		SampleListFragmentLeft sampleListFragmentLeft=new SampleListFragmentLeft();
		Bundle bundle=new Bundle();
		bundle.putInt("ID", id);
		sampleListFragmentLeft.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_two, sampleListFragmentLeft).commit();
		getApplicationContext().getSharedPreferences(getString(R.string.preferences_settings),
				Context.MODE_PRIVATE);
		Log.v("life", "0++");
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
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		sharEditor.putBoolean(getString(R.string.preferences_is_driving), true);
		sharEditor.commit();
		bus.unregister(this);
		
	}
	
	@Override
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		super.onUserLeaveHint();
		//finish();
	}
	
	
}
