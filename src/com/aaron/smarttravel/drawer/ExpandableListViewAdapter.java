package com.aaron.smarttravel.drawer;

import java.util.ArrayList;
import java.util.HashMap;

import com.aaron.smarttravel.main.R;
import com.aaron.smarttravel.utilities.NavDrawerItem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter{

	private Context context;
	private ArrayList<String> listDataHeader;
	private HashMap<String, ArrayList<NavDrawerItem>> listDatachild;
	
	
	public ExpandableListViewAdapter(Context context,ArrayList<String> listDataHeader,HashMap<String, ArrayList<NavDrawerItem>> listDataChild){
		this.context=context;
		this.listDataHeader=listDataHeader;
		this.listDatachild=listDataChild;
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return this.listDatachild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		final NavDrawerItem childDrawerItem=(NavDrawerItem)getChild(groupPosition, childPosition);
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
		int collision_count=childDrawerItem.getCount_collisions();
		int count_rank=collision_count*10/86;
		if (count_rank>9) {
			count_rank=9;
		}
		count_collision_hotspot.setBackgroundResource(count_bgs[count_rank]);
		
		type_hotspot.setText(childDrawerItem.getType_hotspot());
		
		if ((childDrawerItem.getType_hotspot()).startsWith("SCHOOL ZONE")) {
			count_collision_hotspot.setVisibility(View.INVISIBLE);
		}else {
			count_collision_hotspot.setText(Integer.toString(collision_count));
			count_collision_hotspot.setVisibility(View.VISIBLE);
		}
		Log.v("STTest", childDrawerItem.getType_hotspot()+count_collision_hotspot.getVisibility());

		name_hotspot.setText(childDrawerItem.getName_hotspot());
		
		return convertView;
		
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return this.listDatachild.get(this.listDataHeader.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return this.listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandablelistview_header, null);
        }
 
        TextView head_text = (TextView) convertView
                .findViewById(R.id.header_text);
        head_text.setTypeface(null, Typeface.BOLD);
        head_text.setText(headerTitle);
 
        return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
