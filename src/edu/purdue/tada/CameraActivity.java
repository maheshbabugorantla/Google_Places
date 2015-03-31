package edu.purdue.tada;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Paint;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
//import org.opencv.core.Core;
//import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Core;
//import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
//import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
//import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
//import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

 
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
private Mat Mat1;
private Mat Mat2;
private MatOfDMatch matches;
//private MatOfDMatch gm;
private MatOfKeyPoint keypoints_object;
private MatOfKeyPoint keypoints_scene;
private Mat descriptors_object;
private Mat descriptors_scene;
private MatOfPoint2f obj;
private MatOfPoint2f scene;
private LinkedList<DMatch> good_matches;
private LinkedList<Point> objList;
private LinkedList<Point> sceneList;
private FeatureDetector fd;
private DescriptorExtractor extractor;
private DescriptorMatcher matcher;
private Mat H;
//private Mat obj_corners;
//private Mat scene_corners;
//private Mat img_matches;
//private Scalar scale5;
//private Scalar scale6;
//private MatOfByte dat;

//quality detection
private BitmapFactory.Options opt;
private Mat matImage;
private Mat matImageGrey;
private Mat dst2;
private Mat laplacianImage;
private Mat laplacianImage8bit;

private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

	  //By downloading, copying, installing or using the software you agree to this license.
	  //If you do not agree to this license, do not download, install, copy or use the software.
	  //
	  //
	  //								License Agreement
	  //					For Open Source Computer Vision Library
	  //							(3-clause BSD License)
	  //
	  //
	  //		*Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
	  //		*Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
	  //		*Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
	  //
	  //
	  //Neither the names of the copyright holders nor the names of the contributors may be used to endorse or promote products derived from this software without specific prior written permission.
	  //This software is provided by the copyright holders and contributors “as is” and any express or implied warranties, including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are disclaimed. In no event shall copyright holders or contributors be liable for any direct, indirect, incidental, special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability, whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of
	  //the use of this software, even if advised of the possibility of such damage.
    @Override
    public void onManagerConnected(int status) {
        switch (status) {
            case LoaderCallbackInterface.SUCCESS:
            {
                Log.i(TAG, "OpenCV loaded successfully");
                
                Mat1 = new Mat();
                Mat2 = new Mat();
                matches = new MatOfDMatch();
      		  	//gm = new MatOfDMatch();
      		  	keypoints_object = new MatOfKeyPoint();
      		  	keypoints_scene = new MatOfKeyPoint();
      		  	descriptors_object = new Mat();
      		  	descriptors_scene = new Mat();
      		  	obj = new MatOfPoint2f();
      		  	scene = new MatOfPoint2f();
      		  	good_matches = new LinkedList<DMatch>();
      		  	objList = new LinkedList<Point>();
      		  	sceneList = new LinkedList<Point>();
    		   // obj_corners = new Mat(4,1,CvType.CV_32FC2);
    		    //scene_corners = new Mat(4,1,CvType.CV_32FC2);
    		    
    		    //img_matches = new Mat();
                //scale5 = new Scalar(255,0,0);
                //scale6 = new Scalar(0,0,255);
                //dat = new MatOfByte();
      		  	
                //change features and test
      		  	fd = FeatureDetector.create(FeatureDetector.GRID_FAST);
      		  	extractor = DescriptorExtractor.create(DescriptorExtractor.OPPONENT_FREAK);
      		  	matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
      		  	
      		  	//quality detection
      		  	opt = new BitmapFactory.Options();
      		  	matImage = new Mat();
      		  	matImageGrey = new Mat();
      		  	dst2 = new Mat();
      		  	laplacianImage = new Mat();
      		  	laplacianImage8bit = new Mat();
            } break;
            default:
            {
                super.onManagerConnected(status);
            } break;
        }
    }
};

	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
	}

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
       buttonTakePicture.setOnClickListener(new Button.OnClickListener(){
 
    	   @Override
    	   public void onClick(View view) {
    		   // TODO Auto-generated method stub
    		   ActivityBridge.getInstance().setAngle1(angleView.getText().toString().substring(0,2));
    		   myCamera.takePicture(null,null,jpegPictureCallback);   
    	   }});
       //set onClickListener on the "cancel" button 
       Button buttonCancel = (Button)findViewById(R.id.cancelbutton);
       buttonCancel.setOnClickListener(new Button.OnClickListener(){
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
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
				   
				   Boolean quality_check = quality_detect(bitmapPicture);
//				   bitmapPicture = quality(bitmapPicture);
				   Boolean fm_check = fmHomography(bitmapPicture);
				   
				   //storing the laplace image in gallery
				   String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); // temporary
				   Bitmap combination = quality(bitmapPicture); // temporary
				   //save in gallery
				   
				   MediaStore.Images.Media.insertImage(getContentResolver(),combination,"test_"+ timeStamp + ".jpg",timeStamp.toString()); // temporary
				   
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
				   
				   //just for demo
				   Context context = getApplicationContext();
				   CharSequence text = " ";
				   int duration = Toast.LENGTH_SHORT;
				   
				   if(quality_check)
				   {
					   if (fm_check)
					   {
						   text = "FM Detected!" ;
					   } 
					   else
					   {
						   text = "FM not Detected!";
					   }
				   }
				   else
				   {
					   text = "Image is Blurred, please retake the image";
				   }
				   Toast toast = Toast.makeText(context, text, duration);
				   toast.show();
				   
				   // preview the picture in new Activity
				   Intent previewIntent = new Intent();
				   //previewIntent.setData(Uri.parse("file://"+filePath));//use Uri to send the image to preview activity
				   previewIntent.setData(Uri.parse("file://"+filePath));
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
			   if (ActivityBridge.getInstance().isChecked1()) {
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
	  builder.setTitle("Tips");
	  builder.setView(LayoutInflater.from(this).inflate(R.layout.scroll_tips,null));
	  builder.setPositiveButton("OK", new OnClickListener() {
		  @Override
		  public void onClick(DialogInterface arg0, int arg1) {
			  // TODO Auto-generated method stub
			  arg0.dismiss();
		  }});
	  builder.create().show();
  }

  
  //By downloading, copying, installing or using the software you agree to this license.
  //If you do not agree to this license, do not download, install, copy or use the software.
  //
  //
  //								License Agreement
  //					For Open Source Computer Vision Library
  //							(3-clause BSD License)
  //
  //
  //		*Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
  //		*Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
  //		*Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
  //
  //
  //Neither the names of the copyright holders nor the names of the contributors may be used to endorse or promote products derived from this software without specific prior written permission.
  //This software is provided by the copyright holders and contributors “as is” and any express or implied warranties, including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are disclaimed. In no event shall copyright holders or contributors be liable for any direct, indirect, incidental, special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability, whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of
  //the use of this software, even if advised of the possibility of such damage.
  
  
  
  public Boolean fmHomography(Bitmap input) {	  
	  //Bitmap i_object = BitmapFactory.decodeResource(getResources(), R.drawable.fmimage);
	  //Utils.bitmapToMat(i_object, img_object);
	  //Utils.bitmapToMat(i_scene, img_scene);
	  
	 try{
	  		//Mat2 = Highgui.imread(filePath, Highgui.CV_LOAD_IMAGE_COLOR);
	  
	  		//load object
	  		Bitmap i_object = BitmapFactory.decodeResource(getResources(),R.drawable.fmimage);
	  		//Bitmap i_object_gray = toGrayscale(i_object);
	  		//Utils.bitmapToMat(i_object_gray, Mat1);
	  		Utils.bitmapToMat(i_object, Mat1);
	  		
		
	  		//load scene
	  		//Bitmap input_gray = toGrayscale(input);
		  	//Utils.bitmapToMat(input_gray, Mat2);
		  	Utils.bitmapToMat(input, Mat2);
		  	
		  	//changing the color of the images
		  	Imgproc.cvtColor(Mat2, Mat2, Imgproc.COLOR_RGBA2RGB);
		  	Imgproc.cvtColor(Mat1, Mat1, Imgproc.COLOR_RGBA2RGB);
		  	
		  	//Imgproc.cvtColor( Mat1, Mat1, Imgproc.COLOR_RGBA2GRAY );
		  	//Imgproc.cvtColor( Mat2, Mat2, Imgproc.COLOR_RGBA2GRAY );

		  	//changing the size of the image
		  	org.opencv.core.Size fsize = new org.opencv.core.Size(5,5);
		  	//org.opencv.core.Size nsize = new org.opencv.core.Size(800,400);
		  	//org.opencv.core.Size nsize2 = new org.opencv.core.Size(200,168);
		  	
		  	//Imgproc.resize(Mat2, Mat2, nsize);
		  	//Imgproc.resize(Mat1, Mat1, nsize2);
		  	
		  	Imgproc.blur(Mat2, Mat2, fsize);
		  	Imgproc.blur(Mat1, Mat1, fsize);
		  	
		    fd.detect( Mat1, keypoints_object );
		    fd.detect( Mat2, keypoints_scene );
		  
		    //– Step 2: Calculate descriptors (feature vectors)
		    extractor.compute( Mat1, keypoints_object, descriptors_object );
		    extractor.compute( Mat2, keypoints_scene, descriptors_scene );

		    Mat1.release();
		    Mat2.release();
		    i_object.recycle();
		    
		    Mat1 = null;
		    Mat2 = null;
		    i_object = null;
		    
		    matcher.match( descriptors_object, descriptors_scene, matches);
		    
		    descriptors_scene.release();
		    descriptors_scene = null;

		    double max_dist = 0; 
		    double min_dist = 100;
		  
		    
		    
		    List<DMatch> matchesList = matches.toList();
		    
		    matches.release();
		    matches = null;

		    //– Quick calculation of max and min distances between keypoints
		    for( int i = 0; i < descriptors_object.rows(); i++ ){
		    	Double dist = (double) matchesList.get(i).distance;
		    	if( dist < min_dist ) min_dist = dist;
		    	if( dist > max_dist ) max_dist = dist;
		    }

		    for(int i = 0; i < descriptors_object.rows(); i++){
		    	if(matchesList.get(i).distance < 3*min_dist){
		    		good_matches.addLast(matchesList.get(i));
		    	}
		    }

		    descriptors_object.release();
		    descriptors_object = null;
		    //matchesList.free();
		    
		    //gm.fromList(good_matches);
		    
		    //Features2d.drawMatches( Mat1, keypoints_object, Mat2, keypoints_scene, gm, img_matches);

		    List<KeyPoint> keypoints_objectList = keypoints_object.toList();
		    List<KeyPoint> keypoints_sceneList = keypoints_scene.toList();

		    for(int i = 0; i<good_matches.size(); i++){
		    	objList.addLast(keypoints_objectList.get(good_matches.get(i).queryIdx).pt);
		    	sceneList.addLast(keypoints_sceneList.get(good_matches.get(i).trainIdx).pt);
		    }
		    
		    keypoints_object.release();
		    keypoints_scene.release();
		    keypoints_scene = null;
		    keypoints_object = null;
		  
		    obj.fromList(objList);
		    scene.fromList(sceneList);
		    
		    //objList.removeAll(objList);
		    //sceneList.removeAll(sceneList);
		    //objList = null;
		    //sceneList = null;
		    //good_matches.remove();
		    //keypoints_objectList.remove();
		    //keypoints_sceneList.remove();

		    //change values and test
		    H = Calib3d.findHomography(obj, scene,Calib3d.RANSAC,3);
		    
		    //obj.release();
		    //obj = null;
		   // scene.release();
		    //scene = null;
		    

		    //obj_corners.put(0, 0, new double[] {0,0});
		    //obj_corners.put(1, 0, new double[] {Mat1.cols(),0});
		    //obj_corners.put(2, 0, new double[] {Mat1.cols(),Mat1.rows()});
		    //obj_corners.put(3, 0, new double[] {0,Mat1.rows()});
		    
		    //Core.perspectiveTransform(obj_corners,scene_corners, H);
		    
		    //Core.line(img_matches, new Point(scene_corners.get(0,0)), new Point(scene_corners.get(1,0)), new Scalar(0, 255, 0),4);
		    //Core.line(img_matches, new Point(scene_corners.get(1,0)), new Point(scene_corners.get(2,0)), new Scalar(0, 255, 0),4);
		    //Core.line(img_matches, new Point(scene_corners.get(2,0)), new Point(scene_corners.get(3,0)), new Scalar(0, 255, 0),4);
		    //Core.line(img_matches, new Point(scene_corners.get(3,0)), new Point(scene_corners.get(0,0)), new Scalar(0, 255, 0),4);
		    
		    //Point c1 = new Point(scene_corners.get(0,0));
		    //Point c2 = new Point(scene_corners.get(1,0));
		    //Point c3 = new Point(scene_corners.get(2,0));
		    //Point c4 = new Point(scene_corners.get(3,0));
		    
		    //c1.x = c1.x + Mat1.cols();
		   // c2.x = c2.x + Mat1.cols();
		    //c3.x = c3.x + Mat1.cols();
		    //c4.x = c4.x + Mat1.cols();
		    
		    //Core.line(img_matches, c1, c2, new Scalar(0, 255, 0), 4);
		    //Core.line(img_matches, c2, c3, new Scalar(0, 255, 0), 4);
		    //Core.line(img_matches, c3, c4, new Scalar(0, 255, 0), 4);
		    //Core.line(img_matches, c4, c1, new Scalar(0, 255, 0), 4); 

		    //Bitmap out = Bitmap.createBitmap(Mat2.cols(),Mat2.rows(),Bitmap.Config.ARGB_8888); // this creates a MUTABLE bitmap;
		    //Utils.matToBitmap(Mat2, out);
		    
		  		    
		    //Bitmap out = Bitmap.createBitmap(img_matches.cols(),img_matches.rows(),Bitmap.Config.ARGB_8888); // this creates a MUTABLE bitmap;
		    //Utils.matToBitmap(img_matches, out);
		  
		    //H.release();
		    //H = null;
		    
		    if(good_matches.size() > 20)  //  >= 43 ideal
		    {
		    	Log.i(TAG, "FM detected");
		    	return true;
		    }
		    else
		    {
		    	Log.i(TAG, "FM not detected");
		    	return false;
		    }
		    	
	  	} catch (Exception e){
	  		Log.i(TAG, "FM not detected");
	  		return false;
	  	}	   
	 
  }
  
  public Boolean quality_detect(Bitmap image) {
	  Bitmap destImage;
	  Bitmap bmp;
	  int maxLap = -16777216;
	  int soglia = -6118750;
	  int l = CvType.CV_8UC1; //8-bit grey scale image
	  
	  opt.inDither = true;
	  opt.inPreferredConfig = Bitmap.Config.ARGB_8888;

	  Utils.bitmapToMat(image, matImage);
	  Imgproc.cvtColor(matImage, matImageGrey, Imgproc.COLOR_BGR2GRAY);

	  destImage = Bitmap.createBitmap(image);             

	  Utils.bitmapToMat(destImage, dst2);
	  dst2.convertTo(laplacianImage, l);
	  Imgproc.Laplacian(matImageGrey, laplacianImage, CvType.CV_8U);

	  laplacianImage.convertTo(laplacianImage8bit, l);

	  bmp = Bitmap.createBitmap(laplacianImage8bit.cols(),laplacianImage8bit.rows(), Bitmap.Config.ARGB_8888);
	  Utils.matToBitmap(laplacianImage8bit, bmp);
	  
	  int[] pixels = new int[bmp.getHeight() * bmp.getWidth()];
	  bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

	  for (int i = 0; i < pixels.length; i++) {
	  if (pixels[i] > maxLap)
	      maxLap = pixels[i];
	  }
	  
	  if (maxLap < soglia || maxLap == soglia) {
	      Log.i(TAG, "blur image");
	      return false;
	  }
	  return true;
  }
  
  public Bitmap quality(Bitmap image) {
	  Bitmap destImage;
	  Bitmap bmp;
	  int l = CvType.CV_8UC1; //8-bit grey scale image
	  
	  opt.inDither = true;
	  opt.inPreferredConfig = Bitmap.Config.ARGB_8888;

	  Utils.bitmapToMat(image, matImage);
	  Imgproc.cvtColor(matImage, matImageGrey, Imgproc.COLOR_BGR2GRAY);
	  
	  Imgproc.Laplacian(matImageGrey, laplacianImage, CvType.CV_16S, 3, 1, 0, Imgproc.BORDER_DEFAULT);
	  Core.convertScaleAbs(laplacianImage,laplacianImage8bit);

	  bmp = Bitmap.createBitmap(laplacianImage8bit.cols(),laplacianImage8bit.rows(), Bitmap.Config.ARGB_8888);
	  Utils.matToBitmap(laplacianImage8bit, bmp);
	  
	  return bmp;
  }
}


