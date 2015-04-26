package edu.purdue.tada;

import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;

public class BaseFragment extends Fragment
{
	private final String TAG = "BaseActivity";
	
	protected int mTheme = R.style.AppBaseTheme;
	int backGroundColor;
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
		// Get the color of the theme
		TypedValue typedValue = new TypedValue();
		Theme theme = getActivity().getTheme();
		theme.resolveAttribute(R.attr.background, typedValue, true);
		backGroundColor = typedValue.data;
		recSaved = getActivity().getBaseContext().getFilesDir().getPath();
		Log.d(TAG, "recSaved = \"" + recSaved + "\"");
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putInt("theme", mTheme);
	}
}
