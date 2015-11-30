package com.aaron.smarttravel.drawer;

import java.util.ArrayList;

import com.aaron.smarttravel.main.R;
import com.aaron.smarttravel.utilities.NavDrawerItem;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SchoolListViewAdapter extends BaseAdapter{
	private ArrayList<NavDrawerItem> schooList;
	private Context context;
	public SchoolListViewAdapter(ArrayList<NavDrawerItem> schooList,Context context){
		this.schooList=schooList;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return schooList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return schooList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 NavDrawerItem childDrawerItem=(NavDrawerItem)schooList.get(position);
		if (convertView==null) {
			LayoutInflater mInflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView=mInflater.inflate(R.layout.drawer_list_item, null);
		}
		TextView type_hotspot=(TextView)convertView.findViewById(R.id.hotspot_type);
		TextView name_hotspot=(TextView)convertView.findViewById(R.id.hotspot_name);
		TextView count_collision_hotspot=(TextView) convertView.findViewById(R.id.hotspot_collision_count);

		type_hotspot.setText(childDrawerItem.getType_hotspot());
		count_collision_hotspot.setVisibility(View.INVISIBLE);
		
		name_hotspot.setText(childDrawerItem.getName_hotspot());
		
		return convertView;
		
	}

}
