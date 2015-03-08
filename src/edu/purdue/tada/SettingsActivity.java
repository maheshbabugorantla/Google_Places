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
				Intent intent = new Intent(SettingsActivity.this, UserSettings.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Window w = SettingsGroup.group.getLocalActivityManager()
						.startActivity("UserSettings", intent);
				View view = w.getDecorView();
				SettingsGroup.group.setContentView(view);
				TabGroup.isSetting = false;
				
			}
		});
		
		//set up button two to go to Researcher settings - Nicole Missele 2/20/15
		// Alert dialog
		final SettingsActivity instance = this;
		btn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				TabGroup.isSetting = false;
				// Above code moves to ResearchPassword class, let's try to do it with a dialog
				instance.makeDiaglog();
			}
			
		});
		
		//set up button three to go to "About" screen
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
	
	protected void makeDiaglog() {
		DialogFragment d = new ResearchDialogFragment(this);
		d.show(getSupportFragmentManager(), "Reasearch Dialog");
	}
	
	private class ResearchDialogFragment extends DialogFragment {
		private Context c;
		
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
		}
	}
}
