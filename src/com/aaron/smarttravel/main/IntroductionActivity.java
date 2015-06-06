package com.aaron.smarttravel.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class IntroductionActivity extends Activity{

	private ImageView got_it_button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.introduction_page);
		getActionBar().hide();
		got_it_button=(ImageView)findViewById(R.id.introduction_got_it);
		
		got_it_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent main_activityIntent=new Intent(IntroductionActivity.this, MainActivity.class);
				startActivity(main_activityIntent);
				
				finish();
				
			}
		});
		
	}
	

}
