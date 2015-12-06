package com.aaron.smarttravel.main;

import android.app.Activity;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class InfoActivity extends Activity implements android.view.View.OnClickListener{

	private ImageButton imageButton;
	private TextView actionbarHome;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_activity);
		
		imageButton=(ImageButton)findViewById(R.id.info_button);
		imageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.edmonton.ca/transportation/traffic-safety.aspx"));
				startActivity(i);
			}
		});
		LayoutInflater actionbar_vInflater=(LayoutInflater) getActionBar().getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		
		 View actionbar_view=actionbar_vInflater.inflate(R.layout.actionbar_custom, null);
		 TextView TextView_title=(TextView)actionbar_view.findViewById(R.id.actionbar_title);
		 actionbarHome=(TextView)actionbar_view.findViewById(R.id.actionbar_home);
		 actionbarHome.setOnClickListener(this);
		 TextView_title.setText("Traffic Safety Information");
		 android.app.ActionBar actionBar=getActionBar();
		
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionbar_view);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.actionbar_home:
			onBackPressed();
			break;

		default:
			break;
		}
		
	}
	

}
