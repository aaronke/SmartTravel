package com.aaron.smarttravel.main;

import java.util.ArrayList;

import com.aaron.smarttravel.database.HotspotsDbHelper;
import com.aaron.smarttravel.drawer.LeftDrawerListAdapter;
import com.aaron.smarttravel.utilities.BottomInfoItem;
import com.aaron.smarttravel.utilities.DataHandler;
import com.aaron.smarttravel.utilities.HotSpotEntry;
import com.aaron.smarttravel.utilities.NavDrawerItem;
import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SampleListFragment extends ListFragment implements View.OnClickListener{
	//public String[] menus = { "Fragment1", "Fragment2", "Fragment3",
	//		"Fragment4" };
	OnSampleListFragmentListener mcallback;
	public Context context;
	TextView intersection_count_textView,mid_avenue_count_textView,mid_street_count_textView,school_zone_count_textView;
	private RelativeLayout intersection_relativeLayout, Mid_Avenue_relativelayout,mid_Street_relativeLayout,school_zone_relativeLayout;
	View intersection_dividerView,mid_avenue_dividerView,mid_street_dividerView,school_zone_dividerView;
	
	ArrayList<NavDrawerItem> navDrawerItems_school=new ArrayList<NavDrawerItem>();
	ArrayList<NavDrawerItem> navDrawerItems_intersection=new ArrayList<NavDrawerItem>();
	ArrayList<NavDrawerItem> navDrawerItems_mid_avnue=new ArrayList<NavDrawerItem>();
	ArrayList<NavDrawerItem> navDrawerItems_mid_street=new ArrayList<NavDrawerItem>();
	//public ListView listView;
	ArrayList<HotSpotEntry> intersection_arraylist,midblock_arraylist,VRU_arraylist;
	LeftDrawerListAdapter leftDrawerListAdapte;
	private static final String SCHOOL_ZONE="SCHOOL ZONE";
	private static final String INTERSECTION="INTERSECTION";
	private static final String MID_AVENUE="MID AVENUE";
	private static final String MID_STREET="MID STREET";
	HotspotsDbHelper dbHelper;
	View view;
	private SharedPreferences sharedPreferences;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		sharedPreferences=context.getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		view= inflater.inflate(R.layout.left_drawer, null);
		
		intersection_relativeLayout=(RelativeLayout)view.findViewById(R.id.left_drawer_button_intersection);
		Mid_Avenue_relativelayout=(RelativeLayout)view.findViewById(R.id.left_drawer_button_Mid_Avenue);
		mid_Street_relativeLayout=(RelativeLayout)view.findViewById(R.id.left_drawer_button_Mid_Street);
		school_zone_relativeLayout=(RelativeLayout)view.findViewById(R.id.left_drawer_button_School_Zone);
		
		intersection_dividerView=view.findViewById(R.id.left_drawer_intersection_divider);
		mid_avenue_dividerView=view.findViewById(R.id.left_drawer_Mid_Avenue_divider);
		mid_street_dividerView=view.findViewById(R.id.left_drawer_Mid_Street_divider);
		school_zone_dividerView=view.findViewById(R.id.left_drawer_School_Zone_divider);
		
		intersection_count_textView=(TextView)view.findViewById(R.id.left_drawer_intersection_count);
		mid_avenue_count_textView=(TextView)view.findViewById(R.id.left_drawer_Mid_Avenue_count);
		mid_street_count_textView=(TextView)view.findViewById(R.id.left_drawer_Mid_Street_count);
		school_zone_count_textView=(TextView)view.findViewById(R.id.left_drawer_School_Zone_count);
		
		intersection_relativeLayout.setOnClickListener(this); 
		Mid_Avenue_relativelayout.setOnClickListener(this);
		mid_Street_relativeLayout.setOnClickListener(this);
		school_zone_relativeLayout.setOnClickListener(this);
		
		UpdateUI(R.id.left_drawer_button_intersection);
		
		
		return view;
	}
	
	public void UpdateUI(int layout_id){
		
		intersection_dividerView.setBackgroundResource(R.color.vertical_divider_bg);
		mid_avenue_dividerView.setBackgroundResource(R.color.vertical_divider_bg);
		mid_street_dividerView.setBackgroundResource(R.color.vertical_divider_bg);
		school_zone_dividerView.setBackgroundResource(R.color.vertical_divider_bg);
		intersection_relativeLayout.setClickable(true);
		Mid_Avenue_relativelayout.setClickable(true);
		mid_Street_relativeLayout.setClickable(true);
		school_zone_relativeLayout.setClickable(true);
		
		RelativeLayout temp_layout=(RelativeLayout)view.findViewById(layout_id);
		View tempView=temp_layout.getChildAt(2);
		temp_layout.setClickable(false);
		tempView.setBackgroundResource(R.color.horizontal_divider_bg);
		
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	//	SampleAdapter adapter = new SampleAdapter(getActivity());
		
	//	String[] names_hotspot=context.getResources().getStringArray(R.array.nav_drawer_items);
		
		/*HotspotParse my_HotspotParse=new HotspotParse();
		 intersection_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("intersection_top_10.json", getActivity()),"Intersection");
		 midblock_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("midblock_top_10.json", getActivity()),"Midblock");
		 VRU_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("VRU_top_x.json", getActivity()),"VRU");*/
		
	/*	addItemToList(intersection_arraylist, "Intersection");
		addItemToList(midblock_arraylist, "Midblock");*/
		dbHelper=new HotspotsDbHelper(context);
		navDrawerItems_school=getItemsFromDatabase(SCHOOL_ZONE);
		navDrawerItems_intersection=getItemsFromDatabase(INTERSECTION);
		navDrawerItems_mid_avnue=getItemsFromDatabase(MID_AVENUE);
		navDrawerItems_mid_street=getItemsFromDatabase(MID_STREET);
		leftDrawerListAdapte=new LeftDrawerListAdapter(context, navDrawerItems_intersection);
		setListAdapter(leftDrawerListAdapte);
		intersection_count_textView.setText(navDrawerItems_intersection.size()+"");
		mid_avenue_count_textView.setText(navDrawerItems_mid_avnue.size()+"");
		mid_street_count_textView.setText(navDrawerItems_mid_street.size()+"");
		school_zone_count_textView.setText(navDrawerItems_school.size()+"");
		
	}
