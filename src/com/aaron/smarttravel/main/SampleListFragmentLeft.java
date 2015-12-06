package com.aaron.smarttravel.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.inject.Inject;

import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.aaron.smarttravel.drawer.ExpandableListViewAdapter;
import com.aaron.smarttravel.drawer.SchoolListViewAdapter;
import com.aaron.smarttravel.injection.SmartTravelApplication;
import com.aaron.smarttravel.injection.event.MapReadyEvent;
import com.aaron.smarttravel.utilities.BottomInfoItem;
import com.aaron.smarttravel.utilities.DataHandler;
import com.aaron.smarttravel.utilities.ListHeaderItem;
import com.aaron.smarttravel.utilities.NavDrawerItem;
import com.aaron.smarttravel.utilities.SchoolZoneObject;
import com.flurry.android.FlurryAgent;
import com.flurry.sdk.hs;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;

public class SampleListFragmentLeft extends Fragment implements OnChildClickListener,OnItemClickListener{
	
	
	@Inject
	Bus bus;
	OnSampleListFragmentLeftListener mcallback;
	public Context context;
	private View view;
	private HotspotsDbHelper dbHelper;
	private SharedPreferences sharedPreferences;
	private ExpandableListViewAdapter listViewAdapter;
	private ExpandableListView listView;
	private HashMap<String,ArrayList<Integer>> listDataHeader;
	private HashMap<String, ArrayList<NavDrawerItem>> listDataChild;
	private ArrayList<ListHeaderItem> headerItems=new ArrayList<>();
	private ListView schooListView;
	private SchoolListViewAdapter schoolListViewAdapter;
	private ArrayList<NavDrawerItem> schooListNavDrawerItems;
	private int id;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		id=getArguments().getInt("ID");
		Log.v("life", "samplelist ID"+id);
		sharedPreferences=context.getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		view= inflater.inflate(R.layout.left_menu, null);
		
		listView=(ExpandableListView)view.findViewById(R.id.expandableListView);
		schooListView=(ListView)view.findViewById(R.id.school_listview);
		
