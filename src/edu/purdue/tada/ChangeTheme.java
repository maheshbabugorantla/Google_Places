package edu.purdue.tada;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChangeTheme extends BaseFragment {
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_theme);	

		
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
	       
		
		//Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.change_theme, container, false);
		
		Button cornflowerblue = (Button)view.findViewById(R.id.cornflowerblue);
		cornflowerblue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(getActivity(), R.style.AppTheme_cornflowerblue);
                reload();
				
			
			}
		});
		Button skyblue = (Button)view.findViewById(R.id.skyblue);
		skyblue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(getActivity(), R.style.AppTheme_skyblue);
                reload();
			}
		});
		Button powderblue = (Button)view.findViewById(R.id.powderblue);
		powderblue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(getActivity(), R.style.AppTheme_powderblue);
                reload();
				
			}
		});
		Button mediumturquoise = (Button)view.findViewById(R.id.mediumturquoise);
		mediumturquoise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(getAcitivty(), R.style.AppTheme_mediumturquoise);
                reload();
				
			}
		});
		Button white = (Button)view.findViewById(R.id.white);
		white.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(getActivity(), R.style.AppTheme_white);
                reload();
				
			}
		});
		Button lightpink = (Button)view.findViewById(R.id.lightpink);
		lightpink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(getActivity(), R.style.AppTheme_lightpink);
                reload();
				
			}
		});
		Button plum = (Button)view.findViewById(R.id.plum);
		plum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(getActivty(), R.style.AppTheme_plum);
                reload();
				
			}
		});
		Button navajowhite = (Button)view.findViewById(R.id.navajowhite);
		navajowhite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(getActivity(), R.style.AppTheme_navajowhite);
                reload();
				
			}
		});
		Button lightsalmon = (Button)view.findViewById(R.id.lightsalmon);
		lightsalmon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(getActivity(), R.style.AppTheme_lightsalmon);
                reload();
				
			}
		});
		
		
	}
}
