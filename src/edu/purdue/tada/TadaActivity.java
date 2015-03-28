package edu.purdue.tada;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.dm.zbar.android.scanner.*;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/*  Spring 2014
 * -----ABOUT-----
 *  TadaActivity is the "Main Page" of the app. It is the activity that house he before and after buttons.
 *  -----IMPORTANT!!!!-----
 *  // HEY! GO TO https://github.com/DushyanthMaguluru/ZBarScanner TO GET THE ZBAR LIBRARY ON YOUR INDIVIDUAL MACHINE
 */

public class TadaActivity extends BaseFragment
{
	private final String TAG = "TadaActivity";
	
	private ImageButton img_before;
	private ImageButton img_after;
	private static final int TAKE_PHOTO = 43;
	private static final int UPLOAD_UNSENT = 59;
	private static final int SCAN_RESULT_REQUEST = 1001;
	private static final int ZBAR_SCANNER_REQUEST = 0;
	private static final int KEEP_RESULT = 99;
	private static final int RETAKE_RESULT = 98;
	private Button img_scanner;
	private Button unsent;
	private String unsentRec; // moved to oncreate.
	
	private final String REC_SAVED = "/rec_unsent.txt"; 
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		//Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.activity_tada, container, false);
		unsentRec = "" + recSaved + REC_SAVED; // Added By David to fix crash 9/24/2013
		//img_scanner = (Button) view.findViewById(R.id.scan);
		unsent = (Button) view.findViewById(R.id.unsent_event);
		System.out.println("Tada: on create!");
		// Initialize imgFlag
		// ActivityBridge.getInstance().setImgFlag(ActivityBridge.getInstance().getImgFlag());
		// flag = 0:imageButton1 is available for taking photos;
		// flag = 1:imageButton2 is available for taking photos
		// Set up "unsent event" button
		try
		{
			Log.d(TAG, "unsentRec: " + unsentRec);
			
			FileInputStream fis = null;
			fis = new FileInputStream(unsentRec);
			BufferedReader buffreader = new BufferedReader(
					new InputStreamReader(fis));
			// String data = "";
			String line = buffreader.readLine();
			List<String> unsentList = new ArrayList<String>();
			System.out.println("in the tada::: show TXT");
			while (line != null)
			{
				unsentList.add(line);
				System.out.println(line);
				line = buffreader.readLine();
				// data = data + line;
			}
			System.out.println("in the tada::: end of TXT");
			fis.close();
			System.out.println("list size::::" + unsentList.size());
			if ((unsentList.size()) != 0)
			{
				unsent.setText(String.format("%d", unsentList.size())
						+ " unsent event(s)");
				unsent.setVisibility(View.VISIBLE);
			} else
			{
				unsent.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e)
		{
			Log.v(TAG, "File not found. Continuing...");
			//e.printStackTrace();
			
		}
		// set OnClickListener for "unsent event" button
		unsent.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// read the data in "rec_unsent" into a list(unsentList)
				List<String> unsentList = new ArrayList<String>();
				try
				{
					FileInputStream fis = null;
					fis = new FileInputStream(unsentRec);
					BufferedReader buffreader = new BufferedReader(
							new InputStreamReader(fis));
					String line = buffreader.readLine();
					while (line != null)
					{
						unsentList.add(line);
						line = buffreader.readLine();
					}
					fis.close();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				
				String filepath = unsentList.get(0);// save the first line as
													// "filepath", which will be
													// deleted later.
				
				String gpspath = filepath.substring(0,
						filepath.lastIndexOf("."))
						+ ".gps";
				
				// open the rec file according to "filepath"
				File unsentFile = new File(filepath);
				// open the gps file according to "gpspath"
				File gpsFile = new File(gpspath);
				
				if (unsentFile.exists())
				{
					// if the rec file you want to open exists, then
					if (isNetworkConnected())
					{
						// if the Internet is accessible,
						// read the fourth and fifth lines in the rec file into
						// singleton.
						try
						{
							FileInputStream fis = null;
							fis = new FileInputStream(unsentFile);
							BufferedReader buffreader = new BufferedReader(
									new InputStreamReader(fis));
							String[] data = { "", "", "", "", "" };
							String line = buffreader.readLine();
							int lineNum = 0;
							while (line != null)
							{
								data[lineNum] = line;
								line = buffreader.readLine();
								lineNum++;
							}
							fis.close();
							String imageFolder = recSaved
									+ "/";// this is the folder
															// containing meal
															// images
							ActivityBridge.getInstance().setFilepath2(
									imageFolder + data[3]);// this is the file
															// path to the first
															// image
							ActivityBridge.getInstance().setFilepath(
									imageFolder + data[4]);// this is the file
															// path to the
															// second image
						} catch (Exception e)
						{
							e.printStackTrace();
						}
						// read the third and fourth lines in the gps file into
						// singleton.
						try
						{
							FileInputStream fis = null;
							fis = new FileInputStream(gpsFile);
							BufferedReader buffreader = new BufferedReader(
									new InputStreamReader(fis));
							String[] data = { "", "", "", "" };
							String line = buffreader.readLine();
							int lineNum = 0;
							while (line != null)
							{
								data[lineNum] = line;
								line = buffreader.readLine();
								lineNum++;
							}
							fis.close();
							// update longitude and latitude of the first image
							ActivityBridge.getInstance().setLongitude2(
									data[2].substring(0,
											data[2].lastIndexOf(",")));
							ActivityBridge.getInstance()
									.setLatitude2(
											data[2].substring(data[2]
													.lastIndexOf(",") + 1));
							// update longitude and latitude of the second image
							ActivityBridge.getInstance().setLongitude1(
									data[3].substring(0,
											data[3].lastIndexOf(",")));
							ActivityBridge.getInstance()
									.setLatitude1(
											data[3].substring(data[3]
													.lastIndexOf(",") + 1));
							
						} catch (Exception e)
						{
							e.printStackTrace();
						}
						// rewrite unsentRec.txt: delete the first line, update
						// the list
						try
						{
							FileOutputStream fos = null;
							File unsentRec = new File(recSaved,
									"rec_unsent.txt");
							// create a new file called "rec_unsent.txt"
							if (!unsentRec.exists())
							{
								unsentRec.createNewFile();
							} else
							{
								unsentRec.delete();
								unsentRec.createNewFile();
							}
							fos = new FileOutputStream(unsentRec, true);
							// delete the first item in the list, namely delete
							// "filepath"
							unsentList.remove(filepath);
							// rewrite other items into the new file
							for (String temp : unsentList)
							{
								fos.write((temp + "\n").getBytes());
							}
							fos.close();
							// display a message showing that txt file is
							// updated
							Toast.makeText(getActivity().getApplicationContext(),
									"TXT file is updated!", 1000).show();
						} catch (Exception e)
						{
							e.printStackTrace();
						}
						// after everything is saved in singleton, call
						// httpssendimage to upload images to the server
						Intent intent = new Intent();
						intent.setClass(getActivity(), HttpsSendImage.class);
						startActivityForResult(intent, UPLOAD_UNSENT);
					} else
					{
						Toast.makeText(getActivity().getApplicationContext(), "No Internet!",
								500).show();
					}
				} else
				{
					// when the file you try to open does not exist, then
					// rewrite unsentRec.txt: delete the first line, update the
					// list
					try
					{
						
						FileOutputStream fos = null;
						File unsentRec = new File(recSaved, "rec_unsent.txt");
						if (!unsentRec.exists())
						{
							unsentRec.createNewFile();
						} else
						{
							unsentRec.delete();
							unsentRec.createNewFile();
						}
						fos = new FileOutputStream(unsentRec, true);
						unsentList.remove(filepath);
						for (String temp : unsentList)
						{
							fos.write(temp.getBytes());
						}
						fos.close();
						if ((unsentList.size()) > 0)
						{
							unsent.setText(String.format("%d",
									unsentList.size())
									+ " unsent event(s)");
						} else
						{
							unsent.setVisibility(View.INVISIBLE);
						}
						Toast.makeText(getActivity().getApplicationContext(),
								"TXT file is updated!", 1000).show();
						
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});
		
		img_before = (ImageButton) view.findViewById(R.id.imageButton1);
		img_after = (ImageButton) view.findViewById(R.id.imageButton2);
		
		//WHAT HAPPENS WHEN THE 'SCAN' BUTTON IS CLICKED
		img_scanner.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callBarCode();
				//ZBAR_SCANNER_REQUEST = 0
				
			}
			
			
		});
		//WHAT HAPPENS WHEN THE 'BEFORE' BUTTON IS CLICKED
		img_before.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				System.out.println("0:::"
						+ ActivityBridge.getInstance().getImgFlag());
				if (ActivityBridge.getInstance().getImgFlag() == 0)
				{
					ActivityBridge.getInstance().setImgFlag(1);
					Intent intent = new Intent();
					intent.setClass(getActivity(), CameraActivity.class);
					startActivityForResult(intent, TAKE_PHOTO);
				} else
				{
					// create a dialog to warn users if they want to replace the
					// first image
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					//builder.setTitle("You have already taken the first image. Do you want to replace it?");
					//The setMessage should be used since it allows more characters Nicole Missele 2/8/2014
					builder.setMessage("You have already taken the first image. Do you want to replace it?");
					builder.setPositiveButton("Yes",
							new DialogInterface.OnClickListener()
							{
								
								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									Intent intent = new Intent();
									intent.setClass(getActivity(),
											CameraActivity.class);
									startActivityForResult(intent, TAKE_PHOTO);
								}
							});
					builder.setNegativeButton("No",
							new DialogInterface.OnClickListener()
							{
								
								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									dialog.dismiss();
								}
							});
					builder.create().show();
				}
				
			}
		});
		

		//WHAT HAPPENS AFTER THE 'AFTER' BUTTON IS CLICKED
		img_after.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (ActivityBridge.getInstance().getImgFlag() == 0)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setTitle("You have not taken the first image yet");
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener()
							{
								
								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									dialog.dismiss();
								}
							});
					builder.create().show();
				} else
				{
					ActivityBridge.getInstance().setImgFlag(0);
					Intent intent = new Intent();
					intent.setClass(getActivity(), CameraActivity.class);
					startActivityForResult(intent, TAKE_PHOTO);
				}
				
			}
		});
	    
		
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		Button unsent = (Button) getView().findViewById(R.id.unsent_event);
		
		try
		{
			FileInputStream fis = null;
			fis = new FileInputStream(unsentRec);
			BufferedReader buffreader = new BufferedReader(
					new InputStreamReader(fis));
			// String data = "";
			String line = buffreader.readLine();
			List<String> unsentList = new ArrayList<String>();
			
			while (line != null)
			{
				unsentList.add(line);
				System.out.println(line);
				line = buffreader.readLine();
			}
			fis.close();
			if ((unsentList.size()) != 0)
			{
				unsent.setText(String.format("%d", unsentList.size())
						+ " unsent event(s)");
				unsent.setVisibility(View.VISIBLE);
			} else
			{
				unsent.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e)
		{
			Log.v(TAG, "File not found. Continuing...");
			//e.printStackTrace();
			//TODO: Move this block and onCrate to method. 
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
			super.onActivityResult(requestCode, resultCode, data);
			System.out.println("tada gets result:" + resultCode);
			
			//returns here from 'before/after' cameras
			if (requestCode == TAKE_PHOTO || requestCode == UPLOAD_UNSENT)
			{
				System.out.println("successfully return to main");
			}
			//what happened when 'scan' button is pressed. 
			if(ActivityBridge.getInstance().getChecked4()) {
				callBarCode();
			}
			
			//returns here from barcode scanner
			if(requestCode == ZBAR_SCANNER_REQUEST){
				if (resultCode == getActivity().RESULT_OK){
					Intent scanIntent = new Intent(getActivity(), ScanResult.class);
					scanIntent.putExtra("SCAN_RESULT", data.getStringExtra(ZBarConstants.SCAN_RESULT));
					startActivityForResult(scanIntent, SCAN_RESULT_REQUEST);
					
					// ---IMPORTANT---
					// Scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT)
					// Type of the scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT_TYPE)
					
		        
				} else if(resultCode == getActivity().RESULT_CANCELED) {
					Toast.makeText(getActivity(), "Picture Cancelled", Toast.LENGTH_SHORT).show();
				}
			}
			
			//Returns here from ScanResult Activity
			if(requestCode == SCAN_RESULT_REQUEST)
			{
				if(resultCode == KEEP_RESULT){
					//dont do anything
				}else if(resultCode == RETAKE_RESULT){
					//if retake pressed, call the zbar activity again
					Intent intent = new Intent(getActivity(), ZBarScannerActivity.class);
					startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
				}
				
			}
	}
	
	private boolean isNetworkConnected()
	{
		ConnectivityManager conManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		return (conManager.getActiveNetworkInfo() != null);
	}
	
	//Spring 2014
	//Calls bar code scanner
	// To implement surface overlay on camera, since zbarscanneractivity resides within the jar, will need to pull that code out and modify it.
	public void callBarCode(){
		ActivityBridge.getInstance().setChecked4();
		Intent intent = new Intent(getActivity(), ZBarScannerActivity.class);
		startActivityForResult(intent, ZBAR_SCANNER_REQUEST);//ZBAR_SCANNER_REQUEST = 0
	}
	
	// If the fragment is visible, set the orientation to portrait
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if(isVisibleToUser) {
	        if(getActivity() != null) getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
	}
	
}
