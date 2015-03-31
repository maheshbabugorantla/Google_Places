package edu.purdue.tada;

import java.util.Calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ResearchSettings extends BaseFragment {
	
	private String TAG = "ResearchSettings";
	
	//open the research settings from the password screen Nicole Missele 2/28/15
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
	       
		//Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.research_settings, container, false);
		//Button alarm1 = (Button)findViewById(R.id.research_button1);
		
		//set up alarm 1
		return view;
		
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.research_settings);	
	}
	
}
