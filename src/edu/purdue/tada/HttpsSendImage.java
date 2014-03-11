package edu.purdue.tada;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import edu.purdue.tada.ActivityBridge;
import edu.purdue.tada.Utils;

public class HttpsSendImage extends Utils {
	/*
	 * HttpSendImage is an ACTIVITY, as it is part of the User Interface (UI) -
	 * it opens a new layout view and shows the uploaded picture and relevant
	 * data
	 */

	/* Specify server-side filename */
	private String PHP_FILENAME = PATH + "epics_receive_image.php";

	/* Create tag for logcat */
	private static final String TAG = "YU-HTTPSENDIMAGE";
	private String filepath2;

	/* Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Insert code to setup activity layout ... */

		/*
		 * Create async task to upload an image to the server while showing a
		 * progress dialog
		 */
		DownloadDataTask ddt = new DownloadDataTask();

		ddt.execute();
	}

	/**
	 * Extends {@link AsyncTask}. Shows progress bar while uploading image to
	 * the server
	 * 
	 * @author Yu Wang
	 * 
	 */
	private class DownloadDataTask extends AsyncTask<Void, Integer, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				HttpsSendImage.this);

		// can use UI thread here
		protected void onPreExecute() {
			dialog.setMessage("Uploading image...");
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
			
			
			String filepath = ActivityBridge.getInstance().getFilepath();
			filepath2 = ActivityBridge.getInstance().getFilepath2();
			System.out.println("filepath:"+ filepath);
			System.out.println("filepath2:"+filepath2);

			try {
				/* Send image to server */
				Log.d(TAG, "Sendin image to server...");

				/*
				 * Send image and save response in a string to put in a TextView
				 * (GUI)
				 */

				/* Get user ID and password (MD5) */
				String userIdLogin = "5001";
				String pwdMd5Login = "epics975";

				/*
				 * Call method to create HTTPS connection and communicate with
				 * server
				 */
				String response = sendImageAndLogin(filepath, PHP_FILENAME,
						userIdLogin, pwdMd5Login);

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
			HttpsSendImage.this.setResult(RESULT_OK);
			finish();
		}
	}

	private String sendImageAndLogin(String imagePath, String serverFilename,
			String userIdLogin, String pwdMd5Login) throws IOException {

		/* Always verify the host - Do not check for certificate */
		HostnameVerifier v = Utils.hostVerify();

		/* Get server URL from singleton */
		//String server = ActivityBridge.getInstance().getServerDomainName();
		String server = "epicsapps.ecn.purdue.edu";
		
		/* Open HTPPS connection to the server */
		HttpsURLConnection httpsConnection = getHttpsConnection(serverFilename,
				v, server, "multipart/form-data;boundary=" + BOUNDARY);

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
			
			/*Information of the first image */
			outputStream = outputStreamSetUpForm(outputStream, "deviceID",
					Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));	
			outputStream = outputStreamSetUpForm(outputStream, "imageDate",
					filepath2.substring(filepath2.lastIndexOf("/")+1).substring(0,10));
			outputStream = outputStreamSetUpForm(outputStream, "imageTime",
					filepath2.substring(filepath2.lastIndexOf("/")+1).substring(11,19));
			outputStream = outputStreamSetUpForm(outputStream, "latitude", ActivityBridge.getInstance().getLatitude2());
			outputStream = outputStreamSetUpForm(outputStream, "longitude", ActivityBridge.getInstance().getLongitude2());
			outputStream = outputStreamSetUpForm(outputStream, "angle", ActivityBridge.getInstance().getAngle2());
			//----------------first image ------------
			/* Set up output stream for file upload */
			outputStream = outputStreamSetUpFile(filepath2, outputStream, "file");
			//outputStream = outputStreamSetUpFile2(filepath2,imagePath, outputStream, "file","file2");
			
			/* Create input stream to put the image */
			FileInputStream fileInputStream2 = new FileInputStream(new File(
					filepath2));

			/* Retrieve file and write it to output stream to be sent */ 
			retrieveFileBytes(filepath2, outputStream, fileInputStream2);
			//End of the first image
			
			//Information of the second image
			outputStream = outputStreamSetUpForm(outputStream, "imageDate2",
					imagePath.substring(imagePath.lastIndexOf("/")+1).substring(0,10));
			outputStream = outputStreamSetUpForm(outputStream, "imageTime2",
					imagePath.substring(imagePath.lastIndexOf("/")+1).substring(11,19));
			outputStream = outputStreamSetUpForm(outputStream, "numOfImages","2");
			outputStream = outputStreamSetUpForm(outputStream, "latitude2", ActivityBridge.getInstance().getLatitude1());
			outputStream = outputStreamSetUpForm(outputStream, "longitude2", ActivityBridge.getInstance().getLongitude1());
			outputStream = outputStreamSetUpForm(outputStream, "angle2", ActivityBridge.getInstance().getAngle1());
			//------------second image -------------
			/* Set up output stream for file upload */
			outputStream = outputStreamSetUpFile(imagePath, outputStream, "file2");
			
			/* Create input stream to put the image */
			FileInputStream fileInputStream = new FileInputStream(new File(
					imagePath));

			/* Retrieve file and write it to output stream to be sent */
			retrieveFileBytes(imagePath, outputStream, fileInputStream);
			//End of the second image
			
			// -------------------------------------

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
			fileInputStream.close();
			fileInputStream2.close();

			/* Parse received data */
		}

		return httpsResponseBody;
	}
}