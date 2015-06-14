package com.aaron.smarttravel.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class SampleListFragmentRight extends Fragment implements OnCheckedChangeListener{
	
	public Context context;
	SharedPreferences sharedPreferences_settings;
	Switch check_update_switch,notification_switch,voice_message_switch;
	SharedPreferences.Editor setting_Editor;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		sharedPreferences_settings=context.getSharedPreferences(getString(R.string.preferences_settings), Context.MODE_PRIVATE);
		View view=inflater.inflate(R.layout.right_drawer, null);
		
		
		notification_switch=(Switch)view.findViewById(R.id.setting_notification_switch);
		voice_message_switch=(Switch)view.findViewById(R.id.setting_voice_switch);
		check_update_switch=(Switch)view.findViewById(R.id.setting_update_switch);
		
		notification_switch.setChecked(sharedPreferences_settings.getBoolean(getString(R.string.preferences_setting_notification), true));
		voice_message_switch.setChecked(sharedPreferences_settings.getBoolean(getString(R.string.preferences_setting_voice_message), true));
		check_update_switch.setChecked(sharedPreferences_settings.getBoolean(getString(R.string.preferences_setting_check_update), true));
		
		notification_switch.setOnCheckedChangeListener(this);
		voice_message_switch.setOnCheckedChangeListener(this);
		check_update_switch.setOnCheckedChangeListener(this);
		
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
		updateSetting();
		super.onDestroy();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	
	public void updateSetting(){
		setting_Editor=sharedPreferences_settings.edit();
		setting_Editor.putBoolean(getString(R.string.preferences_setting_notification), notification_switch.isChecked());
		setting_Editor.putBoolean(getString(R.string.preferences_setting_voice_message), voice_message_switch.isChecked());
		setting_Editor.putBoolean(getString(R.string.preferences_setting_check_update), check_update_switch.isChecked());
		setting_Editor.commit();
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
			switch (buttonView.getId()) {
			case R.id.setting_voice_switch:
				if (isChecked) {
					
				}
			default:
				break;
			}
			
			updateSetting();
		}
	
	
}
