package edu.purdue.tada;

import java.util.ArrayList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
// Handles the swipe functionality between the main pages of the app
public class ScreenSwipe extends FragmentPagerAdapter {
 
    private Fragment[] frs;

    // Creates a ScreenSwipe FragmentPagerAdapter with the fragments that are included 
    public ScreenSwipe(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.frs = fragments;
        for (int i = 0; i < frs.length; i++)
        {
        	System.out.println(i + ": " + frs[i]);
        }
    }

    // Returns each view's position in the swipeable interface
    @Override
    public Fragment getItem(int position) {
        return frs[position];
    }
 

    // Returns the number of views to swipe through
    @Override
    public int getCount() {
        return frs.length;
    }
    
}
