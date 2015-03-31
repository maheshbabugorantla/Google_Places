package edu.purdue.tada;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerContainer extends Fragment
{
	private PagerAdapter mPagerAdapter;
	public static ViewPager mViewPager;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.viewpager_container, container, false);          

        // Set the viewPager up with the three fragments that we want to swipe through
        Fragment[] frs = {new TadaActivity(), new ReviewActivity(), new SettingsActivity()};
        mPagerAdapter = new ScreenSwipe(getFragmentManager(), frs);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mPagerAdapter);
        System.out.println("In viewpager");

        return view;
    }
}
