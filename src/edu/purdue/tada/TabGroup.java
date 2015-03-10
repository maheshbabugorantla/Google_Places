package edu.purdue.tada;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ViewFlipper;


public class TabGroup extends ActivityGroup{

	private final String TAG = "TabGroup";
    private FrameLayout container = null;
    private RadioGroup rGroup;
    private RadioButton radio0;
    private RadioButton radio1;
    private RadioButton radio2;
    public static boolean isSetting = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.tada_layout);
        container = (FrameLayout)findViewById(R.id.container);
        rGroup = (RadioGroup)findViewById(R.id.tabGroup);
        
        
        radio0 = (RadioButton)findViewById(R.id.tab_0);
        radio1 = (RadioButton)findViewById(R.id.tab_1);
        radio2 = (RadioButton)findViewById(R.id.tab_2);
        //By default, the record button is checked when TADA is created
        radio0.setChecked(true);
        radio1.setChecked(false);
        radio2.setChecked(false);
        //initial the text color and button image for the record button 
        radio0.setTextColor(Color.parseColor("#FFFFFF"));
        radio0.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_tab_testicon_selected), null, null);
		//set the first tab view to TadaActivity
        container.removeAllViews();
        container.addView(getLocalActivityManager().startActivity(
                "Module1",
                new Intent(TabGroup.this, TadaActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView());
                
        rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
					switch (checkedId) {
					case R.id.tab_0:
						container.removeAllViews();
		                container.addView(getLocalActivityManager().startActivity(
		                        "Module1",
		                        new Intent(TabGroup.this, TadaActivity.class)
		                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
		                        .getDecorView()); 
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
						container.removeAllViews();
		                container.addView(getLocalActivityManager().startActivity(
		                        "Module2",
		                        new Intent(TabGroup.this, ReviewActivity.class)
		                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
		                        .getDecorView());
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
						isSetting = true;
						container.removeAllViews();
		                container.addView(getLocalActivityManager().startActivity(
		                        "Module3",
		                        new Intent(TabGroup.this, SettingsActivity.class)
		                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
		                        .getDecorView());
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
    }
    
    
    
    @Override
    public void onBackPressed() {  
    	// In the more tab if you are in any of the settings, if you press the back button
    	// it will take you back to original More tab
    	if (ActivityBridge.getInstance().isRadio2() == true)
    	{
    		if (isSetting == false)
    		{
    			isSetting = true;
		    	Intent intent = new Intent(TabGroup.this, SettingsActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Window w = SettingsGroup.group.getLocalActivityManager()
						.startActivity("BackToSettings", intent);
				View view = w.getDecorView();
				SettingsGroup.group.setContentView(view);
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
    protected void onResume() {
    	super.onResume();
    	//set the tabs to the previous check status 
    	radio0.setChecked(ActivityBridge.getInstance().isRadio0());
        radio1.setChecked(ActivityBridge.getInstance().isRadio1());
        radio2.setChecked(ActivityBridge.getInstance().isRadio2());
    }
}