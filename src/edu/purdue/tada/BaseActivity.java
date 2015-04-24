package edu.purdue.tada;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class BaseActivity extends FragmentActivity
{
	private final String TAG = "BaseActivity";
	
	public int mTheme = R.style.AppBaseTheme;
	// no longer need to use this, use
	// activitybridge.getInstance().getRecSaved()
	// final String recSaved = getBaseContext().getFilesDir().getPath();
	// //changed to application directory --Alex Beard 9/21/2013
	protected String recSaved;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		if (savedInstanceState == null)
		{
			mTheme = PreferenceHelper.getTheme(this);
		} else
		{
			mTheme = savedInstanceState.getInt("theme");
		}
		setTheme(mTheme);
		
		recSaved = getFilesDir().getPath();
		Log.d(TAG, "recSaved = \"" + recSaved + "\"");
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if (mTheme != PreferenceHelper.getTheme(this))
		{
			reload();
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putInt("theme", mTheme);
	}
	
	protected void reload()
	{
		overridePendingTransition(0, 0);
		/*
		 * intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); finish();
		 * overridePendingTransition(0, 0); startActivity(intent);
		 */   
		
		// Update the fragments in the viewPager since their is a color change
		ViewPagerContainer.mPagerAdapter.notifyDataSetChanged();
		//prepare for changing theme to update by reloading Nicole Missele 4/12/2015
		TabGroup.container.removeAllViews();
		TabGroup.container.addView(TabGroup.group.getLocalActivityManager().startActivity(
                "ChangeTheme",
                new Intent(this, ChangeTheme.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView());
		}
}