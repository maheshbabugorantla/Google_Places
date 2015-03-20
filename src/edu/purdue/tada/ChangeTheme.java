package edu.purdue.tada;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;

public class ChangeTheme extends BaseActivity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_theme);	
		//The first color
		Button changeButton1 = (Button)findViewById(R.id.change1);
		changeButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(ChangeTheme.this, R.style.AppTheme_cyan);
                reload();
			
			}
		});
		
		//The second color
		Button changeButton2 = (Button)findViewById(R.id.change2);
		changeButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(ChangeTheme.this, R.style.AppTheme_white);
                reload();
			
			}
		});
		
		//The third color
		Button changeButton3 = (Button)findViewById(R.id.change3);
		changeButton3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(ChangeTheme.this, R.style.AppTheme_orange);
                reload();
			
			}
		});
		
		//The fourth color
		Button changeButton4 = (Button)findViewById(R.id.change4);
		changeButton4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(ChangeTheme.this, R.style.AppTheme_green);
                reload();
			
			}
		});
		
		//The fifth color
		Button changeButton5 = (Button)findViewById(R.id.change5);
		changeButton5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(ChangeTheme.this, R.style.AppTheme_magenta);
                reload();
			
			}
		});
	}
}
