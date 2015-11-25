package com.aaron.smarttravel.main;

import com.aaron.smarttravel.database.HotspotsDbHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class AboutActivity extends Activity implements View.OnClickListener{
	private TextView update_date_TextView,version_TextView;
	private ImageButton aboutButton,termButton,userButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		LayoutInflater actionbar_vInflater=(LayoutInflater) getActionBar().getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		
		 View actionbar_view=actionbar_vInflater.inflate(R.layout.actionbar_custom, null);
		 TextView TextView_title=(TextView)actionbar_view.findViewById(R.id.actionbar_title);
		 TextView_title.setText("About");
		 LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		 params.setMargins(120, 20, 0, 5);
		 TextView_title.setLayoutParams(params);
		 android.app.ActionBar actionBar=getActionBar();
		 aboutButton=(ImageButton)findViewById(R.id.about_button);
		 aboutButton.setOnClickListener(this);
		 termButton=(ImageButton)findViewById(R.id.about_term);
		 termButton.setOnClickListener(this);
		 userButton=(ImageButton)findViewById(R.id.about_user);
		 userButton.setOnClickListener(this);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionbar_view);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		update_date_TextView=(TextView)findViewById(R.id.setting_update_date_textview);
		version_TextView=(TextView)findViewById(R.id.setting_update_version_textview);
		HotspotsDbHelper dbHelper=new HotspotsDbHelper(this);
		update_date_TextView.setText("update date:"+dbHelper.getVersionString());
		String versionnameString = null;
		try {
			versionnameString = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		version_TextView.setText("version:"+versionnameString);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.edmonton.ca/transportation/traffic-safety.aspx"));
		startActivity(i);
	}


}
