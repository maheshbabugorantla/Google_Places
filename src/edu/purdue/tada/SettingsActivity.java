package edu.purdue.tada;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog.Builder;
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
		
		//set up button one to go to User Settings
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this, UserSettings.class);
				startActivity(intent);
				TabGroup.isSetting = false;
				
			}
		});
		
		//set up button two to go to Researcher settings - Nicole Missele 2/20/15
		btn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				TabGroup.isSetting = false;
				// Above code moves to ResearchPassword class, let's try to do it with a dialog
				dialog();
			}
			//using dialog box
			private void dialog(){
				AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
				LayoutInflater inflater = LayoutInflater.from(SettingsActivity.this);
				View layout = inflater.inflate(R.layout.password_layout, null);
				builder.setTitle("Login");			
				builder.setView(layout);
			
				//add action buttons
				//builder.setPositiveButton(R.string.research_login, new DialogInterface.OnClickListener() {
				builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// TODO Auto-generated method stub
						//sign in user
						//dialog.dismiss();
						Intent intent = new Intent(SettingsActivity.this, ResearchPassword.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						Window w = SettingsGroup.group.getLocalActivityManager()
								.startActivity("ResearchPassword", intent);
						View view = w.getDecorView();
						SettingsGroup.group.setContentView(view);
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						
					}
				});
			
				builder.create().show();
				
			};
		});
			
		
		//set up button three to go to "About" screen
		btn3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this, AboutTada.class);
				startActivity(intent);
				TabGroup.isSetting = false;
			}
		});	
	}
}
	
	