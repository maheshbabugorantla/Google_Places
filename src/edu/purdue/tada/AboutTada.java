package edu.purdue.tada;

import android.os.Bundle;

public class AboutTada extends BaseActivity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout); 	
		
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
}