package com.dm.zbar.android.scanner;

//import android.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

public class ZBarScannerActivity extends Activity implements Camera.PreviewCallback, ZBarConstants {

    private static final String TAG = "ZBarScannerActivity";
    private CameraPreview mPreview;
    private Camera mCamera;
    private ImageScanner mScanner;
    private Handler mAutoFocusHandler;
    private boolean mPreviewing = true;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isCameraAvailable()) {
            // Cancel request if there is no rear-facing camera.
            cancelRequest();
            return;
        }

        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAutoFocusHandler = new Handler();

        // Create and configure the ImageScanner;
        setupScanner();

        // Create a RelativeLayout container that will hold a SurfaceView,
        // and set it as the content of our activity.
        mPreview = new CameraPreview(this, this, autoFocusCB);
        setContentView(mPreview);
    }
    

    public void setupScanner() {
        mScanner = new ImageScanner();
        mScanner.setConfig(0, Config.X_DENSITY, 3);
        mScanner.setConfig(0, Config.Y_DENSITY, 3);

        int[] symbols = getIntent().getIntArrayExtra(SCAN_MODES);
        if (symbols != null) {
            mScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
            for (int symbol : symbols) {
                mScanner.setConfig(symbol, Config.ENABLE, 1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Open the default i.e. the first rear facing camera.
        mCamera = Camera.open();
        if(mCamera == null) {
            // Cancel request if mCamera is null.
            cancelRequest();
            return;
        }

        mPreview.setCamera(mCamera);
        mPreview.showSurfaceView();

        mPreviewing = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null) {
            mPreview.setCamera(null);
            mCamera.cancelAutoFocus();
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();

            // According to Jason Kuang on http://stackoverflow.com/questions/6519120/how-to-recover-camera-preview-from-sleep,
            // there might be surface recreation problems when the device goes to sleep. So lets just hide it and
            // recreate on resume
            mPreview.hideSurfaceView();

            mPreviewing = false;
            mCamera = null;
        }
    }

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public void cancelRequest() {
        Intent dataIntent = new Intent();
        dataIntent.putExtra(ERROR_INFO, "Camera unavailable");
        setResult(Activity.RESULT_CANCELED, dataIntent);
        finish();
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPreviewSize();

        Image barcode = new Image(size.width, size.height, "Y800");
        barcode.setData(data);

        int result = mScanner.scanImage(barcode);

        if (result != 0) {
            SymbolSet syms = mScanner.getResults();
            for (Symbol sym : syms) {
                String symData = sym.getData();
                if (!TextUtils.isEmpty(symData)) {
                	/*
                	 * Ben Klutzke 10/17/14
                	 * Verifies the check digit of the barcode results before progressing.
                	 */
                	if(verifyBarcode(symData, sym.getType())){

                        mCamera.cancelAutoFocus();
                        mCamera.setPreviewCallback(null);
                        mCamera.stopPreview();
                        mPreviewing = false;
                        
                        Intent dataIntent = new Intent();
                        dataIntent.putExtra(SCAN_RESULT, symData);
                        dataIntent.putExtra(SCAN_RESULT_TYPE, sym.getType());
                        setResult(Activity.RESULT_OK, dataIntent);
                        finish();
                        break;                		
                	}
                }
            }
        }
    }
    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if(mCamera != null && mPreviewing) {
                mCamera.autoFocus(autoFocusCB);
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
    
    /*
     * Ben Klutzke 10/17/14
     * Barcode number verification method. Most barcodes are changed to EAN13 format by zbar.
     * Maybe determine which barcodes are only used with foods.
     */
    private boolean verifyBarcode(String barcode, int type){
    	int digit;
    	char check_digit;
    	int sum1;
    	int sum2;
    	
    	switch(type){
    	case Symbol.UPCE:
    	case Symbol.UPCA:
    	case Symbol.EAN8:
    	case Symbol.I25:
	    	sum1 = 0;
	    	sum2 = 0;
	    	for(int i = 0; i < barcode.length()-1; i++){
	    		if(i%2 == 0)
	    			sum2 += 3*Integer.parseInt("" + barcode.charAt(i));
	    		else
	    			sum1 += Integer.parseInt("" + barcode.charAt(i));
	    	}
	    	digit = 10 - (sum1+sum2) % 10;
	    	if(digit == 10)
	    		check_digit = '0';
	    	else
		    	check_digit = (char)('0' + digit);
	    	
	    	return check_digit == barcode.charAt(barcode.length()-1);
    	
    	case Symbol.ISBN10:
    		int sum = 0;
    		for (int i = 0; i < barcode.length()-1; i++){
    			sum += Integer.parseInt("" + barcode.charAt(i)) * (10-i);
    		}
    		digit = (11 - (sum % 11)) % 11;
    		
    		if(digit == 10)
    			check_digit = 'X';
    		else
    			check_digit = (char)('0' + digit);

    		return check_digit == barcode.charAt(barcode.length()-1);
    	
    	case Symbol.ISBN13:
    	case Symbol.EAN13:
	    	sum1 = 0;
	    	sum2 = 0;
	    	for(int i = 0; i < barcode.length()-1; i++){
	    		if(i%2 == 0)
	    			sum1 += Integer.parseInt("" + barcode.charAt(i));
	    		else
	    			sum2 += 3*Integer.parseInt("" + barcode.charAt(i));
	    	}
	    	digit = 10 - (sum1+sum2) % 10;
	    	if(digit == 10)
	    		check_digit = '0';
	    	else
		    	check_digit = (char)('0' + digit);

	    	return check_digit == barcode.charAt(barcode.length()-1);
    		
    	case Symbol.CODABAR:
	    	sum1 = 0;
	    	sum2 = 0;
	    	int dig;
	    	for(int i = 0; i < barcode.length()-1; i++){
	    		if(i%2 == 0){
	    			dig = 2*Integer.parseInt("" + barcode.charAt(i));
	    			if(dig >= 10)
	    				dig -= 9;
	    			sum2 += dig;
	    		}else
	    			sum1 += Integer.parseInt("" + barcode.charAt(i));
	    	}
	    	digit = 10 - (sum1+sum2) % 10;
	    	if(digit == 10)
	    		check_digit = '0';
	    	else
		    	check_digit = (char)('0' + digit);

	    	return check_digit == barcode.charAt(barcode.length()-1);

    	case Symbol.DATABAR:
	    	sum1 = 0;
	    	sum2 = 0;
	    	for(int i = 2; i < barcode.length()-1; i++){
	    		if(i%2 == 0)
	    			sum2 += 3*Integer.parseInt("" + barcode.charAt(i));
	    		else
	    			sum1 += Integer.parseInt("" + barcode.charAt(i));
	    	}
	    	digit = 10 - (sum1+sum2) % 10;
	    	if(digit == 10)
	    		check_digit = '0';
	    	else
		    	check_digit = (char)('0' + digit);
	    	
	    	return check_digit == barcode.charAt(barcode.length()-1);
    		
    	case Symbol.DATABAR_EXP:
    	case Symbol.CODE39:
    	case Symbol.CODE93:
    	case Symbol.CODE128:
    	case Symbol.QRCODE:
    	case Symbol.PDF417:
    		return true; // future
    	case Symbol.NONE:
    	case Symbol.PARTIAL:
    		return false;
    	}   
    	return true;
    }
}