/*	public void addItemToList(ArrayList<HotSpotEntry> hotspot_array,String type_string){
		
		for (int i = 0; i < hotspot_array.size(); i++) {
			//	adapter.add(new SampleItem(names_hotspot[i], android.R.drawable.btn_star));
				NavDrawerItem navDrawerItem=new NavDrawerItem();
				navDrawerItem.setType_hotspot(type_string);
				navDrawerItem.setName_hotspot(hotspot_array.get(i).getName());
				navDrawerItem.setCount_collisions(hotspot_array.get(i).getCollision_count());
				navDrawerItems.add(navDrawerItem);
			}
	}*/

	public ArrayList<NavDrawerItem> getItemsFromDatabase(String typeString){
		ArrayList<NavDrawerItem> tempArrayList=new ArrayList<NavDrawerItem>();
		Boolean is_at_shanghai=sharedPreferences.getBoolean(getString(R.string.preferences_is_at_shanghai), false);
		tempArrayList=dbHelper.getAllObjectsByType(typeString,is_at_shanghai);	
		return tempArrayList;
	}
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		context=activity;
		
		try {
			mcallback=(OnSampleListFragmentListener) activity;
		} catch (Exception e) {
			// TODO: handle exception
			throw new ClassCastException(activity.toString()+"must implement OnsampleListFragmentListener");
		}
	}
	
	public interface OnSampleListFragmentListener{
		public void onitemselected(ArrayList<BottomInfoItem> arrayList);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		TextView name_textView=(TextView)v.findViewById(R.id.hotspot_name);
		String location_name=(String) name_textView.getText();
		DataHandler dHandler=new DataHandler();
		mcallback.onitemselected( dHandler.getBottomInfoItemByLocaitonName(context, location_name));
		
	}

	/*private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		}

	}*/
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ArrayList<NavDrawerItem> temp_navitems=new ArrayList<NavDrawerItem>();
		switch (v.getId()) {
		case R.id.left_drawer_button_intersection:
				temp_navitems=navDrawerItems_intersection;
			break;
		case R.id.left_drawer_button_Mid_Avenue:
				temp_navitems=navDrawerItems_mid_avnue;
			break;
		case R.id.left_drawer_button_Mid_Street:
				temp_navitems=navDrawerItems_mid_street;
			break;
		case R.id.left_drawer_button_School_Zone:
				temp_navitems=navDrawerItems_school;
			break;
		default:
			
			break;
		}
		
		leftDrawerListAdapte=new LeftDrawerListAdapter(context, temp_navitems);
		setListAdapter(leftDrawerListAdapte);
		UpdateUI(v.getId());
		
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

	
}
