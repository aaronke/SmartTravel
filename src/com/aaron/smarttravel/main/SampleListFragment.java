package com.aaron.smarttravel.main;

import java.util.ArrayList;

import com.aaron.smarttravel.drawer.LeftDrawerListAdapter;
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
		View view= inflater.inflate(R.layout.list, null);
	//	listView=(ListView) view.findViewById(R.id.list);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	//	SampleAdapter adapter = new SampleAdapter(getActivity());
		
		String[] names_hotspot=context.getResources().getStringArray(R.array.nav_drawer_items);
		for (int i = 0; i < names_hotspot.length; i++) {
		//	adapter.add(new SampleItem(names_hotspot[i], android.R.drawable.btn_star));
			NavDrawerItem navDrawerItem=new NavDrawerItem();
			navDrawerItem.setType_hotspot("Intersection");
			navDrawerItem.setName_hotspot(names_hotspot[i]);
			navDrawerItem.setCount_collisions(14);
			navDrawerItems.add(navDrawerItem);
		}
		LeftDrawerListAdapter leftDrawerListAdapte=new LeftDrawerListAdapter(context, navDrawerItems);
		setListAdapter(leftDrawerListAdapte);
		

	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		context=activity;
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		Fragment newContent = null;
		
		switch (position) {
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

		}
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
