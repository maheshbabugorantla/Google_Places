package edu.purdue.tada;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
 
/*
 * ----ABOUT----
 * This activity is called to start to the camera when the "before" or "after" button is pushed
 */

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback{
private static final String TAG = "mCamera";
private static final int SHOW_PREVIEW = 47;
private Camera myCamera;
private SurfaceView previewSurfaceView;
private SurfaceHolder previewSurfaceHolder;
boolean previewing = false;
private String filePath;
private SensorManager sensorManager;
private Drawable image;
private Button buttonTakePicture;
private TextView angleView;
private CheckBox doNotShow;
private int doNotShowAgain = 0;

 
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       Log.d(TAG, "onCreate");

       getWindow().setFormat(PixelFormat.TRANSLUCENT);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
       setContentView(R.layout.camera_layout);
       previewSurfaceView = (SurfaceView)findViewById(R.id.previewsurface);
       previewSurfaceHolder = previewSurfaceView.getHolder();
       previewSurfaceHolder.addCallback(this);
       previewSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
       previewSurfaceHolder.setFormat(ImageFormat.NV21);
       //get user settings for tips_option 
       ActivityBridge.getInstance().setChecked1((PreferenceHelper.getTips(this)));
       //set onClickListener on the "Snap it" button 
       buttonTakePicture = (Button)findViewById(R.id.takebutton);
       
       //change the background of the camera button by Lechuan
       buttonTakePicture.setBackgroundResource(R.drawable.camera);
       buttonTakePicture.setText(" ");

       buttonTakePicture.setOnClickListener(new Button.OnClickListener(){
 
    	   @Override
    	   public void onClick(View view) {
    		   // TODO Auto-generated method stub
    		   ActivityBridge.getInstance().setAngle1(angleView.getText().toString().substring(0,2));
    		   myCamera.takePicture(null,null,jpegPictureCallback);   
    	   }});
       
       //set onClickListener on the "cancel" button 
       Button buttonCancel = (Button)findViewById(R.id.cancelbutton);
       
       //change the background of the cancel button
       buttonCancel.setBackgroundResource(R.drawable.barcode);
       buttonCancel.setText(" ");
       buttonCancel.setOnClickListener(new Button.OnClickListener(){
		@Override
		public void onClick(View view) {
			//TODO Auto-generated method stub  
			if (ActivityBridge.getInstance().getImgFlag()==1) {  
				ActivityBridge.getInstance().setImgFlag(0);  
			}else {  
				ActivityBridge.getInstance().setImgFlag(1);  
			}  
			CameraActivity.this.setResult(Activity.RESULT_CANCELED);  
			  
			finish();


		}});
       
       //set onClickListener on the "Tips" button 
       Button buttonTip = (Button)findViewById(R.id.tipbutton);
       
       //change the background of the tip button
       buttonTip.setBackgroundResource(R.drawable.tip);
       buttonTip.setText(" ");
       buttonTip.setOnClickListener(new Button.OnClickListener(){
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			dialog();
		}});
       

       
       //set onClickListener on the camera view to enable manual focus
       LinearLayout layoutBackground = (LinearLayout)findViewById(R.id.background);
       layoutBackground.setOnClickListener(new LinearLayout.OnClickListener(){

    	   @Override
    	   public void onClick(View view) {
    		   // TODO Auto-generated method stub
    		   buttonTakePicture.setEnabled(false);
    		   myCamera.autoFocus(myAutoFocusCallback);
    	   }});
   	}
   
   		AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){
   			@Override
   			public void onAutoFocus(boolean success, Camera camera) {
   				// TODO Auto-generated method stub
   				buttonTakePicture.setEnabled(true);
   			}};
	   PictureCallback jpegPictureCallback = new PictureCallback(){
 
		   @Override
		   public void onPictureTaken(byte[] data, Camera camera) {
			   // TODO Auto-generated method stub
			   try{
				   Bitmap bitmapPicture = BitmapFactory.decodeByteArray(data, 0, data.length);//create bitmap to store the captured photo
      
				   Date date = new Date();
				   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");//reset date format
				   String dateString = dateFormat.format(date).toString();//generate date string to be used as filename 
				   filePath = recSaved + "/"+ dateString + ".jpg";//filename
				   ActivityBridge.getInstance().setFilepath(filePath);//save the filename in singleton
				   //get the location information and save it in singleton
				   myGPS gps = new myGPS(CameraActivity.this);
				   if (gps.canGetLocation()) {
						double latitude = gps.getLatitude();
						double longtitude = gps.getLongitude();
						String laS = Double.toString(latitude);
						String longS = Double.toString(longtitude);
						ActivityBridge.getInstance().setLatitude1(laS);
						ActivityBridge.getInstance().setLongitude1(longS);						
					}else {
						gps.showSettingsAlert();//show an alert if location is not available
					}
				   
				   FileOutputStream b = new FileOutputStream(filePath);
				   bitmapPicture.compress(Bitmap.CompressFormat.JPEG,100,b);
				   b.flush();//write out image file on SDcard
				   b.close();//close file output stream
				   galleryAddPic();//broadcast photos in the gallery
				   
				   // preview the picture in new Activity
				   Intent previewIntent = new Intent();
				   previewIntent.setData(Uri.parse("file://"+filePath));//use Uri to send the image to preview activity
				   previewIntent.setClass(CameraActivity.this,PreviewActivity.class);
				   startActivityForResult(previewIntent, SHOW_PREVIEW);
			   }
			   catch(Exception e){
				   e.printStackTrace();
			   }
 
		   }
	   };
	   
	   @Override
	   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		   super.onActivityResult(requestCode, resultCode, data);
		   System.out.println("camera gets result:"+ resultCode);
		   if (requestCode == SHOW_PREVIEW && resultCode == Activity.RESULT_OK) {
				System.out.println("successfully return to camera");
				CameraActivity.this.setResult(RESULT_OK);
				this.finish();
			}
		   if (requestCode == SHOW_PREVIEW && resultCode == Activity.RESULT_CANCELED) {
				System.out.println("successfully return to camera");
			}
		}
	   @Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			
			if (ActivityBridge.getInstance().getImgFlag() == 1) {
				ActivityBridge.getInstance().setImgFlag(0);
			}else {
				ActivityBridge.getInstance().setImgFlag(1);
			}
			CameraActivity.this.setResult(RESULT_CANCELED);
			finish();
		}
	   
	   //broadcast photos in gallery
	   private void galleryAddPic() {
		   Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		   File f = new File(filePath);
		   Uri contentUri = Uri.fromFile(f);
		   mediaScanIntent.setData(contentUri);
		   this.sendBroadcast(mediaScanIntent);
	   }			    


	   @Override
	   public void surfaceCreated(SurfaceHolder holder) {
		   // TODO Auto-generated method stub
		   Log.d(TAG,"surfaceCreated Surface is :"+ previewSurfaceHolder.getSurface().getClass().getName());
		   myCamera = Camera.open();
   
		   try {
			   //WindowManager mWindowManager= (WindowManager)getSystemService(WINDOW_SERVICE);
			   //System.out.println(mWindowManager.getDefaultDisplay().getRotation());
	
			   myCamera.setPreviewDisplay(holder);
			   sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
			   Sensor gSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
			   sensorManager.registerListener(new SensorEventListener() {
			
				   @Override
				   public void onSensorChanged(SensorEvent event) {
					   // TODO Auto-generated method stub
					   float values[] = event.values;
					   float Z = values[2];
					   angleView = (TextView) findViewById(R.id.angleView);
					   TextView actionView = (TextView) findViewById(R.id.actionView);
					   ImageView imageView = (ImageView) findViewById(R.id.top_bottom);
					   imageView.setScaleType(ScaleType.FIT_XY);
					   
					   if (Z > 45 && Z < 60) {
						   actionView.setTextColor(Color.GREEN);
						   actionView.setText("Take Picture");
						   angleView.setTextColor(Color.GREEN);
						   angleView.setText(String.format("%d", Math.round(Z))+(char)0x00B0);
						   image = getResources().getDrawable(R.drawable.overlaygraphic_vert_green);
						   imageView.setImageDrawable(image);
					   }else {
						   actionView.setTextColor(Color.RED);
						   actionView.setText("Change angle to be between 45"+(char)0x00B0 + "and 60"+(char)0x00B0);
						   angleView.setTextColor(Color.RED);
						   angleView.setText(String.format("%d", Math.round(Z))+(char)0x00B0);
						   image = getResources().getDrawable(R.drawable.overlaygraphic_vert);
						   imageView.setImageDrawable(image);
					   }
			    
				   }
			
				   @Override
				   public void onAccuracyChanged(Sensor sensor, int accuracy) {
					   // TODO Auto-generated method stub 
				   }
			   }, gSensor, SensorManager.SENSOR_DELAY_NORMAL);
			   //if "Tips" in user settings is checked, then show tips when camera opens.
			   if (ActivityBridge.getInstance().isChecked1() && ActivityBridge.getInstance().getChecked3()) {
				 dialog();
			}
			  
		   }catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
			   myCamera.release();
			   myCamera = null;
		   }
	   }

	   @Override
	   public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		   // TODO Auto-generated method stub
		   Log.d(TAG,String.format("surfaceChanged: format = %d, w =%d, h=%d", format, w, h));
		   if (previewing) {
			   myCamera.stopPreview();
		   }
		   try {
			   Camera.Parameters parameters = myCamera.getParameters();
			   parameters.setPictureFormat(PixelFormat.JPEG);
			   //parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO); //Commented out by David Tschida 9/20/2013 (Incompatible w/ 2.2)
			   List<Size> sizes = parameters.getSupportedPreviewSizes();
			   //List<Size> sizes2 = parameters.getSupportedPictureSizes();
			   Size optimalSize = getOptimalPreviewSize(sizes, w, h);
			   parameters.setPreviewSize(optimalSize.width, optimalSize.height);
			   //parameters.setPictureSize(sizes2.get(0).width, sizes2.get(0).height);

			   myCamera.setParameters(parameters);
			   myCamera.startPreview();
			   previewing = true;
		  
			   //int bufSize = optimalSize.width * optimalSize.height * ImageFormat.getBitsPerPixel(parameters.getPreviewFormat())/8;
			   //byte[] cbBuffer = new byte[bufSize];
			   //myCamera.setPreviewCallbackWithBuffer(this);
			   //myCamera.addCallbackBuffer(cbBuffer);
		   } catch (Exception e) {
			   // TODO: handle exception
			   e.printStackTrace();
		   }

	   }

  private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.05;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;
		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
	  
		return optimalSize;
	}
  @Override
  public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
	  // TODO Auto-generated method stub
	  if (myCamera != null) {
		  myCamera.stopPreview();
		  myCamera.release();
		  myCamera = null;
		  previewing = false;
	  }
  }
  //show a dialog when users press tips button


  
  
  
  private void dialog() {
	  AlertDialog.Builder builder = new Builder(CameraActivity.this);
	  LayoutInflater inflater = LayoutInflater.from(CameraActivity.this);
	  View layout = inflater.inflate(R.layout.scroll_tips, null);
	  doNotShow = (CheckBox)layout.findViewById(R.id.DoNotShow);
	  builder.setTitle("Tips");
	  builder.setView(layout);
	  builder.setPositiveButton("OK", new OnClickListener() {
		  
		  public void onClick(DialogInterface arg0, int arg1) {
			  // TODO Auto-generated method stub
			  if (doNotShow.isChecked()) {
				  ActivityBridge.getInstance().setChecked3();
				  PreferenceHelper.setTips(CameraActivity.this, false);
			  }
			  arg0.dismiss();
		  }});	

	  builder.create().show();
	  }
  
}