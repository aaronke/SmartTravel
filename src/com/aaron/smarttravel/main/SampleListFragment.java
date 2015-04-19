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

public class SampleListFragment extends ListFragment {
	//public String[] menus = { "Fragment1", "Fragment2", "Fragment3",
	//		"Fragment4" };
	public Context context;
	
	ArrayList<NavDrawerItem> navDrawerItems=new ArrayList<NavDrawerItem>();
	public ListView listView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.left_drawer, null);
	//	listView=(ListView) view.findViewById(R.id.list);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	//	SampleAdapter adapter = new SampleAdapter(getActivity());
		
	//	String[] names_hotspot=context.getResources().getStringArray(R.array.nav_drawer_items);
		
		HotspotParse my_HotspotParse=new HotspotParse();
		ArrayList<HotSpotEntry> intersection_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("intersection_top_10.json", getActivity()));
		ArrayList<HotSpotEntry> midblock_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("midblock_top_10.json", getActivity()));
		ArrayList<HotSpotEntry> VRU_arraylist=my_HotspotParse.getHotspotEntries(my_HotspotParse.loadJsonString("VRU_top_x.json", getActivity()));
		
		addItemToList(intersection_arraylist, "Intersection");
		addItemToList(midblock_arraylist, "Midblock");
		addItemToList(VRU_arraylist, "Midblock");
		LeftDrawerListAdapter leftDrawerListAdapte=new LeftDrawerListAdapter(context, navDrawerItems);
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
}
