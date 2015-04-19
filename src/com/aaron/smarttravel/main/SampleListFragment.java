package com.aaron.smarttravel.main;

import java.util.ArrayList;

import com.aaron.smarttravel.data.HotspotParse;
import com.aaron.smarttravel.drawer.LeftDrawerListAdapter;
import com.aaron.smarttravel.utilities.HotSpotEntry;
import com.aaron.smarttravel.utilities.NavDrawerItem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	public Context context;
	TextView collision_countTextView;
	TextView VRU_countTextView;
	private RelativeLayout collision_relativeLayout, VRU_relativelayout;
	View collision_dividerView,VRU_dividerView;
	
	ArrayList<NavDrawerItem> navDrawerItems=new ArrayList<NavDrawerItem>();
	public ListView listView;
	ArrayList<HotSpotEntry> intersection_arraylist,midblock_arraylist,VRU_arraylist;
	LeftDrawerListAdapter leftDrawerListAdapte;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view= inflater.inflate(R.layout.left_drawer, null);
		collision_countTextView=(TextView) view.findViewById(R.id.left_drawer_collosion_count);
		VRU_countTextView=(TextView) view.findViewById(R.id.left_drawer_VRU_count);
		listView=(ListView)view.findViewById(android.R.id.list);
		collision_relativeLayout=(RelativeLayout)view.findViewById(R.id.left_drawer_button_collision);
		VRU_relativelayout=(RelativeLayout) view.findViewById(R.id.left_drawer_button_VRU);
		collision_dividerView=(View) view.findViewById(R.id.left_drawer_collision_divider);
		VRU_dividerView=(View) view.findViewById(R.id.left_drawer_VRU_divider);
		
		collision_relativeLayout.setOnClickListener(this); 
		collision_relativeLayout.setClickable(false);
		VRU_relativelayout.setOnClickListener(this);
		VRU_relativelayout.setClickable(true);
		
		collision_countTextView.setText("20");
		VRU_countTextView.setText("22");

		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	//	SampleAdapter adapter = new SampleAdapter(getActivity());
		
	//	String[] names_hotspot=context.getResources().getStringArray(R.array.nav_drawer_items);
		
		HotspotParse my_HotspotParse=new HotspotParse();
		 intersection_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("intersection_top_10.json", getActivity()));
		 midblock_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("midblock_top_10.json", getActivity()));
		 VRU_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("VRU_top_x.json", getActivity()));
		
		addItemToList(intersection_arraylist, "Intersection");
		addItemToList(midblock_arraylist, "Midblock");
		//addItemToList(VRU_arraylist, "Midblock");
		leftDrawerListAdapte=new LeftDrawerListAdapter(context, navDrawerItems);
		setListAdapter(leftDrawerListAdapte);
		
		
	}
	public void addItemToList(ArrayList<HotSpotEntry> hotspot_array,String type_string){
		
		for (int i = 0; i < hotspot_array.size(); i++) {
			//	adapter.add(new SampleItem(names_hotspot[i], android.R.drawable.btn_star));
				NavDrawerItem navDrawerItem=new NavDrawerItem();
				navDrawerItem.setType_hotspot(type_string);
				navDrawerItem.setName_hotspot(hotspot_array.get(i).getName());
				navDrawerItem.setCount_collisions(hotspot_array.get(i).getCollision_count());
				navDrawerItems.add(navDrawerItem);
			}
	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		context=activity;
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		Fragment newContent = null;
		
		/*switch (position) {
		case 0:
			newContent = new Fragment1();
			break;
		case 1:
			newContent = new Fragment2();
			break;
		case 2:
			newContent = new Fragment3();
			break;
		case 3:
			newContent = new Fragment4();
			break;

		}*/
		if (newContent != null)
			switchFragment(newContent);
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_drawer_button_collision:
			collision_relativeLayout.setClickable(false);
			VRU_relativelayout.setClickable(true);
			collision_dividerView.setBackgroundResource(R.color.horizontal_divider_bg);
			VRU_dividerView.setBackgroundResource(R.color.vertical_divider_bg);
			
			navDrawerItems.clear();
			addItemToList(intersection_arraylist, "Intersection");
			addItemToList(midblock_arraylist, "Midblock");
			//addItemToList(VRU_arraylist, "Midblock");
			leftDrawerListAdapte=new LeftDrawerListAdapter(context, navDrawerItems);
			setListAdapter(leftDrawerListAdapte);
			
			break;
		case R.id.left_drawer_button_VRU:
			collision_relativeLayout.setClickable(true);
			VRU_relativelayout.setClickable(false);
			collision_dividerView.setBackgroundResource(R.color.vertical_divider_bg);
			VRU_dividerView.setBackgroundResource(R.color.horizontal_divider_bg);
			
			navDrawerItems.clear();
			addItemToList(VRU_arraylist, "Midblock");
			leftDrawerListAdapte=new LeftDrawerListAdapter(context, navDrawerItems);
			setListAdapter(leftDrawerListAdapte);
			break;
		default:
			break;
		}
		
	}

}
