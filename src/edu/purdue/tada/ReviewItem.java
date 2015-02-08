package edu.purdue.tada;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Class detailing the contents of each row for the review activity
 * 
 * @author Ben Klutzke
 * 
 */

public class ReviewItem implements Comparable<ReviewItem> {
	private String hash;
	private Date date;
	private String image1;
	private String image2;
	private String subDate;
	private String bld;
	private Bitmap bm;
	
	public ReviewItem(String hash, String date, String image1, String image2){
		this.hash = hash;
		this.image1 = image1;
		this.image2 = image2;
		this.subDate = null;
		this.bm = null;
		
		try {
			this.date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.ENGLISH).parse(date);
			int hr = this.date.getHours();
			if(hr >= 0 && hr < 10)
				this.bld = "B";
			else if(hr >= 10 && hr< 16)
				this.bld = "L";
			else
				this.bld = "D";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.date = null;
			this.bld = "E";			
		}
	}
	public Bitmap getThumbnail(){
		return this.bm;
	}
	public Bitmap setThumbnail(String s){
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inSampleSize = 20;
		this.bm = BitmapFactory.decodeFile(s + "/" + image1, o); // scaled to 1/20th image size
		return this.bm;
	}
	public void setSubDate(String s){
		this.subDate = s;
	}
	public String getSubDate(){
		return this.subDate;
	}

	public String getHash(){
		return this.hash;
	}
	public Date getDate(){
		return this.date;
	}
	public String getImage1(){
		return this.image1;
	}
	public String getImage2(){
		return this.image2;
	}
	public String getBLD(){
		return bld;
	}
	@Override
	public int compareTo(ReviewItem ri) {
		return date.compareTo(ri.getDate());
	}
}
