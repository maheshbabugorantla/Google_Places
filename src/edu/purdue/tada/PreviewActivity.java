package edu.purdue.tada;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PreviewActivity extends Activity {
	private ImageView mImageView;
	private Bitmap bitmap;
	private static final int UPLOAD_IMAGES = 53;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.preview_layout);
		
		mImageView = (ImageView) findViewById(R.id.imageViewNew);  
        Button useButton = (Button)findViewById(R.id.previewButton1);
        Button retakeButton = (Button)findViewById(R.id.previewButton2);
        useButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    				
				int flag = ActivityBridge.getInstance().getImgNum();
				if (flag == 0) {
					ActivityBridge.getInstance().setImgNum(1);
					//copy the first filepath to filepath2
					ActivityBridge.getInstance().setFilepath2(ActivityBridge.getInstance().getFilepath());
					
					//copy the longitude and latitude saved in singleton to longitude2 and latitude2
					ActivityBridge.getInstance().setLatitude2(ActivityBridge.getInstance().getLatitude1());
					ActivityBridge.getInstance().setLongitude2(ActivityBridge.getInstance().getLongitude1());
					
					//copy the first shooting angle to angle2
					ActivityBridge.getInstance().setAngle2(ActivityBridge.getInstance().getAngle1());
					
					//return to main activity
					PreviewActivity.this.setResult(RESULT_OK);
					finish();
					/*Intent intent = new Intent();
					intent.setClass(PreviewActivity.this,TabGroup.class);
					startActivity(intent);*/
				}
				else if (flag == 1) {
					ActivityBridge.getInstance().setImgNum(0);
					if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
						FileOutputStream fos = null;
						//generate date and device ID to be used as the file name of .rec 
						Date date = new Date();
				       	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
				       	String dateString = dateFormat.format(date).toString();
				       	String deviceID = Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
				       	//retrieve data from singleton
				       	String filepath1 = ActivityBridge.getInstance().getFilepath2();//get the file path of image1
				        String filepath2 = ActivityBridge.getInstance().getFilepath();//get the file path of image2
				        
				        //alternative::String dateString = filepath1.substring(filepath1.lastIndexOf("/")+1).substring(0,19);
				        //folder name in which rec and gps files will be saved.
				       	String recSaved = Environment.getExternalStorageDirectory().getPath();
				       	//prepare file names for writing out .rec and .gps
				        String recFileName = deviceID + "_"+ dateString + ".rec";//.rec file name
				        String gpsFileName = deviceID + "_"+ dateString + ".gps";//.gps file name
				        
				        //retrieve location data from singleton
				        String laS1 = ActivityBridge.getInstance().getLatitude2();
				        String longS1 = ActivityBridge.getInstance().getLongitude2();
				        String laS2 = ActivityBridge.getInstance().getLatitude1();
				        String longS2 = ActivityBridge.getInstance().getLongitude1();
				        				        
				        
				        	try {
						        //write out rec file on SDcard
						        File myRec = new File(recSaved,recFileName);
						        if (!myRec.exists()) {
									myRec.createNewFile();
								}
						        
						        fos = new FileOutputStream(myRec);
						        String text = deviceID + "\r\n"+dateString + "\r\n"+"2\r\n"+
						        		filepath1.substring(filepath1.lastIndexOf("/")+1)+"\r\n"+
						        		filepath2.substring(filepath2.lastIndexOf("/")+1)+"\r\n";
						        fos.write(text.getBytes());
						        fos.close();
						        Toast.makeText(getApplicationContext(), "REC file is saved!", 1000).show();
						        						
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
				        	
				        	try {
						        //write out GPS file on SDcard
						        File myGps = new File(recSaved,gpsFileName);
						        if (!myGps.exists()) {
									myGps.createNewFile();
								}
						        
						        fos = new FileOutputStream(myGps);
						        String text = deviceID + "\r\n" + "3\r\n" + 
						        		laS1+","+longS1+"\r\n"+laS2+","+longS2 + "\r\n";		
						        fos.write(text.getBytes());
						        fos.close();
						        Toast.makeText(getApplicationContext(), "GPS file is saved!", 1000).show();
						        						
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
				        	
				        	//if the Internet is available, upload the images 
				        	if (isNetworkConnected()) {	
				        		Intent intent = new Intent();
				        		intent.setClass(PreviewActivity.this,HttpsSendImage.class);
				        		startActivityForResult(intent, UPLOAD_IMAGES);
				        	}else {
				        		//if not, list the unsent event in "rec_unsent.txt"
				        		try {
							        //write the file path as one item in "rec_unsent.txt"
				        			//format in rec_unsent.txt
				        			//filepath/filename1.rec
				        			//filepath/filename2.rec
					        		String rec_unsent = "rec_unsent.txt";
							        File unsentRec = new File(recSaved,rec_unsent);
							        if (!unsentRec.exists()) {
										unsentRec.createNewFile();
									}
							        
							        fos = new FileOutputStream(unsentRec,true);
							        String text = recSaved+"/"+recFileName+"\n";		
							        fos.write(text.getBytes());
							        fos.close();
							        						        						
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
					        	try {
									FileInputStream fis = null;
									fis = new FileInputStream(recSaved+"/rec_unsent.txt");
									BufferedReader buffreader = new BufferedReader(new InputStreamReader (fis)) ;
									String data = "";
						            String line = buffreader.readLine();
						            List<String> unsentList = new ArrayList<String>();
						            System.out.println("in the preview activity: show the TXT file:::");
						            while ( line != null ) {
						                unsentList.add(line);
						                data = data + line; 
						                System.out.println(line);
						                line = buffreader.readLine();
						            }
						            System.out.println("in the preview activity: end of TXT");
						            fis.close();
						            
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
				        		Toast.makeText(getApplicationContext(), "No Internet!", 1000).show();
				        		//Intent intent = new Intent();
				        		PreviewActivity.this.setResult(RESULT_OK);
				        		finish();
							}	
					}	
				}
			}});
        
        retakeButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
        
        Intent mIntent = getIntent();         
        Uri pathUri = mIntent.getData();
        //setTitle(pathUri.toString());
        try {
			bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), pathUri);
			BitmapDrawable mBitmapDrawable = new BitmapDrawable(bitmap);  
	        mImageView.setBackgroundDrawable(mBitmapDrawable);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("preview gets result:"+resultCode);
		if (requestCode == UPLOAD_IMAGES) {
			System.out.println("successfully return to preview");
			PreviewActivity.this.setResult(RESULT_OK);
			this.finish();
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder builder = new Builder(PreviewActivity.this);
		builder.setTitle("Are you sure to discard this photo?");
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				PreviewActivity.this.setResult(RESULT_CANCELED);
				finish();
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
		//super.onBackPressed();
	}
	
	private boolean isNetworkConnected(){
		ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		return(conManager.getActiveNetworkInfo() != null);
	}
}