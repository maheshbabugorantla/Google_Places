package edu.purdue.tada;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dm.zbar.android.scanner.*;


import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/* Spring 2014
 * 	----ABOUT----
 *	This is the activity that is called when a barcode is successfully scanned.
 *  It will display two buttons, "keep" and "retake."
 */

public class ScanResult extends BaseActivity{
	//add text view and set the text field to the sent string
	//add two buttons, one to retake and the other to keep
	Button retake;
	Button keep;
	TextView code;
	private static final int KEEP_RESULT = 99;
	private static final int RETAKE_RESULT = 98;
	private static final int REQUEST_BAR_CODE = 1234;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_result_layout);
		//---CREATE BUTTONS
		retake = (Button) findViewById(R.id.retake_btn);
		keep = (Button) findViewById(R.id.keep_btn);
		//---CREATE TEXT FIELD
		code = (TextView) findViewById(R.id.code_text);
		//---SET TEXT OF TEXT FIELD TO BARCODE
		Intent intent = getIntent();
		final String data = intent.getStringExtra("SCAN_RESULT");
		code.setText(data);
		
		//---WHAT HAPPENS WHEN RETAKE IS CLICKED
		retake.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				//result loops back to camera
				setResult(RETAKE_RESULT);
				finish();
			}
		});
		
		//---WHAT HAPPENS WHEN KEEP IS CLICKED
		keep.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				//adds barcode to arraylist and returns to title screen
				setResult(KEEP_RESULT);
				ActivityBridge.getInstance().setBarCode(data);
				finish();
			}
		});
	}
	
	
	
}