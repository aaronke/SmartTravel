package com.aaron.smarttravel.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class WelcomePage extends Activity {
	 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
	private SharedPreferences my_sharedPreferences;
	private Intent new_intent;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        getActionBar().hide();
        
        my_sharedPreferences=getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		
		Boolean first_timeBoolean=my_sharedPreferences.getBoolean(getString(R.string.preferences_first_time), true);
		if (first_timeBoolean) {
			
			SharedPreferences.Editor editor_first_time=my_sharedPreferences.edit();
			editor_first_time.putBoolean(getString(R.string.preferences_first_time), false);
			editor_first_time.commit();
			new_intent=new Intent(WelcomePage.this,TermOfUseActivity.class);
		}else {
			new_intent = new Intent(WelcomePage.this, MainActivity.class);
		}
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
               
                startActivity(new_intent);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
 
}