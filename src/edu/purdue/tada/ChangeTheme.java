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
	
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
	       
		
		//Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.change_theme, container, false);
		
		Button changeButton1 = (Button)view.findViewById(R.id.change1);
		changeButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(getActivity(), R.style.AppTheme_cyan);
                reload();
			
			}
		});
		Button changeButton2 = (Button)view.findViewById(R.id.change2);
		changeButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(getActivity(), R.style.AppTheme_white);
                reload();
			
			}
		});
		Button changeButton3 = (Button)view.findViewById(R.id.change3);
		changeButton3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(getActivity(), R.style.AppTheme_orange);
                reload();
			
			}
		});
		Button changeButton4 = (Button)view.findViewById(R.id.change4);
		changeButton4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(getActivity(), R.style.AppTheme_green);
                reload();
			
			}
		});
		Button changeButton5 = (Button)view.findViewById(R.id.change5);
		changeButton5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreferenceHelper.setTheme(getActivity(), R.style.AppTheme_magenta);
                reload();
			
			}
		});
		return view;
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.change_theme);
	}
}