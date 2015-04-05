package edu.purdue.tada;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
 
// Handles the swipe functionality between the main pages of the app
public class ScreenSwipe extends FragmentStatePagerAdapter {
 
	private int NUM_COUNT = 3;
    // Creates a ScreenSwipe FragmentPagerAdapter with the fragments that are included 
    public ScreenSwipe(FragmentManager fm) {
        super(fm);
    }

    // Returns each view's position in the swipeable interface
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
        {
        	System.out.println("In Tada");
        	return new TadaActivity();
        }
        else if (position == 1)
        {
        	System.out.println("In Review");
        	return new ReviewActivity();
        }
        else if (position == 2)
        {
        	System.out.println("In Settings");
        	return new SettingsActivity();
        }
		return null;
    }
 

    // Returns the number of views to swipe through
    @Override
    public int getCount() {
        return NUM_COUNT;
    }
    
    public int getItemPosition(Object obj)
    {
    	return POSITION_NONE;
    }
    
}
