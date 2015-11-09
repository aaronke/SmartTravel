package com.aaron.smarttravel.injection;

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import com.squareup.otto.Bus;
import dagger.ObjectGraph;


public class SmartTravelApplication extends MultiDexApplication{
	
	@Inject
	Bus bus;
	
	private ObjectGraph graph;
	
	public SmartTravelApplication(){
		
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		graph=ObjectGraph.create(getModules().toArray());
		graph.inject(this);
		bus.register(this);
	}
	
	private List<Object>getModules(){
		return Arrays.asList(new ApplicationModule(this),new GeneralModule(this));
	}
	
	public void inject(Object object){
		graph.inject(object);
	}

}
