package edu.purdue.tada;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;

public class ViewPagerContainer extends BaseActivity
{
	public static ScreenSwipe mPagerAdapter;
	public static ViewPager mViewPager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.viewpager_container);
        
     // Set the viewPager up with the three fragments that we want to swipe through;
        mPagerAdapter = new ScreenSwipe(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPagerAdapter);
        System.out.println("In viewpager");
    }
}
