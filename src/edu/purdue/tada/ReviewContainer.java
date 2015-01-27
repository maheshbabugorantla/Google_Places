package edu.purdue.tada;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Review activity list is split into sections. This class details the contents
 * of each section
 * 
 * @author Ben Klutzke
 * 
 */

public class ReviewContainer implements Comparable<ReviewContainer>{
	private Date date;
	private ArrayList<ReviewItem> items;
	
	public ReviewContainer(String d){
		try {
			this.date = new SimpleDateFormat("MMMM, yyyy", Locale.ENGLISH).parse(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.date = null;
		}
		items = new ArrayList<ReviewItem>();
	}
	
	public void addItem(ReviewItem ri){
		items.add(ri);
	}
	public Date getDate(){
		return date;
	}	
	public String getDateString(){
		return new SimpleDateFormat("MMMM, yyyy").format(date).toString();
	}
	
	public ArrayList<ReviewItem> getItems(){
		return items;
	}
	
	public int getItemCount(){
		return items.size();
	}

	@Override
	public int compareTo(ReviewContainer rc) {
		return date.compareTo(rc.getDate());
	}
}
