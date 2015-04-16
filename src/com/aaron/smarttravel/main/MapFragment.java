package com.aaron.smarttravel.main;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment{

	MapView mapView;
	private GoogleMap googleMap;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.main_map, container, false);
		mapView=(MapView) view.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		mapView.onResume();
		/*
		View location_buttonView=((View)mapView.findViewById(1).getParent()).findViewById(2);
		
		location_buttonView.setBackgroundResource(R.drawable.location_icon);
		
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(54, 66);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
		params.setMargins(80, 0, 0, 80);
		location_buttonView.setLayoutParams(params);*/
		
		
		try {
			MapsInitializer.initialize(getActivity().getApplicationContext());
		} catch (Exception e) {
			// TODO: handle exception
		}
		googleMap=mapView.getMap();
		CameraUpdate cameraUpdate=CameraUpdateFactory.newLatLngZoom(new LatLng(53.539150,  -113.496867), 13);
		googleMap.animateCamera(cameraUpdate);
		googleMap.setMyLocationEnabled(true);
	//	googleMap.setTrafficEnabled(true);
		
		googleMap.getUiSettings().setZoomControlsEnabled(true);
		
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return super.getView();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
	}
		
	

}
