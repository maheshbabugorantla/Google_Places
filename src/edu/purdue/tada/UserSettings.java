package edu.purdue.tada;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.content.Intent;

public class UserSettings extends BaseActivity{
	private CheckBox checkBox1;
	private CheckBox checkBox2;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_layout);
		//update check box status
		checkBox1 = (CheckBox)findViewById(R.id.checkBox1);
		checkBox2 = (CheckBox)findViewById(R.id.checkBox2);
		checkBox1.setChecked(PreferenceHelper.getTips(this));
		checkBox2.setChecked(ActivityBridge.getInstance().isChecked2());
		
		Button btn0 = (Button)findViewById(R.id.user_settings_button0);
		btn0.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(UserSettings.this, SettingsActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Window w = SettingsGroup.group.getLocalActivityManager()
						.startActivity("BackToSettings", intent);
				View view = w.getDecorView();
				SettingsGroup.group.setContentView(view);
			}
		});
		//prepare "theme" button
		Button themeButton = (Button)findViewById(R.id.theme_button);
		themeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(UserSettings.this, ChangeTheme.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Window w = SettingsGroup.group.getLocalActivityManager()
						.startActivity("ChangeTheme", intent);
				View view = w.getDecorView();
				SettingsGroup.group.setContentView(view);
			}
		});
		//prepare the checkbox for "tips" option
		checkBox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					checkBox1.setChecked(true);
					PreferenceHelper.setTips(UserSettings.this, true);
					ActivityBridge.getInstance().setChecked1(true);
				}else {
					checkBox1.setChecked(false);
					ActivityBridge.getInstance().setChecked1(false);
					PreferenceHelper.setTips(UserSettings.this, false);
				}
			}
		});
		//prepare the checkbox for "camera assistant" option
		checkBox2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					checkBox2.setChecked(true);
					ActivityBridge.getInstance().setChecked2(true);
				}else {
					checkBox2.setChecked(false);
					ActivityBridge.getInstance().setChecked2(false);
				}
			}
		});
	}
}