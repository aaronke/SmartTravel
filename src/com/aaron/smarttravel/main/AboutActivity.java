package com.aaron.smarttravel.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class AboutActivity extends Activity{
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
		 params.setMargins(80, 20, 0, 5);
		 TextView_title.setLayoutParams(params);
		 android.app.ActionBar actionBar=getActionBar();
		
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionbar_view);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
	}

}
