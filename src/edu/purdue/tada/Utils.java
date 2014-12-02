package edu.purdue.tada;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.app.Activity;
import android.util.Log;

/* 
 * methods are going to be final protected. final: other subclasses cannot modify 
 * the method. protected: method can only be used by classes of the same type
 */

public class Utils extends Activity {

	/* HTTPS */
	public final static String PHP_USER_ID = "PHP_USER_ID";
	public final static String PHP_PWD = "PHP_PWD";

	public final static String LINE_END = "\n"; /*
												 * NOTE: USED TO BE \r\n -
												 * Windows Line
												 */
	public final static String TWO_HYPHENS = "--";
	public final static String BOUNDARY = "*****";
	public final static String PATH = "/~tadaepics/";

	private static final String TAG = "ALBERT-UTILS";

	//////////////////////// HTTPS ////////////////////////

	/* Always verify the host - Do not check for certificate */
	public final static HostnameVerifier hostVerify() {
		return new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
	}

	/**
	 * Trust every server - do not check for any certificate
	 */
	final protected static void trustAllHosts() {
		/* Create a trust manager that does not validate certificate chains */
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

		} };

		/* Install the all-trusting trust manager */
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final static HttpsURLConnection getHttpsConnection(String filename,
			HostnameVerifier v, String server, String contentType)
			throws IOException {

		HttpsURLConnection httpsConnection = null;

		trustAllHosts();

		Log.d(TAG, "Opening HTTPS Connection");

		URL url = new URL("https", server, filename);

		httpsConnection = (HttpsURLConnection) url.openConnection();

		/* Use a POST method */
		httpsConnection.setRequestMethod("POST");

		httpsConnection.setHostnameVerifier(v);

		/* Allow inputs */
		httpsConnection.setDoInput(true);

		/* Allow outputs */
		httpsConnection.setDoOutput(true);

		/* Don't use a cached copy */
		httpsConnection.setUseCaches(false);

		httpsConnection.setRequestProperty("Connection", "Keep-Alive");

		/* Set content type */
		httpsConnection.setRequestProperty("Content-Type", contentType);

		Log.d(TAG, "Initialized HTTPS Connection");

		Log.d(TAG, "httpsConnection: " + httpsConnection);

		return httpsConnection;
	}

	public final static String convertStreamToString(InputStream inputStream)
			throws IOException {
		/* Method updated 12/01/14 by Ben Klutzke
		 * Tag files are parsed by tabs and so each line must be trimmed to remove
		 * extra tabs.
		 */
		if (inputStream != null) {
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder sb = new StringBuilder();
			while((line = reader.readLine()) != null){
				sb.append(line.trim() + '\n');
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	public final static void closeStreams(InputStream httpsInputStream,
			DataOutputStream outputStream) {
		Log.d(TAG, "Closing Streams...");
		try {
			httpsInputStream.close();
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final static DataOutputStream outputStreamHeaderMultiPartForm(
			DataOutputStream outputStream) throws IOException {

		Log.d(TAG, "Starting headers for multi-part form");

		/* Send data/headers */
		outputStream.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);

		Log.d(TAG, "Headers for multi-part form are written");

		return outputStream;
	}

	public final static DataOutputStream outputStreamSetUpText(
			DataOutputStream outputStream) throws IOException {

		Log.d(TAG, "Starting headers for plain text");

		/* Send data/headers */
		outputStream
				.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END);

		outputStream.writeBytes(LINE_END);

		Log.d(TAG, "Headers for plain text are written");

		return outputStream;
	}

	public final static DataOutputStream outputStreamSetUpForm(
			DataOutputStream outputStream, String key, String value)
			throws IOException {

		Log.d(TAG, "Starting headers for form");

		outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key
				+ "\"" + LINE_END + LINE_END + value + LINE_END + TWO_HYPHENS
				+ BOUNDARY + LINE_END);

		Log.d(TAG, "Headers for form are written");

		return outputStream;
	}

	public final static DataOutputStream outputStreamSetUpFile(String filepath,
			DataOutputStream outputStream, String tag) throws IOException {

		Log.d(TAG, "Starting headers for file upload");

		outputStream
				.writeBytes("Content-Disposition: form-data; name=\"" + tag + "\";filename=\""
						+ filepath + "\"" + LINE_END);

		outputStream.writeBytes(LINE_END);

		Log.d(TAG, "Headers for file upload are written");

		return outputStream;
	}
	
	public final static DataOutputStream outputStreamSetUpFile2(String filepath,String filepath2,
			DataOutputStream outputStream, String tag, String tag2) throws IOException {

		Log.d(TAG, "Starting headers for file upload");

		outputStream
				.writeBytes("Content-Disposition: multipart/form-data; name=\"" + tag + "\";filename=\""
						+ filepath + "\"" + LINE_END + "multipart/form-data; name=\"" + tag2 + "\";filename=\""
								+ filepath2 + "\"" + LINE_END);

		outputStream.writeBytes(LINE_END);

		Log.d(TAG, "Headers for file upload are written");

		return outputStream;
	}

	public final static DataOutputStream retrieveFileBytes(String filepath,
			DataOutputStream outputStream, FileInputStream fileInputStream)
			throws IOException {

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		bytesAvailable = fileInputStream.available();

		bufferSize = Math.min(bytesAvailable, maxBufferSize);

		buffer = new byte[bufferSize];

		/* Read file (image) */
		bytesRead = fileInputStream.read(buffer, 0, bufferSize);

		/* Send image */
		while (bytesRead > 0) {
			outputStream.write(buffer, 0, bufferSize);
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		}

		return outputStream;
	}

	public final static void outputStreamComplete(DataOutputStream outputStream) {
		try {
			outputStream.writeBytes(LINE_END);

			outputStream.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS
					+ LINE_END);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final static void getServerResponse(
			HttpsURLConnection httpsConnection) {
		try {
			/* Responses from the server (code and message) */
			int serverResponseCode = httpsConnection.getResponseCode();

			String serverResponseMessage = httpsConnection.getResponseMessage();

			Log.d(TAG, "Got Response: " + serverResponseCode);

			Log.d(TAG, "Got Response: " + serverResponseMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DataOutputStream createOutputStream(
			HttpsURLConnection httpsConnection) {

		DataOutputStream outputStream = null;

		try {
			outputStream = new DataOutputStream(
					httpsConnection.getOutputStream());

		} catch (IOException e) {
			/*
			 * If tehre's something wrong with the server, catching the
			 * exception will set the output stream to null
			 */
			Log.d(TAG, "IOException: " + e.toString());
		}

		return outputStream;
	}
}
