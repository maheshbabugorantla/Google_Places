package edu.purdue.tada;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;

public class AboutTada extends BaseActivity {
	
	/*public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
	       
		// Create the About view and place it on the screen
		View view = inflater.inflate(R.layout.about_layout, container, false);
		return view;
	} */
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.about_layout);
	}
		

		/* Not needed due to functionality of back button in settings tab
		//rid of unnecessary button - Nicole Missele 3/20/15
		/* Button titleButton = (Button)findViewById(R.id.settings_button0);
		titleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AboutTada.this, SettingsActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Window w = SettingsGroup.group.getLocalActivityManager()
						.startActivity("BackToSettings", intent);
				View view = w.getDecorView();
				SettingsGroup.group.setContentView(view);
			}
		}); */

}
