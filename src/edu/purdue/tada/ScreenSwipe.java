package edu.purdue.tada;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
 
// Handles the swipe functionality between the main pages of the app
public class ScreenSwipe extends FragmentStatePagerAdapter {
 
    // Creates a ScreenSwipe FragmentPagerAdapter
    public ScreenSwipe(FragmentManager fm) {
        super(fm);
    }

    // Returns each view's position in the swipeable interface
    @Override
    public Fragment getItem(int position) {
    	// If at first page, return Record page
        if (position == 0)
        {
        	return new TadaActivity();
        }
        // If at second page, return Review page
        else if (position == 1)
        {
        	return new ReviewActivity();
        }
        // If at third page, return More page
        else if (position == 2)
        {
        	return new SettingsActivity();
        }
        // If none of the above, return null
		return null;
    }
 

    // Returns the number of views to swipe through
    @Override
    public int getCount() {
        return 3;
    }
    
    // Return POSITION_NONE so the viewpager is updated when you notify their is an update
    public int getItemPosition(Object obj)
    {
    	return POSITION_NONE;
    }
    
}
