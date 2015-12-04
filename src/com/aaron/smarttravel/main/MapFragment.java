package com.aaron.smarttravel.main;

import java.util.ArrayList;

import javax.inject.Inject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.aaron.smarttravel.drawer.BottomListAdapter;
import com.aaron.smarttravel.injection.SmartTravelApplication;
import com.aaron.smarttravel.injection.event.DrivingModeDismissEvent;
import com.aaron.smarttravel.injection.event.DrivingModeEvent;
import com.aaron.smarttravel.injection.event.MapReadyEvent;
import com.aaron.smarttravel.injection.event.SlidingDrawerUpdateEvent;
import com.aaron.smarttravel.injection.event.UpdateInfoBoxEvent;
import com.aaron.smarttravel.utilities.BottomInfoItem;
import com.aaron.smarttravel.utilities.CollisionHandler;
import com.aaron.smarttravel.utilities.DataHandler;
import com.aaron.smarttravel.utilities.HotSpotEntry;
import com.aaron.smarttravel.utilities.SchoolZoneHandler;
import com.aaron.smarttravel.utilities.TopInfoEntry;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


@SuppressWarnings("deprecation")
public class MapFragment extends Fragment implements OnMapReadyCallback,OnMarkerClickListener,OnMapLoadedCallback,
View.OnClickListener,OnCameraChangeListener,android.widget.RadioGroup.OnCheckedChangeListener{

	@Inject
	Bus bus;
	
	MapView mapView;
	private GoogleMap googleMap;
	public Context context;
	
	//private HotSpotEntry near_HotSpotEntry=new HotSpotEntry();
	
	ArrayList<HotSpotEntry> intersection_arraylist, midblock_arraylist,VRU_arraylist;
	private TextView name_TextView,reason_TextView,distance_TextView;
	public SlidingDrawer slidingDrawer,bottom_sldingDrawer;
	private SharedPreferences sharedPreferences_settings;
	private SharedPreferences.Editor preferencesEditor;
	
	//private static final String SCHOOL_ZONE="SCHOOL ZONE";
	
	HotspotsDbHelper dbHelper;
	private TextView bottom_location_name_textview;
	private LinearLayout slidinghanderLayout,bottom_slidinghanderLayout;
	private ListView bottom_list;
	private RelativeLayout driving_modeLayout;
	private Button driving_mode_button;
	private BottomListAdapter bottomListAdapter;
	private SchoolZoneHandler schoolZoneHandler;

	private LinearLayout bottom_linearLayout;
	private TextView school_zone_text;
	private Boolean school_Segments_IndicatorBoolean=false;
	private CollisionHandler collisionHandler;
	private TextView actionbar_titleTextView;
	private int id=0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.main_map, container, false);
		
		Bundle bundle=this.getArguments();
		id=bundle.getInt("ID");
		name_TextView=(TextView)view.findViewById(R.id.info_box_hotspot_name);
		reason_TextView=(TextView)view.findViewById(R.id.info_box_reason);
		distance_TextView=(TextView)view.findViewById(R.id.info_box_distance);
		slidingDrawer=(SlidingDrawer)view.findViewById(R.id.SlidingDrawer);
		bottom_sldingDrawer=(SlidingDrawer)view.findViewById(R.id.bottom_SlidingDrawer);
		bottom_location_name_textview=(TextView)view.findViewById(R.id.bottom_info_location_name);
		bottom_list=(ListView)view.findViewById(R.id.bottom_list);
		
		slidinghanderLayout=(LinearLayout)view.findViewById(R.id.slideHandle);
		bottom_slidinghanderLayout=(LinearLayout)view.findViewById(R.id.bottom_slideHandle);
		driving_modeLayout=(RelativeLayout)view.findViewById(R.id.driving_mode_layout);
		driving_mode_button=(Button)view.findViewById(R.id.not_driving_button);
		
		
		bottom_linearLayout=(LinearLayout)view.findViewById(R.id.bottom_info_item_title_bar);
		school_zone_text=(TextView)view.findViewById(R.id.school_zone_text);
		driving_mode_button.setOnClickListener(this);
		
		slidinghanderLayout.setOnClickListener(this);
		bottom_slidinghanderLayout.setOnClickListener(this);
		
		sharedPreferences_settings=context.getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		preferencesEditor=sharedPreferences_settings.edit();
		//mapView.onResume();
		
		
		
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		View actionbarView=((BaseActivity)getActivity()).getSupportActionBar().getCustomView();
		actionbar_titleTextView=(TextView)actionbarView.findViewById(R.id.actionbar_title);
		if (id==1) {
			RadioButton radioButton=(RadioButton)view.findViewById(R.id.radiobutton_schoolzone);
			radioButton.setChecked(true);
			actionbar_titleTextView.setText("School Zones");
			LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			params.setMargins(70, 24, 0, 5);
			actionbar_titleTextView.setLayoutParams(params);
		}else {
			actionbar_titleTextView.setText("High Collision Locations");
		}
		dbHelper=new HotspotsDbHelper(context);
		
		try {
			MapsInitializer.initialize(context);
		} catch (Exception e) {
			// TODO: handle exception
		}
		mapView=(MapView) view.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		googleMap=mapView.getMap();
		googleMap.setOnMapLoadedCallback(this);
		CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(new LatLng(53.539150,  -113.496867), 12);
		googleMap.animateCamera(cameraUpdate);
		googleMap.setMyLocationEnabled(true);
	//	googleMap.setTrafficEnabled(true);
		googleMap.getUiSettings().setZoomControlsEnabled(true);
		googleMap.setOnMarkerClickListener(this);
		googleMap.setOnCameraChangeListener(this);
		
		//getActivity();
		mapView.getMapAsync(this);
	}
	@Subscribe
	public void OnupdateInfoBoxEvent(UpdateInfoBoxEvent event){
		updateInfoBox(event.getInfoEntry());
	}
	
	private void updateInfoBox(TopInfoEntry topInfoEntry){
		if (slidingDrawer.getVisibility()==View.INVISIBLE) {
			slidingDrawer.setVisibility(View.VISIBLE);
		}
		
		if (!slidingDrawer.isOpened()) {
			slidingDrawer.open();
			
		}
		bottom_sldingDrawer.close();
		bottom_sldingDrawer.setVisibility(View.INVISIBLE);
		
		name_TextView.setText(topInfoEntry.getLocation_name());
		reason_TextView.setText(topInfoEntry.getHotspot_reason());
		distance_TextView.setText(topInfoEntry.getDistance()+"");
		
	}
	

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		context=(SherlockFragmentActivity)activity;
	}

	@Override
	public void onMapReady(GoogleMap map) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		Log.v("Test", "mapfragment getview");
		return super.getView();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		((SmartTravelApplication)getActivity().getApplication()).inject(this);
		bus.register(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
		bus.register(this);
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
		Log.v("Test", "mapfragment onpause");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
		super.onStart();
		FlurryAgent.onStartSession(getActivity(), getString(R.string.Flurry_API_Key));
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
		Log.v("Test", "mapfragment onresume");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
	
		super.onStop();
		
		
	//	locationManager.removeUpdates(this);
		FlurryAgent.onEndSession(getActivity());
		
	}

	
	@Subscribe
	public void OnDrivingModeEvent(DrivingModeEvent event){
		driving_modeLayout.setVisibility(
				View.VISIBLE);
		
		((SherlockFragmentActivity) context)
		.getSupportActionBar().hide();
		if (slidingDrawer!=null && slidingDrawer.isOpened()) {
			slidingDrawer.close();
			slidingDrawer.setVisibility(View.INVISIBLE);
		}
		if (bottom_sldingDrawer!=null &&bottom_sldingDrawer.isOpened()) {
			bottom_sldingDrawer.close();
			bottom_sldingDrawer.setVisibility(View.INVISIBLE);
		}
		
	}
	
	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		String location_name=marker.getTitle();
		DataHandler dHandler=new DataHandler();
		
		UpdateBottomInfoUI(dHandler.getBottomInfoItemByLocaitonName(context, location_name));
		return true;
	}
	
	
	
	public void UpdateBottomInfoUI(ArrayList<BottomInfoItem> arrayList_items){
		
		slidingDrawer.close();
		if (arrayList_items.size()>0) { 
			bottom_location_name_textview.setText(arrayList_items.get(0).getLocation_name());
			CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(arrayList_items.get(0).getLocationLatLng(), 15);
			googleMap.animateCamera(cameraUpdate);
			if (arrayList_items.get(0).getType().contains("SCHOOL ZONE")) {
				bottom_list.setVisibility(View.INVISIBLE);
				school_zone_text.setVisibility(View.VISIBLE);
				bottom_linearLayout.setVisibility(View.INVISIBLE);
				if (arrayList_items.get(0).getSchoolDay()) {
					school_zone_text.setText("School Day (08:00-16:30) \n 30 km/h Speed Limit");
				}else {
					school_zone_text.setText("No School Day");
				}
			}else {
				bottom_linearLayout.setVisibility(View.VISIBLE);
				school_zone_text.setVisibility(View.GONE);
				bottom_list.setVisibility(View.VISIBLE);
				bottomListAdapter=new BottomListAdapter(context, arrayList_items);
				bottom_list.setAdapter(bottomListAdapter);
			}
			
			bottom_sldingDrawer.open();
			bottom_sldingDrawer.setVisibility(View.VISIBLE);
		}else {
			Toast.makeText(getActivity(), "no reason available for this location", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.slideHandle:
			if (slidingDrawer.isOpened()) {
				slidingDrawer.close();
			}else {
				slidingDrawer.open();
				bottom_sldingDrawer.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.bottom_slideHandle:
			if (bottom_sldingDrawer.isOpened()) {
				bottom_sldingDrawer.close();
			}else {
				bottom_sldingDrawer.open();
			}
			break;
		case R.id.not_driving_button:
			preferencesEditor.putBoolean(getString(R.string.preferences_is_driving), false);
			preferencesEditor.commit();
			driving_modeLayout.setVisibility(View.INVISIBLE);
			((SherlockFragmentActivity) context).getSupportActionBar().show();
			bus.post(new DrivingModeDismissEvent());
			
			break;
		default:
			break;
		}
		
	}


	@Override
	public void onCameraChange(CameraPosition position) {
		// TODO Auto-generated method stub
		if (position.zoom >14) {
		//	Log.v("STTest", "zoom is larger than 14");
			if (id==1 && school_Segments_IndicatorBoolean) {
				schoolZoneHandler.addSchoolZoneSegments();
				school_Segments_IndicatorBoolean=false;
			}
		}else {
			if (schoolZoneHandler!=null)
			schoolZoneHandler.removeSchoolZoneSegments();
			school_Segments_IndicatorBoolean=true;
		}
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.radiobutton_schoolzone:
			schoolZoneHandler.addSchoolZoneMarkers();
			collisionHandler.removeCollisionLayer();
			((BaseActivity)getActivity()).getSupportActionBar().setTitle("School Zones");
			break;
		case R.id.radiobutton_collision:
			collisionHandler.removeCollisionLayer();
			collisionHandler.addCollisionLayer();
			schoolZoneHandler.removeSchoolZoneMarkers();
			schoolZoneHandler.removeSchoolZoneSegments();
			school_Segments_IndicatorBoolean=true;
			((BaseActivity)getActivity()).getSupportActionBar().setTitle("High Collision Locations");
		default:
			break;
		}
	}
	
	@Subscribe
	public void SlidingDrawerUpdateUI(SlidingDrawerUpdateEvent event){
		if (slidingDrawer.isOpened()) {
			slidingDrawer.close();
			slidingDrawer.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		bus.post(new MapReadyEvent());
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				schoolZoneHandler=new SchoolZoneHandler(googleMap, context);
				collisionHandler=new CollisionHandler(googleMap, context);
				handler.sendEmptyMessage(0);
			}
		});
		thread.start();
		
	}
	
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			//radioGroup_map_layer.setVisibility(View.VISIBLE);
			if (id==1) schoolZoneHandler.addSchoolZoneMarkers();
			else collisionHandler.addCollisionLayer();
		}
		
	};
	
}