		return view;
	}
	private HashMap<String, Integer> initial(){
		HashMap<String, Integer> headerTitleToImgMap=new HashMap<>();
		headerTitleToImgMap.put("PEDESTRIANS", R.drawable.header_pedstrain);
		headerTitleToImgMap.put("MOTORCYCLISTS", R.drawable.header_motrocyclist);
		headerTitleToImgMap.put("CYCLISTS", R.drawable.header_cyclist);
		headerTitleToImgMap.put("FOLLOWED TOO CLOSELY", R.drawable.header_followed_too_closely);
		headerTitleToImgMap.put("LEFT TURN", R.drawable.header_left_turn);
		headerTitleToImgMap.put("RED-LIGHT RUNNING", R.drawable.header_red_light_running);
		headerTitleToImgMap.put("STOP SIGN VIOLATION", R.drawable.header_stop_sign_vilation);
		headerTitleToImgMap.put("IMPROPER CHANGE LANE", R.drawable.header_imporper_lane_change);
		headerTitleToImgMap.put("RAN OFF ROAD", R.drawable.header_ran_off_road);
		headerTitleToImgMap.put("TOP LOCATIONS BY ALL CAUSES", R.drawable.header_top_collision);
		headerTitleToImgMap.put("MORNING RUSH HOUR", R.drawable.header_morning_rush_hour);
		headerTitleToImgMap.put("AFTERNOON RUSH HOUR", R.drawable.header_afternonn_rush_hour);
		headerTitleToImgMap.put("WEEKEND EARLY MORNING", R.drawable.header_weekend_early);	
		return headerTitleToImgMap;
	}
	private ArrayList<String> categoryOrderFormat(){
		ArrayList<String> arrayList=new ArrayList<>();
		arrayList.add("PEDESTRIANS");
		arrayList.add("MOTORCYCLISTS");
		arrayList.add("CYCLISTS");
		arrayList.add("FOLLOWED TOO CLOSELY");
		arrayList.add("LEFT TURN");
		arrayList.add("RED-LIGHT RUNNING");
		arrayList.add("STOP SIGN VIOLATION");
		arrayList.add("IMPROPER CHANGE LANE");
		arrayList.add("MORNING RUSH HOUR");
		arrayList.add("AFTERNOON RUSH HOUR");
		arrayList.add("WEEKEND EARLY MORNING");
		arrayList.add("TOP LOCATIONS BY ALL CAUSES");
		return arrayList;
	}

	private void loadData() {
		// TODO Auto-generated method stub
		dbHelper=new HotspotsDbHelper(context);
		listDataHeader=dbHelper.getReasonList();
		listDataChild=new HashMap<String,ArrayList<NavDrawerItem>>();
		Boolean is_at_shanghai=sharedPreferences.getBoolean(context.getString(R.string.preferences_is_at_shanghai), false);	
		HashMap<String, Integer> headerTxtToImgMap=initial();
		ArrayList<String> orderFormatArrayList=categoryOrderFormat();
		for (String categoryString:orderFormatArrayList) {
			ListHeaderItem headerItem=new ListHeaderItem();
			headerItem.setHeaser_txt(categoryString);
			headerItem.setHeader_img(headerTxtToImgMap.get(categoryString));
			ArrayList<Integer> arrayList=listDataHeader.get(categoryString);
			ArrayList<NavDrawerItem> childArrayList=new ArrayList<>();
			for (int j = 0; arrayList!=null && j < arrayList.size(); j++) {
				childArrayList.addAll(dbHelper.getAllObjectByReasonId(arrayList.get(j), is_at_shanghai));
			}
			sortNavDrawerItems(childArrayList);
			listDataChild.put(categoryString, childArrayList);

			headerItem.setCount(getCategoryTotalCollisionCount(listDataChild.get(categoryString)));
			headerItems.add(headerItem);
		}
		
		schooListNavDrawerItems=getALLSchoolZones();
	}
	private void sortNavDrawerItems(ArrayList<NavDrawerItem> arrayList){
		Collections.sort(arrayList, new Comparator<NavDrawerItem>() {
			@Override
			public int compare(NavDrawerItem lhs,NavDrawerItem  rhs) {
				// TODO Auto-generated method stub
				String[] lhsName=lhs.getName_hotspot().split("\\s+");
				String[] rhsName=rhs.getName_hotspot().split("\\s+");
				String lhsString=dealTheAvenueSort( lhsName, lhs);
				String rhsString=dealTheAvenueSort(rhsName, rhs);
				
				return lhsString.compareToIgnoreCase(rhsString);
			}
		});
	}

	private String dealTheAvenueSort(String[] strings,NavDrawerItem navDrawerItem){
		String resultString = navDrawerItem.getName_hotspot();
		strings[0].replace("a", "");
		if (strings[0].charAt(0)>='0' && strings[0].charAt(0)<='9') {
			if(strings[0].length()==1) resultString="00"+navDrawerItem.getName_hotspot();
			if(strings[0].length()==2) resultString="0"+navDrawerItem.getName_hotspot();
		}
		return resultString;
	}
	private int getCategoryTotalCollisionCount(ArrayList<NavDrawerItem> navDrawerItems){
		int count=0;
		for (int i = 0; i < navDrawerItems.size(); i++) {
			count+=navDrawerItems.get(i).getCount_collisions();
		}
		return count;
	}
	private ArrayList<NavDrawerItem> getALLSchoolZones(){
		ArrayList<NavDrawerItem> arrayList=new ArrayList<>();
		ArrayList<SchoolZoneObject> schoolZoneObjects=dbHelper.getSchoolZoneObjects();
		for (int i = 0; i < schoolZoneObjects.size(); i++) {
			NavDrawerItem item=new NavDrawerItem();
			item.setName_hotspot(schoolZoneObjects.get(i).getSchool_name());
			item.setType_hotspot(schoolZoneObjects.get(i).getSchool_type());
			arrayList.add(item);
		}
		return arrayList;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}

   
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		if (id==1) {
			listView.setVisibility(View.GONE);
			schooListView.setVisibility(View.VISIBLE);
		}else {
			listView.setVisibility(View.VISIBLE);
			schooListView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		((SmartTravelApplication)getActivity().getApplication()).inject(this);
		bus.register(this);
	}
	Handler uiHandler=new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {

			listViewAdapter=new ExpandableListViewAdapter(context, headerItems, listDataChild);
			listView.setAdapter(listViewAdapter);
			listView.setOnChildClickListener(SampleListFragmentLeft.this);
			schoolListViewAdapter=new SchoolListViewAdapter(schooListNavDrawerItems, getActivity());
			schooListView.setAdapter(schoolListViewAdapter);
			schooListView.setOnItemClickListener(SampleListFragmentLeft.this);
		}
	};

	@Subscribe
	public void onMapReadyEvent(MapReadyEvent event){
		Thread threadLoadData=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				loadData();
				uiHandler.sendEmptyMessage(1);
			}
		});
		
		threadLoadData.start();
		
		
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
		if (dbHelper!=null) dbHelper.close();
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
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		// TODO Auto-generated method stub
		TextView name_textView=(TextView)v.findViewById(R.id.hotspot_name);
		String location_name=(String) name_textView.getText();
		DataHandler dHandler=new DataHandler();
		mcallback.onitemselected( dHandler.getBottomInfoItemByLocaitonName(context, location_name));
	}

}
