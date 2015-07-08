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
		
		int[] count_bgs=new int[]{
				R.drawable.b1,R.drawable.b2,R.drawable.b3,R.drawable.b4,R.drawable.b5,
				R.drawable.b6,R.drawable.b7,R.drawable.b8,R.drawable.b9,R.drawable.b10
		};
		int collision_count=navDrawerItems.get(position).getCount_collisions();
		int count_rank=collision_count*10/86;
		if (count_rank>9) {
			count_rank=9;
		}
		count_collision_hotspot.setBackgroundResource(count_bgs[count_rank]);
		
		type_hotspot.setText(navDrawerItems.get(position).getType_hotspot());
		
		if ((navDrawerItems.get(position).getType_hotspot()).startsWith("SCHOOL ZONE")) {
			count_collision_hotspot.setVisibility(View.INVISIBLE);
		}else {
			count_collision_hotspot.setText(Integer.toString(collision_count));
		}
		name_hotspot.setText(navDrawerItems.get(position).getName_hotspot());
		
		
		
		
		return convertView;
	}

}
