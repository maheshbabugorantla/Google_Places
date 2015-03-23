package edu.purdue.tada;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;

/**
 * File based on HttpsSendImage, may combine the two at some point. Requests
 * a tag file from the server and forwards the data as needed.
 * 
 * @author Ben Klutzke
 * 
 */
public class HttpsReceiveTag extends Utils {
	
	/* Specify server-side filename */
	private String PHP_FILENAME = PATH + "tag_test/epics_get_results.php";

	/* Create tag for logcat */
	private static final String TAG = "HttpsReceiveTag";

	/* Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * Create async task to request a tag file from the server while showing a
		 * progress dialog
		 */
		
		//sees from which activity this activity was called from
		//and creates a ddt with a code that specifies the desired action
		
		DownloadTagTask ddt = new DownloadTagTask();
		
		ddt.execute();
	}

	/**
	 * Extends {@link AsyncTask}. Shows progress bar while requesting tag file from
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

			/* Get HTTPS response from singleton */
			String response = ActivityBridge.getInstance().getHttpsresponse();

			/* Check server response */
			if (response == null) {

				/* Show dialog to tell user something went wrong */
				System.out.println("something wrong!!");
			} else {
				
				/* Set image and text to layout */
				System.out.println("response received");
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

//			Log.d(TAG, "Response Body: " + httpsResponseBody);

			/* Close streams */
			Utils.closeStreams(httpsInputStream, outputStream);

			/* Parse received data - Code by Jason Lin*/	
			String tagString = httpsResponseBody.substring(httpsResponseBody.indexOf('\n')+1); // Removes first line of response
			
			String[] tokens = tagString.split("\n|\t"); 			// tokenize the input stream by splitting \t and \n
            String id = tokens[0];
            ActivityBridge.getInstance().setUserID(id);
			int size = Integer.parseInt(tokens[1]);						// size is how many food pins tag files provide
			
			String [] pinCoord = new String[size];						// a string to catch the pins' coordinates
			ArrayList<String> foodNames = new ArrayList<String>();		// an array list to store food names on each pin
			ArrayList<ArrayList<String>> parts = new ArrayList<ArrayList<String>>();	// creating sub list for foodNames
			
			// iterating through the pin coordinates, saves into the array of string for later use
			for(int i = 0; i < size; i++) {
				pinCoord[i] = tokens[i*13+3] + "," + tokens[i*13+4];
			}
			// adding all food names into a big array
			for(int i = 0; i < size; i++) {
				foodNames.add(tokens[i*13+6]);
				foodNames.add(tokens[i*13+8]);
				foodNames.add(tokens[i*13+10]);
				foodNames.add(tokens[i*13+12]);
				foodNames.add(tokens[i*13+14]);
			}
			// splitting the foodNames into small sub array to store into the mapping
			for(int i = 0; i < foodNames.size(); i+=5) {
				parts.add(new ArrayList<String>(foodNames.subList(i,i+5)));
			}
			// mapping the coordinates to the array of food names
			for(int i = 0; i < size; i++) {
				ActivityBridge.getInstance().setfoodPins(pinCoord[i],parts.get(i));
			}
			// above code is from tag file parsing
		}

		return httpsResponseBody;
	}
}