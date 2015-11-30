package com.aaron.smarttravel.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity {

	private int mTitleRes;
	protected SampleListFragmentLeft mFrag;
	public BaseActivity(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(mTitleRes);
		
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		/*if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			mFrag = new SampleListFragmentLeft();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (SampleListFragmentLeft) this.getSupportFragmentManager()
					.findFragmentById(R.id.menu_frame);
		}
*/
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		
		/*sm.setAnimationCacheEnabled(false);
		sm.setDrawingCacheEnabled(false);
		*/
		
		LayoutInflater actionbar_vInflater=(LayoutInflater) getSupportActionBar().getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		
		final View actionbar_view=actionbar_vInflater.inflate(R.layout.actionbar_custom, null);
		final com.actionbarsherlock.app.ActionBar actionBar=getSupportActionBar();
		
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionbar_view);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		//actionBar.setDisplayUseLogoEnabled(false);
		//actionBar.setIcon(null);
		//getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\"><b>" + "Smart Travel" + "</b></font>"));
	   //getSupportActionBar().setDisplayOptions(0,ActionBar.DISPLAY_USE_LOGO);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater=getSupportMenuInflater();
		menuInflater.inflate(R.menu.actionbar_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//toggle();
			super.onBackPressed();
			return true;
		case R.id.action_settings:
			showSecondaryMenu();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	

}
