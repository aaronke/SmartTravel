package com.aaron.smarttravel.injection;

import javax.inject.Singleton;

import com.aaron.smarttravel.main.HomeActivity;
import com.aaron.smarttravel.main.MainActivity;
import com.aaron.smarttravel.main.MapFragment;
import com.aaron.smarttravel.main.SampleListFragmentLeft;
import com.aaron.smarttravelbackground.WarningService;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import android.content.Context;
import dagger.Module;
import dagger.Provides;


@Module(
		injects={
			SmartTravelApplication.class,
			MainActivity.class,
			WarningService.class,
			MapFragment.class,
			HomeActivity.class,
			SampleListFragmentLeft.class
		},
		complete=false,
		library=true
	)
public class GeneralModule {
	private final SmartTravelApplication application;
	
	public GeneralModule(SmartTravelApplication application){
		this.application=application;
	}

	@Provides
	@Singleton
	Context provideApplicationContext(){
		return application;
	}
	
	@Provides
	@Singleton
	Bus provideBus(){
		
		return new Bus(ThreadEnforcer.ANY);
	}
	
	
}
