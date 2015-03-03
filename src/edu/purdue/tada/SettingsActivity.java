package edu.purdue.tada;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.content.Intent;

public class SettingsActivity extends BaseActivity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_layout);
		System.out.println("in settings activity");
		Button btn1 = (Button)findViewById(R.id.settings_button1);
		Button btn2 = (Button)findViewById(R.id.settings_button2);
		Button btn3 = (Button)findViewById(R.id.settings_button3);
		
		
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this, UserSettings.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Window w = SettingsGroup.group.getLocalActivityManager()
						.startActivity("UserSettings", intent);
				View view = w.getDecorView();
				SettingsGroup.group.setContentView(view);
				TabGroup.isSetting = false;
				
			}
		});
		btn2.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				TabGroup.isSetting = false;
			}
			
		});
		btn3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this, AboutTada.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Window w = SettingsGroup.group.getLocalActivityManager()
						.startActivity("AboutTada", intent);
				View view = w.getDecorView();
				SettingsGroup.group.setContentView(view);
				TabGroup.isSetting = false;
			}
		});	
	}
}