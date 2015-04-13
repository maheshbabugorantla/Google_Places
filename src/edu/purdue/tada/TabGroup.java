package edu.purdue.tada;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class TabGroup extends ActivityGroup {

	private final String TAG = "TabGroup";
    public static FrameLayout container = null;
    private RadioGroup rGroup;
    private RadioButton radio0;
    private RadioButton radio1;
    private RadioButton radio2;
    private Fragment fr;
    private FragmentTransaction fragmentTransaction;
    public static boolean onSetting = true;
    
    public static ActivityGroup group; 
    
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	
    	
        super.onCreate(savedInstanceState);
        group = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.tada_layout);
        
        container = (FrameLayout)findViewById(R.id.container);
        rGroup = (RadioGroup)findViewById(R.id.tabGroup);
        
        radio0 = (RadioButton)findViewById(R.id.tab_0);
        radio1 = (RadioButton)findViewById(R.id.tab_1);
        radio2 = (RadioButton)findViewById(R.id.tab_2);
        //By default, the record button is checked when TADA is created
        //set the record button to "pressed" status
        radio0.setTextColor(Color.parseColor("#FFFFFF"));
        radio1.setTextColor(Color.parseColor("#5DD2DC"));
        radio2.setTextColor(Color.parseColor("#5DD2DC"));
        radio0.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_testicon_selected), null, null);
        radio1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_review_unselected), null, null);
        radio2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_more_unselected), null, null);
        //save tabs status in singleton
        ActivityBridge.getInstance().setRadio0(true);
        ActivityBridge.getInstance().setRadio1(false);
        ActivityBridge.getInstance().setRadio2(false);
		//set the first tab view to TadaActivity
        container.addView(getLocalActivityManager().startActivity(
                "Module1",
                new Intent(TabGroup.this, ViewPagerContainer.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView());
        
        // Start the view pager so that the user can swipe across the to change between
        // The main pages of the app. The default page is the Record screen
        /*fr = new ViewPagerContainer();
    	fragmentTransaction = getSupportFragmentManager().beginTransaction();
    	fragmentTransaction.replace(R.id.container, fr);
    	fragmentTransaction.commit(); */
             
        rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
					switch (checkedId) {
					case R.id.tab_0:
						Log.v("TabGroup", "Pressed first button");
						/*container.removeAllViews();
		                container.addView(getLocalActivityManager().startActivity(
		                        "Module1",
		                        new Intent(TabGroup.this, TadaActivity.class)
		                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
		                        .getDecorView());  */
						// Slide the page to the Record screen
						onSetting = false;
						ViewPagerContainer.mViewPager.setCurrentItem(0, true);
		                //set the record button to "pressed" status
		                radio0.setTextColor(Color.parseColor("#FFFFFF"));
		                radio1.setTextColor(Color.parseColor("#5DD2DC"));
		                radio2.setTextColor(Color.parseColor("#5DD2DC"));
		                radio0.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_testicon_selected), null, null);
		                radio1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_review_unselected), null, null);
		                radio2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_more_unselected), null, null);
		                //save tabs status in singleton
		                ActivityBridge.getInstance().setRadio0(true);
		                ActivityBridge.getInstance().setRadio1(false);
		                ActivityBridge.getInstance().setRadio2(false);
						break; 
					case R.id.tab_1:
						/*container.removeAllViews();
		                container.addView(getLocalActivityManager().startActivity(
		                        "Module2",
		                        new Intent(TabGroup.this, ReviewActivity.class)
		                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
		                        .getDecorView()); */
						// Slide the page to the Review Screen
						onSetting = false;
						ViewPagerContainer.mViewPager.setCurrentItem(1, true);
		                //set the review button to "pressed" status
		                radio1.setTextColor(Color.parseColor("#FFFFFF"));
		                radio0.setTextColor(Color.parseColor("#5DD2DC"));
		                radio2.setTextColor(Color.parseColor("#5DD2DC"));
		                radio1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_review_selected), null, null);
		                radio0.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_testicon_unselected), null, null);
		                radio2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_more_unselected), null, null);
		                //save tabs status in singleton
		                ActivityBridge.getInstance().setRadio1(true);
		                ActivityBridge.getInstance().setRadio0(false);
		                ActivityBridge.getInstance().setRadio2(false);
		                break;
					case R.id.tab_2:
						/*container.removeAllViews();
		                container.addView(getLocalActivityManager().startActivity(
		                        "Module3",
		                        new Intent(TabGroup.this, SettingsActivity.class)
		                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
		                        .getDecorView()); */
						// Slide the page to the More Screen
						onSetting = true;
						ViewPagerContainer.mViewPager.setCurrentItem(2, true);
		                //set the more button to "pressed" status
		                radio2.setTextColor(Color.parseColor("#FFFFFF"));
		                radio0.setTextColor(Color.parseColor("#5DD2DC"));
		                radio1.setTextColor(Color.parseColor("#5DD2DC"));
		                radio2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_more_selected), null, null);
		                radio0.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_testicon_unselected), null, null);
		                radio1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_review_unselected), null, null);
		                //save tabs status in singleton
		                ActivityBridge.getInstance().setRadio2(true);
		                ActivityBridge.getInstance().setRadio0(false);
		                ActivityBridge.getInstance().setRadio1(false);
		                break;
					}
				}
		}); 
        
        if (ViewPagerContainer.getViewPager() == null)
        {
        	System.out.println("Viewpager is null");
        }
        startViewPagerListener();
    } 
    
    //commented out since removed SettingsGroup Nicole Missele - 3/22/15
    public void onBackPressed() {  
    	// In the more tab if you are in any of the settings, if you press the back button
    	// it will take you back to original More tab
    	if (ActivityBridge.getInstance().isRadio2() == true)
    	{
    		if (onSetting == false)
    		{
    			onSetting = true;
    			container.removeAllViews();
    			container.addView(getLocalActivityManager().startActivity(
    	                "Module1",
    	                new Intent(TabGroup.this, ViewPagerContainer.class)
    	                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    	                .getDecorView());
    			ViewPagerContainer.mViewPager.setCurrentItem(2, true);
    			startViewPagerListener();
    		}
    		else
    		{
    			finish();
    		}
    	}
    	else
    	{
    		finish();
    	} 
    	
    	/*
    	//if users press the back button in the main activity, shows an alert dialog
        AlertDialog.Builder builder = new Builder(this);
  	  	builder.setTitle("Are you sure to quit TADA ?");
  	  	builder.setPositiveButton("Yes", new OnClickListener() {
  		  @Override
  		  public void onClick(DialogInterface dialog, int which) {
  			  finish();
  		  }});
  	  	builder.setNegativeButton("No", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
  	  	builder.create().show();
  	  	*/
    }   
    @Override
	public void onResume() {
    	super.onResume();
    	//set the tabs to the previous check status 
    	radio0.setChecked(ActivityBridge.getInstance().isRadio0());
        radio1.setChecked(ActivityBridge.getInstance().isRadio1());
        radio2.setChecked(ActivityBridge.getInstance().isRadio2());
    }
    
    public void startViewPagerListener()
    {
    	ViewPagerContainer.mViewPager.setOnPageChangeListener(new OnPageChangeListener () {
    		public void onPageScrollStateChanged(int state) {}
    		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
    		
    		public void onPageSelected(int position) {
    			switch (position) {
    			case 0: // If the user scrolled to the Record page
    		        //set the record button to "pressed" status
    		        radio0.setTextColor(Color.parseColor("#FFFFFF"));
    		        radio1.setTextColor(Color.parseColor("#5DD2DC"));
    		        radio2.setTextColor(Color.parseColor("#5DD2DC"));
    		        radio0.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_testicon_selected), null, null);
    		        radio1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_review_unselected), null, null);
    		        radio2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_more_unselected), null, null);
    		        //save tabs status in singleton
    		        ActivityBridge.getInstance().setRadio0(true);
    		        ActivityBridge.getInstance().setRadio1(false);
    		        ActivityBridge.getInstance().setRadio2(false);
    				break; 
    			case 1: // If the user scrolled to the Review page
    		        //set the review button to "pressed" status
    		        radio1.setTextColor(Color.parseColor("#FFFFFF"));
    		        radio0.setTextColor(Color.parseColor("#5DD2DC"));
    		        radio2.setTextColor(Color.parseColor("#5DD2DC"));
    		        radio1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_review_selected), null, null);
    		        radio0.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_testicon_unselected), null, null);
    		        radio2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_more_unselected), null, null);
    		        //save tabs status in singleton
    		        ActivityBridge.getInstance().setRadio1(true);
    		        ActivityBridge.getInstance().setRadio0(false);
    		        ActivityBridge.getInstance().setRadio2(false);
    		        break;
    			case 2: // If the user scrolled to the More page
    		        //set the more button to "pressed" status
    				onSetting = true;
    		        radio2.setTextColor(Color.parseColor("#FFFFFF"));
    		        radio0.setTextColor(Color.parseColor("#5DD2DC"));
    		        radio1.setTextColor(Color.parseColor("#5DD2DC"));
    		        radio2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_more_selected), null, null);
    		        radio0.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_testicon_unselected), null, null);
    		        radio1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_review_unselected), null, null);
    		        //save tabs status in singleton
    		        ActivityBridge.getInstance().setRadio2(true);
    		        ActivityBridge.getInstance().setRadio0(false);
    		        ActivityBridge.getInstance().setRadio1(false);
    		        break;
    			}
    		}
    	}); 
    }
}

