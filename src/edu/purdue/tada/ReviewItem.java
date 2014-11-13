package edu.purdue.tada;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReviewItem {
	private String hash;
	private Date date;
	private String image1;
	private String image2;
	private String subDate;
	
	public ReviewItem(String hash, String date, String image1, String image2){
		this.hash = hash;
		this.image1 = image1;
		this.image2 = image2;
		this.subDate = null;
		
		try {
			this.date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.ENGLISH).parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.date = null;
		}
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
}
