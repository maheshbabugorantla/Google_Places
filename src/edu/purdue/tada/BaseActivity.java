package edu.purdue.tada;

//import android.app.Activity;
//import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;
//import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
//import android.view.View;
//import android.view.Window;

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
	//prepare for changing theme Nicole Missele 4/12/2015
		TabGroup.container.removeAllViews();
		TabGroup.container.addView(TabGroup.group.getLocalActivityManager().startActivity(
				"ChangeTheme",
				new Intent(this, ChangeTheme.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView());
	}
}
		
		
		/*Window w = SettingsGroup.group.getLocalActivityManager().startActivity(
				"ChangeTheme", intent);
		View view = w.getDecorView();

		SettingsGroup.group.setContentView(view);*/
		
		//refresh view for the theme to change
		
		// Refresh main activity upon close of dialog box
	
	//attempt 1
		//this works but it still needs the app to be closed out since pressing the back button doesn't work.
	/*
	protected void reload()
	{
		Intent refresh = new Intent(this, ChangeTheme.class);
		startActivity(refresh);
		this.finish(); 
	}
		*/
	//attempt 2
	/*
	public void reload() {

	    Intent intent = getIntent();
	    overridePendingTransition(0, 0);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    finish();

	    overridePendingTransition(0, 0);
	    startActivity(intent);
	}
			*/
	
	
		

