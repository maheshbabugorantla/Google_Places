package com.example.maheshbabugorantla.googlemapspractice.ZomatoAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;

import android.net.Uri;
import android.os.AsyncTask;

/**
 * Created by Mahesh Babu Gorantla on 2/6/2017.
 *
 * This class is used to handle the Downloading of the Restaurants Data from ZOMATO API.
 */

public class ZomatoHelper extends AsyncTask<String , Void, String> {

    private String getZomatoData() {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        String newData = null;

        final String ZOMATO_BASE_URL = "https://developers.zomato.com/api/v2.1/";
        final String API_KEY = "c8b9ea97d52d908d5ad6f20dd9400127";
        final String ID = "user-key";

        try {
            Uri uriBuilder = Uri.parse(ZOMATO_BASE_URL).buildUpon().appendPath("categories").build();
//            uriBuilder.setPath("/api/v2.1/categories");
            //uriBuilder.setScheme("https").setHost("www.developers.zomato.com").setPath("/api/v2.1/categories");

            String finalURI = uriBuilder.toString();

            System.out.println("Build URL is " + finalURI);

            URL builtURL = new URL(finalURI);

            httpURLConnection = (HttpURLConnection) builtURL.openConnection();
            httpURLConnection.setRequestMethod("GET"); // Letting the Connection know that the data will be fetched
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // Setting the headers
            httpURLConnection.setRequestProperty(ID,API_KEY); // Setting the API Key as a header parameter

            httpURLConnection.connect(); // Connecting to the Web Link;

            // Reading the InputStream into a String
            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();

            // This happens when there is not data to be read from the Web URL Connection
            if(inputStream == null) {
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            // Reading the raw JSON Data into the Buffer Reader.
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // This might happened when read operation from URL is UnSuccessful or May be the connection might drop in between
            if(stringBuilder.length() == 0) {
                return null;
            }

            newData = stringBuilder.toString();
        } catch (IOException e) {
            System.out.println("IO Exception Occurred");
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        finally {

            // Checking to see if the URL Connection is still Open. If it is then close it
            if(httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            if(bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error occurred while closing the buffered reader");
                }
            }
        }

        System.out.println(newData);
        return newData;
    }

    @Override
    protected String doInBackground(String... params) {
        return getZomatoData();
    }
}
