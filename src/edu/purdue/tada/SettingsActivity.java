package edu.purdue.tada;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.view.ContextThemeWrapper;

public class SettingsActivity extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
	    
		//Inflate the layout for this fragment 
		View view = inflater.inflate(R.layout.setting_layout, container, false);
		// Apply the color to the fragment's background
		//view.setBackgroundColor(backGroundColor);
		
		Button btn1 = (Button) view.findViewById(R.id.settings_button1);
		//set up button one to go to User Settings without the SettingsGroup functionality - Nicole Missele 3/20/15
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TabGroup.container.removeAllViews();
				TabGroup.container.addView(TabGroup.group.getLocalActivityManager().startActivity(
						"UserSettings",
						new Intent(getActivity(), UserSettings.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
			}
		});
		
		Button btn2 = (Button) view.findViewById(R.id.settings_button2);
		btn2.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				// Dialog for Researcher Settings Login -- Nicole Missele 3/8/15
				dialog();
			}
			
			//using dialog box
			private void dialog(){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				View layout = inflater.inflate(R.layout.password_layout, null);
				builder.setTitle("Login");			
				builder.setView(layout);
				//add action buttons
				//builder.setPositiveButton(R.string.research_login, new DialogInterface.OnClickListener() {
				//Define Login button to take you to Researcher Settings Activity - Nicole Missele 3/10/15
				builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int id) {
						//sign in user
							TabGroup.container.removeAllViews();
							TabGroup.container.addView(TabGroup.group.getLocalActivityManager().startActivity(
							"ResearchSettings",
							new Intent(getActivity(), ResearchSettings.class)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
							.getDecorView());
					}
						
				});
				//Define Cancel Button in dialog box - Nicole Missele 3/8/15
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						
					}
				});
				builder.create().show();
			}
		});
		
		//Set button three to take you to the AboutTada activity 
		Button btn3 = (Button) view.findViewById(R.id.settings_button3);
		btn3.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				TabGroup.container.removeAllViews();
				TabGroup.container.addView(TabGroup.group.getLocalActivityManager().startActivity(
		                "About",
		                new Intent(getActivity(), AboutTada.class)
		                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
		                .getDecorView());
			}
			
		});
	    return view;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.setting_layout);
		System.out.println("in settings activity");
	}
}
		
		
		
			
		