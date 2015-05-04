package com.aaron.smarttravel.main;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TermOfUseActivity extends Activity implements OnClickListener{
	
	private ActionBar term_of_use_actionbar;
	private Button disagreeButton,agreeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.term_of_use);
		term_of_use_actionbar=getActionBar();
		
		term_of_use_actionbar.setDisplayShowCustomEnabled(true);
		term_of_use_actionbar.setCustomView(R.layout.term_of_use_actionbar);
		term_of_use_actionbar.setDisplayHomeAsUpEnabled(false);
		term_of_use_actionbar.setDisplayShowTitleEnabled(false);
		term_of_use_actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_color)));
		
		
		disagreeButton=(Button)findViewById(R.id.term_of_use_disagree_button);
		agreeButton=(Button)findViewById(R.id.term_of_use_agree_button);
		disagreeButton.setOnClickListener(this);
		agreeButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.term_of_use_disagree_button:
			finish();
			break;
		case R.id.term_of_use_agree_button:
			Intent introductionIntent=new Intent(TermOfUseActivity.this, IntroductionActivity.class);
			startActivity(introductionIntent);
			finish();
			break;
		default:
			break;
		}
	}
	
	

}
