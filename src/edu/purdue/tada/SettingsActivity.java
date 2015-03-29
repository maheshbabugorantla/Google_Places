package edu.purdue.tada;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class SettingsActivity extends BaseFragment {
			
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
	       
		//Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.setting_layout, container, false);
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
		btn2.setOnClickListener(new OnClickListener(){
		@Override
			public void onClick(View v) {
				TabGroup.isSetting = false;
				// Above code moves to ResearchPassword class, let's try to do it with a dialog
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
		Button btn3 = (Button) view.findViewById(R.id.settings_button3);
		btn3.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				TabGroup.container.removeAllViews();
				TabGroup.container.addView(TabGroup.group.getLocalActivityManager().startActivity(
						"UserSettings",
						new Intent(getActivity(), UserSettings.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
						.getDecorView());
			}
			
		});
		return view;
	}
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.setting_layout);
		System.out.println("in settings activity");
	}
}
	


	
