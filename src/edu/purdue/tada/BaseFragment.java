package edu.purdue.tada;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;

public class BaseFragment extends Fragment
{
	private final String TAG = "BaseActivity";
	
	protected int mTheme = R.style.AppBaseTheme;
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
		//setTheme(mTheme);
		recSaved = getActivity().getBaseContext().getFilesDir().getPath();
		Log.d(TAG, "recSaved = \"" + recSaved + "\"");
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		if (mTheme != PreferenceHelper.getTheme(getActivity()))
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
		Intent intent = getActivity().getIntent();
		getActivity().overridePendingTransition(0, 0);
		/*
		 * intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); finish();
		 * overridePendingTransition(0, 0); startActivity(intent);
		 */
		// Put the Theme page as the current fragment on the screen 
		// make it so when the back button is clicked, it goes to more page
		/*Fragment newFragment = new ChangeTheme();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);
		transaction.commit();*/
		
	}
}
