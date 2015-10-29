package com.aaron.smarttravel.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.aaron.smarttravel.drawer.ExpandableListViewAdapter;
import com.aaron.smarttravel.utilities.BottomInfoItem;
import com.aaron.smarttravel.utilities.DataHandler;
import com.aaron.smarttravel.utilities.NavDrawerItem;
import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;

public class SampleListFragmentLeft extends Fragment implements OnChildClickListener{
	
	OnSampleListFragmentLeftListener mcallback;
	public Context context;
	private View view;
	private HotspotsDbHelper dbHelper;
	private SharedPreferences sharedPreferences;
	private ExpandableListViewAdapter listViewAdapter;
	private ExpandableListView listView;
	private ArrayList<String> listDataHeader;
	private HashMap<String, ArrayList<NavDrawerItem>> listDataChild;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		sharedPreferences=context.getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		view= inflater.inflate(R.layout.left_menu, null);
		
		listView=(ExpandableListView)view.findViewById(R.id.expandableListView);
		
		return view;
	}
	

	private void loadData() {
		// TODO Auto-generated method stub
		
		listDataHeader=dbHelper.getReasonList();
		listDataChild=new HashMap<String,ArrayList<NavDrawerItem>>();
		Boolean is_at_shanghai=sharedPreferences.getBoolean(getString(R.string.preferences_is_at_shanghai), false);	
		for (int i = 0; i < listDataHeader.size(); i++) {
			
			listDataChild.put(listDataHeader.get(i), dbHelper.getAllObjectByReasonId(i+1, is_at_shanghai));
		}
	}


	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dbHelper=new HotspotsDbHelper(context);
		loadData();
		listViewAdapter=new ExpandableListViewAdapter(context, listDataHeader, listDataChild);
		listView.setAdapter(listViewAdapter);
		listView.setOnChildClickListener(this);
	}

   
	public ArrayList<NavDrawerItem> getItemsFromDatabase(String typeString){
		Boolean is_at_shanghai=sharedPreferences.getBoolean(getString(R.string.preferences_is_at_shanghai), false);	
		return dbHelper.getAllObjectsByType(typeString,is_at_shanghai);
	}
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		context=activity;
		
		try {
			mcallback=(OnSampleListFragmentLeftListener) activity;
		} catch (Exception e) {
			// TODO: handle exception
			throw new ClassCastException(activity.toString()+"must implement OnsampleListFragmentListener");
		}
	}
	
	public interface OnSampleListFragmentLeftListener{
		public void onitemselected(ArrayList<BottomInfoItem> arrayList);
	}
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbHelper.close();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		FlurryAgent.onStartSession(getActivity(), getString(R.string.Flurry_API_Key));
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		FlurryAgent.onEndSession(getActivity());
	}


	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		
		TextView name_textView=(TextView)v.findViewById(R.id.hotspot_name);
		String location_name=(String) name_textView.getText();
		DataHandler dHandler=new DataHandler();
		mcallback.onitemselected( dHandler.getBottomInfoItemByLocaitonName(context, location_name));
		
		
		return true;
	}

}
