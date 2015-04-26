package edu.purdue.tada;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

public class AboutTada extends BaseActivity {
	
	
	public void onCreate(Bundle savedInstanceState) {
		// Set so the back button override knows the page is not on original settings page
		TabGroup.isSetting = false;
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.about_layout);
	}
}
