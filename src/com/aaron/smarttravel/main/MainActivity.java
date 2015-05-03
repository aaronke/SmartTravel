package com.aaron.smarttravel.main;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;

public class MainActivity extends BaseActivity {

	private Fragment mContent;
	
	private SharedPreferences my_sharedPreferences;
	
	public MainActivity() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		my_sharedPreferences=getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		
		Boolean first_timeBoolean=my_sharedPreferences.getBoolean(getString(R.string.preferences_first_time), true);
		if (first_timeBoolean) {
			
			SharedPreferences.Editor editor_first_time=my_sharedPreferences.edit();
			editor_first_time.putBoolean(getString(R.string.preferences_first_time), false);
			editor_first_time.commit();
			Intent introductionIntent=new Intent(MainActivity.this,IntroductionActivity.class);
			
			startActivity(introductionIntent);
			finish();
		}
		
		// setSlidingActionBarEnabled(true);
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");

		if (mContent == null){
			mContent = new MapFragment();
			
			
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
			Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(viewIntent);
		}
		
	}
	
	/*public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}*/
}
