package edu.purdue.tada;

import android.os.Bundle;
import android.view.Window;


public class ReviewActivity extends BaseActivity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.review_layout);
				
	}
}