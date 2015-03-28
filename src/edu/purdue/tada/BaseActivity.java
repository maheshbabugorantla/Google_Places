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

<<<<<<< Upstream, based on ui
public class BaseActivity extends Activity
=======
public class BaseActivity extends Fragment
>>>>>>> 8e7baa0 Rebased off ui branch
{
	private final String TAG = "BaseActivity";
	
	public int mTheme = R.style.AppBaseTheme;
	// no longer need to use this, use
	// activitybridge.getInstance().getRecSaved()
	// final String recSaved = getBaseContext().getFilesDir().getPath();
	// //changed to application directory --Alex Beard 9/21/2013
	protected String recSaved;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		if (savedInstanceState == null)
		{
			mTheme = PreferenceHelper.getTheme(getActivity());
		} else
		{
			mTheme = savedInstanceState.getInt("theme");
		}
		// create ContextThemeWrapper from the original Activity Context with the custom theme
	    final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), mTheme);
		//setTheme(mTheme);
		
		recSaved = getActivity().getFilesDir().getPath();
		Log.d(TAG, "recSaved = \"" + recSaved + "\"");
		
		super.onCreate(savedInstanceState);
	}
	
	public void onResume()
	{
		super.onResume();
		if (mTheme != PreferenceHelper.getTheme(this))
		{
			reload();
		}
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
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
		Window w = SettingsGroup.group.getLocalActivityManager().startActivity(
				"ChangeTheme", intent);
		View view = w.getDecorView();
		SettingsGroup.group.setContentView(view);
		
	}
}
