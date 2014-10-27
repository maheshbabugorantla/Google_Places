package edu.purdue.tada;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class ReviewActivity extends BaseActivity{
	private TableLayout tl;
	
	private float scale;
	private int rowHeight;
	private int imageWidth;
	private int idCount;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.review_layout);
		
		tl = (TableLayout)findViewById(R.id.reviewTable);
		
		// Get the screen's density scale
		scale = getResources().getDisplayMetrics().density;
		rowHeight = (int) (50 * scale + 0.5f); // 50dp to pixels
		imageWidth = (int) (70 * scale + 0.5f); // 70dp to pixels
		idCount = 100;
		
		generateReviewTable();
	}
	
	/*
	 * Ben Klutzke 10/27/14
	 * Generates the table
	 */	
	private void generateReviewTable(){
		// Refresh button may call this but will need to clear the table first
		InputStream in = null;
		
		try{
			String rec_sent = "rec_sent.txt"; // Going to used rec_sent.txt because it has test data that can easily be added to
	        File sentRec = new File(recSaved,rec_sent);
	        if (!sentRec.exists()) {
				System.out.println("No Rec File to Test Stuff With");
				return;
			}
	        
	        in = new BufferedInputStream(new FileInputStream(sentRec));
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	        String line;
	        while((line = reader.readLine()) != null) {
	        	System.out.println(line);
	        	generateTableRow(line);
	        }
	        
	        in.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}
	}

	/*
	 * Ben Klutzke 10/27/14
	 * Generates the row
	 */
	private void generateTableRow(String s){
		TableRow tr = new TableRow(this);
//		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, rowHeight));
		LinearLayout outerLayout = new LinearLayout(this);
//		outerLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		
		InputStream in = null;
		String hash;
		String date;
		String imageCnt;
		String image1;
		String image2;
		
		// Read s (.rec file) to get time and image names 
		try{
	        File rec = new File(s);
	        if (!rec.exists()) {
				System.out.println("File "+ s + "Doesn't exist???");
				return;
			}
	        
	        in = new BufferedInputStream(new FileInputStream(rec));
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	        
	        // Big assumptions made here about the contents of .rec files
	        hash = reader.readLine();
	        date = reader.readLine();
	        imageCnt = reader.readLine();
	        image1 = reader.readLine();
	        image2 = reader.readLine();	        
	        
	        in.close();	      
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}
		
		System.out.println(date);
		
		// Make an imageView and put image1 in it
		ImageView iv = new ImageView(this);
		iv.setLayoutParams(new LayoutParams(imageWidth, rowHeight));
		Bitmap bm = BitmapFactory.decodeFile(recSaved + "/" + image1);
		iv.setImageBitmap(bm);
		outerLayout.addView(iv);
		
		// Make a textView and put date in it, Note that the date needs to be formatted
		TextView tv = new TextView(this);
		tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setText(date);
		outerLayout.addView(tv);
		
		// Make another textView and put B(reakfast), L(unch), or D(innner)
		TextView tv2 = new TextView(this);
		tv2.setLayoutParams(new LayoutParams(imageWidth, LayoutParams.WRAP_CONTENT));
		tv2.setGravity(Gravity.CENTER);
		tv2.setText("L");
		outerLayout.addView(tv2);
		
		tr.addView(outerLayout);
		
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, rowHeight));
		
		// Row Divider
		View v = new View(this);
		v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
		v.setBackgroundColor(Color.rgb(51, 51, 51));
		
		tl.addView(v);
	}
}