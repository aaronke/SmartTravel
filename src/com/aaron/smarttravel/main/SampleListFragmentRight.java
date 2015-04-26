package com.aaron.smarttravel.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SampleListFragmentRight extends Fragment {
	
	public Context context;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.right_drawer, null);
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
}
