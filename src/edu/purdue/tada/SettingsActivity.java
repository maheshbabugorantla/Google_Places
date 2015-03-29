package edu.purdue.tada;

import android.os.Bundle;
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
				 // Put the User Settings page as the current fragment on the screen 
				 // make it so when the back button is clicked, it goes to more page
				 Fragment newFragment = new UserSettings();
				 FragmentTransaction transaction = getFragmentManager().beginTransaction();
				 transaction.replace(R.id.container, newFragment);
		         transaction.addToBackStack(null);
				 transaction.commit();
			}
		
		});
		
		Button btn2 = (Button) view.findViewById(R.id.settings_button2);
		btn2.setOnClickListener(new OnClickListener(){
		@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
//			builder.setPositiveButton(R.string.research_login, new DialogInterface.OnClickListener() {
				builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int id) {
					//sign in user
					// Put the User Settings page as the current fragment on the screen 
					 // make it so when the back button is clicked, it goes to more page
					 Fragment newFragment = new ResearchSettings();
					 FragmentTransaction transaction = getFragmentManager().beginTransaction();
					 transaction.replace(R.id.container, newFragment);
			         transaction.addToBackStack(null);
					 transaction.commit();
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
				 // Put the About page as the current fragment on the screen 
				 // make it so when the back button is clicked, it goes to more page
				 Fragment newFragment = new AboutTada();
				 FragmentTransaction transaction = getFragmentManager().beginTransaction();
				 transaction.replace(R.id.container, newFragment);
 		         transaction.addToBackStack(null);
				 transaction.commit();
			}
			
		});
	    return view;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.setting_layout);
		System.out.println("in settings activity");
		
		
		
			
		//Old code using the SettingsGroup
			/*@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this, UserSettings.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Window w = SettingsGroup.group.getLocalActivityManager()
						.startActivity("UserSettings", intent);
				View view = w.getDecorView();
				SettingsGroup.group.setContentView(view);*/
			
			
		
		//set up button two to go to Researcher settings via alert dialog - Nicole Missele 3/7/15
		
//		//set up alert dialog
//		
		
		
		//set up button 2 to go to Researcher settings password as own activity without alert dialog Nicole Missele 2/20/2015
//		btn2.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(SettingsActivity.this, ResearchPassword.class)
//				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				Window w = SettingsGroup.group.getLocalActivityManager()
//						.startActivity("ResearchPassword", intent);
//				View view = w.getDecorView();
//				SettingsGroup.group.setContentView(view);

		
		//set up button three to go to "About" screen using Settings Group
		/*btn3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this, AboutTada.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Window w = SettingsGroup.group.getLocalActivityManager()
						.startActivity("AboutTada", intent);
				View view = w.getDecorView();
				SettingsGroup.group.setContentView(view);
			}
		});	*/
		
		//set up button one to go to User Settings without the SettingsGroup functionality - Nicole Missele 3/20/15
		
		
	}
	


	/*protected void makeDiaglog() {
		DialogFragment d = new ResearchDialogFragment(this);
		d.show(getSupportFragmentManager(), "Reasearch Dialog");
	}

}
	
	

		public ResearchDialogFragment(Context context){
			super();
			c = context;
		}
		
		public Dialog onCreateDialog(Bundle savedInstanceState) {
	        AlertDialog.Builder builder = new AlertDialog.Builder(c);
	        LayoutInflater inflater = getActivity().getLayoutInflater();
	        
	        builder.setView(inflater.inflate(R.layout.password_layout, null))
		        .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   System.out.println("test");
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   ResearchDialogFragment.this.getDialog().cancel();
	               }
	           });  
	        
	        return builder.create();
		} */
	}

 

