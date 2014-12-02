package edu.purdue.tada;

import java.util.ArrayList;

/**
 * Review activity list is split into sections. This class details the contents
 * of each section
 * 
 * @author Ben Klutzke
 * 
 */

public class ReviewContainer {
	private String date;
	private ArrayList<ReviewItem> items;
	
	public ReviewContainer(String d){
		this.date = d;
		items = new ArrayList<ReviewItem>();
	}
	
	public void addItem(ReviewItem ri){
		items.add(ri);
	}
	
	public String getDate(){
		return date;
	}
	
	public ArrayList<ReviewItem> getItems(){
		return items;
	}
	
	public int getItemCount(){
		return items.size();
	}
}
