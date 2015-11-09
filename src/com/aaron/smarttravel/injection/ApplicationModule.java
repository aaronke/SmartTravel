package com.aaron.smarttravel.injection;

import javax.inject.Singleton;

import android.content.Context;
import dagger.Module;
import dagger.Provides;


@Module(library=true)
public class ApplicationModule {
	private final SmartTravelApplication application;
	
	public ApplicationModule(SmartTravelApplication application){this.application=application;}
	
	
	@Provides
	@Singleton
	@ForApplication
	Context provideApplicationContext(){return application;}
}
