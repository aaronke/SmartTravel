package com.aaron.smarttravel.drawer;

import java.util.ArrayList;

import com.aaron.smarttravel.main.R;
import com.aaron.smarttravel.utilities.BottomInfoItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BottomListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<BottomInfoItem> arrayList;
	
	public BottomListAdapter(Context context,ArrayList<BottomInfoItem> arrayList){
		this.context=context;
		this.arrayList=arrayList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}

	@Override
	public BottomInfoItem getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList.get(position);
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
			LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView=layoutInflater.inflate(R.layout.bottom_list_item, null);
		}
		TextView bottom_direction_textview=(TextView)convertView.findViewById(R.id.bottom_info_content_direction);
		TextView bottom_reason_textview=(TextView)convertView.findViewById(R.id.bottom_info_content_reason);
		TextView bottom_total_textview=(TextView)convertView.findViewById(R.id.bottom_info_content_total);
		
		bottom_direction_textview.setText(arrayList.get(position).getDirection());
		bottom_reason_textview.setText(arrayList.get(position).getReason());
		bottom_total_textview.setText(""+arrayList.get(position).getTotal());
		
		return convertView;
	}

}
