package edu.purdue.tada;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;

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
		
		recSaved = getBaseContext().getFilesDir().getPath();
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
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		/*
		 * intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); finish();
		 * overridePendingTransition(0, 0); startActivity(intent);
		 */
		ViewPagerContainer.mPagerAdapter.notifyDataSetChanged();
		TabGroup.container.removeAllViews();
		TabGroup.container.addView(TabGroup.group.getLocalActivityManager().startActivity(
                "ChangeTheme",
                new Intent(this, ChangeTheme.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView());
		
		
	}
}
