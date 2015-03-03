package edu.purdue.tada;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;


public class ResearchPassword extends BaseActivity{
	
	//open the password screen of research settings Nicole Missele 2/22/15
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_layout);
		
		//Define buttons Nicole Missele 2/28/15
		Button btncancel = (Button)findViewById(R.id.pwd_cancel);
		Button btnsubmit = (Button)findViewById(R.id.pwd_submit);
		
		//Define cancel button
		btncancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResearchPassword.this, SettingsActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Window w = SettingsGroup.group.getLocalActivityManager()
						.startActivity("SettingsActivity", intent);
				View view = w.getDecorView();
				SettingsGroup.group.setContentView(view);
				}
			});
		
		//Define the submit button
		btnsubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ResearchPassword.this, ResearchSettings.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Window w = SettingsGroup.group.getLocalActivityManager()
						.startActivity("ResearchSettings", intent);
				View view = w.getDecorView();
				SettingsGroup.group.setContentView(view);
				}
			});
		
		
		
		
		}
	}


		


