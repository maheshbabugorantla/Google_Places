package edu.purdue.tada;

import java.util.ArrayList;

public class ReviewContainer {
	private String date;
	private ArrayList<ReviewItem> items;
	
	public ReviewContainer(String d){
		this.date = d;
		items = new ArrayList<ReviewItem>();
	}
	
//	public void addItem(String image, String time){
//		ReviewItem ri = new ReviewItem(image, time, "B");
//		items.add(ri);
//	}
	
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
