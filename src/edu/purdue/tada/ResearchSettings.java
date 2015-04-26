package edu.purdue.tada;

import android.os.Bundle;

/* 
 * 	----ABOUT (Spring 2015)----
 * Research settings is a functionality for the Researcher to set alarms for the user of the app to be reminded
 * to take pictures of their food around breakfast, lunch, dinner, and a snack time.  Nicole Missele 2/28/2015
 * 
 */

public class ResearchSettings extends BaseActivity {
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.research_settings);
		// Set so the back button override knows the page is not on original settings page
		TabGroup.isSetting = false;
		//Button alarm1 = (Button)findViewById(R.id.research_button1);
		
		//set up alarm 1
	}
}
			
