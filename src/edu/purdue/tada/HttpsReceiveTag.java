package edu.purdue.tada;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
//import edu.purdue.tada.HttpsSendImage.DownloadDataTask;

public class HttpsReceiveTag extends Utils {
//	private static final int REQUEST_BAR_CODE = 1234;
//	private static final int UPLOAD_IMAGES = 53;
//	private static final int UPLOAD_UNSENT = 59;
	

	/* Specify server-side filename */
	private String PHP_FILENAME = PATH + "epics_get_results.php";

	/* Create tag for logcat */
	private static final String TAG = "YU-HTTPSENDIMAGE";

	/* Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		Intent intent = getIntent();
//		int requestCode = intent.getIntExtra("requestCode",0);
		
		/* Insert code to setup activity layout ... */

		/*
		 * Create async task to upload an image to the server while showing a
		 * progress dialog
		 */
		
		//sees from which activity this activity was called from
		//and creates a ddt with a code that specifies the desired action
		
		DownloadTagTask ddt = new DownloadTagTask();
		
		ddt.execute();
	}

	/**
	 * Extends {@link AsyncTask}. Shows progress bar while uploading image to
	 * the server. Based on Yu Wang's DownloadDataTask
	 */
	private class DownloadTagTask extends AsyncTask<Void, Integer, Void> {
		
		private final ProgressDialog dialog = new ProgressDialog(HttpsReceiveTag.this);

		// can use UI thread here
		protected void onPreExecute() {
			dialog.setMessage("Requesting Tag File...");
			dialog.setCancelable(false);
			dialog.show();
		}

		// automatically done on worker thread (separate from UI thread)
		protected Void doInBackground(final Void... args) {

			/*
			 * Get image filepath - I use a singleton (ActivityBridge) to set
			 * and get parameters - You have to decide how you are going to
			 * access your variables given your specific needs
			 */
			

			try {
				Log.d(TAG, "Receiving tag from server...");

				/* Get user ID and password (MD5) */
				String userIdLogin = "IdNotImportant";
				String pwdMd5Login = "epics975";

				/*
				 * Call method to create HTTPS connection and communicate with
				 * server
				 */
				String response = receiveTag(PHP_FILENAME, userIdLogin, pwdMd5Login);

				/* Set response to singleton to analyze it on onPostExecute */
				ActivityBridge.getInstance().setHttpsresponse(response);

			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		// can use UI thread here
		protected void onPostExecute(Void unused) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			/* Get bitmap of current image (uploaded image) from singleton */
			//Bitmap rgbBitmap = ActivityBridge.getInstance().getRgbBitmap();

			/* Set bitmap of current image (uploaded image) to singleton */
			//ActivityBridge.getInstance().setScreenSizeBitmap(rgbBitmap);

			/* Get HTTPS response from singleton */
			String response = ActivityBridge.getInstance().getHttpsresponse();

			/* Check server response */
			if (response == null) {

				/* Show dialog to tell user something went wrong */
				System.out.println("something wrong!!");
			} else {
				
				/* Set image and text to layout */
				System.out.println("response::"+ response);
			}
			HttpsReceiveTag.this.setResult(RESULT_OK);
			finish();
		}
	}
	
			
	
	private String receiveTag(String serverFilename, String userIdLogin, String pwdMd5Login) throws IOException {

		/* Always verify the host - Do not check for certificate */
		HostnameVerifier v = Utils.hostVerify();

		/* Get server URL from singleton */
		//String server = ActivityBridge.getInstance().getServerDomainName();
		String server = "epicsapps.ecn.purdue.edu";
		
		/* Open HTPPS connection to the server */
		HttpsURLConnection httpsConnection = getHttpsConnection(serverFilename, v, server, "multipart/form-data;boundary=" + BOUNDARY);

		/* Create output stream to write bytes (headers/data/image) */
		DataOutputStream outputStream = createOutputStream(httpsConnection);

		String httpsResponseBody;

		/* Wrong server request */
		if (outputStream == null) {
			/* We need to check this on the returning method */
			httpsResponseBody = null;

		} else {

			/* Send data/headers */
			outputStream = outputStreamHeaderMultiPartForm(outputStream);

			// --------------- LOGIN ---------------

			/* Set up output stream for user ID form */
			outputStream = outputStreamSetUpForm(outputStream, PHP_USER_ID,
					userIdLogin);

			/* Set up output stream for MD5 password form */
			outputStream = outputStreamSetUpForm(outputStream, PHP_PWD,
					pwdMd5Login);
			
			/*Set up deviceID */
			outputStream = outputStreamSetUpForm(outputStream, "deviceID",
					Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));	
			
			
			/* Send multipart form data necessary after file data */
			outputStreamComplete(outputStream);

			/* Print code and message to the log */
			getServerResponse(httpsConnection);

			/* Get data from server and convert it to string */
			InputStream httpsInputStream = httpsConnection.getInputStream();
			httpsResponseBody = convertStreamToString(httpsInputStream);

			Log.d(TAG, "Response Body: " + httpsResponseBody);

			/* Close streams */
			Utils.closeStreams(httpsInputStream, outputStream);

//			/* Parse received data */
//			String faketagString = "FFFFFFFF6638EC19F5BD42C8AC0EF9E86EA0831C\n3\n" +
//					"0\n2576\t791\n6310100\tapple\n63101000\tapple\n63107010\tbanana\n" +
//					"14010100\tcheese\n63101000\tapple\n0\n1455\t1269\n21500100\tground beef\n" +
//					"21500100\tground beef\n41201010\tbeans\n63107010\tbanana\n63101000\tapple\n" +
//					"0\n144\t1497\n11112110\tmilk\n11112110\tmilk\n91501010\tjello\n41201010\tbeans\n" +
//					"63107010\tbanana\n";
//			
//			String[] tokens = faketagString.split("\n|\t"); 			// tokenize the input stream by splitting \t and \n
//			int size = Integer.parseInt(tokens[1]);						// size is how many food pins tag files provide
//			String [] pinCoord = new String[size];						// a string to catch the pins' coordinates
//			ArrayList<String> foodNames = new ArrayList<String>();		// an array list to store food names on each pin
//			ArrayList<ArrayList<String>> parts = new ArrayList<ArrayList<String>>();	// creating sub list for foodNames
//			
//			// iterating through the pin coordinates, saves into the array of string for later use
//			for(int i = 0; i < size; i++) {
//				pinCoord[i] = tokens[i*13+3] + "," + tokens[i*13+4];
//			}
//			// adding all food names into a big array
//			for(int i = 0; i < size; i++) {
//				foodNames.add(tokens[i*13+6]);
//				foodNames.add(tokens[i*13+8]);
//				foodNames.add(tokens[i*13+10]);
//				foodNames.add(tokens[i*13+12]);
//				foodNames.add(tokens[i*13+14]);
//			}
//			// splitting the foodNames into small sub array to store into the mapping
//			for(int i = 0; i < foodNames.size(); i+=5) {
//				parts.add(new ArrayList<String>(foodNames.subList(i,i+5)));
//			}
//			// mapping the coordinates to the array of food names
//			for(int i = 0; i < size; i++) {
//				ActivityBridge.getInstance().setfoodPins(pinCoord[i],parts.get(i));
//			}
//			// above code is from tag file parsing
		}

		return httpsResponseBody;
	}
}