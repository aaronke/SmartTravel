package com.aaron.smarttravel.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DrivingModeActivity extends Activity {

	private Button not_drivingButton;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor shareEditor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driving_mode);
		sharedPreferences=this.getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		not_drivingButton=(Button)findViewById(R.id.not_driving_button);
		
		not_drivingButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareEditor=sharedPreferences.edit();
				shareEditor.putBoolean(getString(R.string.preferences_is_driving), false);
				shareEditor.commit();
				Intent map_intent=new Intent(DrivingModeActivity.this, MainActivity.class);
				startActivity(map_intent);
				finish();
			}
		});
	}
	
	
}
