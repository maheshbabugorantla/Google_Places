package edu.purdue.tada;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChangeTheme extends BaseActivity {
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		TabGroup.isSetting = false;
		setContentView(R.layout.change_theme);	
		
		Button cornflowerblue = (Button)findViewById(R.id.cornflowerblue);
		cornflowerblue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(ChangeTheme.this, R.style.AppTheme_cornflowerblue);
                reload();
				
			
			}
		});
		
		Button skyblue = (Button)findViewById(R.id.skyblue);
		skyblue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(ChangeTheme.this, R.style.AppTheme_skyblue);
                reload();
			}
		});
		
		Button powderblue = (Button)findViewById(R.id.powderblue);
		powderblue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(ChangeTheme.this, R.style.AppTheme_powderblue);
                reload();
				
			}
		});
		
		Button mediumturquoise = (Button)findViewById(R.id.mediumturquoise);
		mediumturquoise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(ChangeTheme.this, R.style.AppTheme_mediumturquoise);
                reload();
				
			}
		});
		
		Button white = (Button)findViewById(R.id.white);
		white.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(ChangeTheme.this, R.style.AppTheme_white);
                reload();
				
			}
		});
		
		Button lightpink = (Button)findViewById(R.id.lightpink);
		lightpink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(ChangeTheme.this, R.style.AppTheme_lightpink);
                reload();
				
			}
		});
		
		Button plum = (Button)findViewById(R.id.plum);
		plum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(ChangeTheme.this, R.style.AppTheme_plum);
                reload();
				
			}
		});
		Button navajowhite = (Button)findViewById(R.id.navajowhite);
		navajowhite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(ChangeTheme.this, R.style.AppTheme_navajowhite);
                reload();
				
			}
		});
		Button lightsalmon = (Button)findViewById(R.id.lightsalmon);
		lightsalmon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(ChangeTheme.this, R.style.AppTheme_lightsalmon);
                reload();
			}
		});
	}
}
