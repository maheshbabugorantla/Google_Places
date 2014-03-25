package edu.purdue.tada;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dm.zbar.android.scanner.*;

//HEY! GO TO https://github.com/DushyanthMaguluru/ZBarScanner TO GET THE ZBAR LIBRARY ON YOUR INDIVIDUAL MACHINE
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
		retake = (Button) findViewById(R.id.retake_btn);
		keep = (Button) findViewById(R.id.keep_btn);
		code = (TextView) findViewById(R.id.code_text);
		Intent intent = getIntent();
		final String data = intent.getStringExtra("SCAN_RESULT");
		code.setText(data);
		
		retake.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				setResult(RETAKE_RESULT);
				finish();
				//return value that would go back to camera
			}
		});
		keep.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				setResult(KEEP_RESULT);
				
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");//reset date format
				String dateString = dateFormat.format(date).toString();//generate date string to be used as filename 
				String filePath = data + "/"+ dateString + ".jpg";//filename
				//ActivityBridge.getInstance().setFilepath(filePath);
				
				//Intent intent = new Intent();
			//	intent.setClass(ScanResult.this, HttpsSendImage.class);
			//	intent.putExtra("requestCode", REQUEST_BAR_CODE);
				ActivityBridge.getInstance().setBarCode(data);
			//	startActivityForResult(intent, REQUEST_BAR_CODE);
				//also, put on Toast that it was saved
				finish();
			}
		});
	}
	
	
	
	
	
	
	
	
}