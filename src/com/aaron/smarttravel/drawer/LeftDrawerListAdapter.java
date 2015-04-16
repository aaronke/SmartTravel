package com.aaron.smarttravel.drawer;

import java.util.ArrayList;

import com.aaron.smarttravel.main.R;
import com.aaron.smarttravel.utilities.NavDrawerItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LeftDrawerListAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	
	public LeftDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		
		this.context=context;
		this.navDrawerItems=navDrawerItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView==null) {
			LayoutInflater mInflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView=mInflater.inflate(R.layout.drawer_list_item, null);
		}
		TextView type_hotspot=(TextView)convertView.findViewById(R.id.hotspot_type);
		TextView name_hotspot=(TextView)convertView.findViewById(R.id.hotspot_name);
		TextView count_collision_hotspot=(TextView) convertView.findViewById(R.id.hotspot_collision_count);
		
		
		count_collision_hotspot.setText(Integer.toString(navDrawerItems.get(position).getCount_collisions()));
		type_hotspot.setText(navDrawerItems.get(position).getType_hotspot());
		name_hotspot.setText(navDrawerItems.get(position).getName_hotspot());
		
		return convertView;
	}

}
