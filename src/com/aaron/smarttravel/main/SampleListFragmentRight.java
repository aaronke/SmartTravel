package com.aaron.smarttravel.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class SampleListFragmentRight extends Fragment {
	
	public Context context;
	SharedPreferences sharedPreferences_settings;
	CheckBox gps_checkbox,notification_checkbox,voice_message_checkbox;
	SharedPreferences.Editor setting_Editor;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		sharedPreferences_settings=context.getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		View view=inflater.inflate(R.layout.right_drawer, null);
		
		gps_checkbox=(CheckBox)view.findViewById(R.id.setting_gps_checkbox);
		notification_checkbox=(CheckBox)view.findViewById(R.id.setting_notification_checkbox);
		voice_message_checkbox=(CheckBox)view.findViewById(R.id.setting_voice_checkbox);
		
		gps_checkbox.setChecked(sharedPreferences_settings.getBoolean(getString(R.string.preferences_setting_gps), true));
		notification_checkbox.setChecked(sharedPreferences_settings.getBoolean(getString(R.string.preferences_setting_notification), false));
		voice_message_checkbox.setChecked(sharedPreferences_settings.getBoolean(getString(R.string.preferences_setting_voice_message), true));
		
		
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/*SampleAdapter adapter = new SampleAdapter(getActivity());
		for (int i = 0; i < 4; i++) {
			adapter.add(new SampleItem("Sample List", android.R.drawable.ic_menu_search));
		}
		setListAdapter(adapter);*/
	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		context=activity;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		setting_Editor=sharedPreferences_settings.edit();
		setting_Editor.putBoolean(getString(R.string.preferences_setting_gps), gps_checkbox.isChecked());
		setting_Editor.putBoolean(getString(R.string.preferences_setting_notification), notification_checkbox.isChecked());
		setting_Editor.putBoolean(getString(R.string.preferences_setting_voice_message), voice_message_checkbox.isChecked());
		
		setting_Editor.commit();
		super.onDestroy();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	
}
