package com.aaron.smarttravel.main;

import com.aaron.smarttravel.main.SampleListFragment.OnSampleListFragmentListener;
import com.aaron.smarttravel.utilities.BottomInfoItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;

public class MainActivity extends BaseActivity implements OnSampleListFragmentListener {

	private Fragment mContent;
	private MapFragment map_fragment;
	
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
	public void onitemselected(BottomInfoItem bottomInfoitem) {
		// TODO Auto-generated method stub
		
		MapFragment temp_mapFragment=(MapFragment)getSupportFragmentManager().findFragmentById(map_fragment.getId());
		
		if (temp_mapFragment!=null) {
			map_fragment.UpdateBottomInfoUI(bottomInfoitem);
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
}
