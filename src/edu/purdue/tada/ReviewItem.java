package edu.purdue.tada;

public class ReviewItem {
	private String image;
	private String time;
	private String bld;
	
	public ReviewItem(String image, String time, String bld){
		this.image = image;
		this.time = time;
		this.bld = bld;
	}
	
	public String getTime(){
		return time;
	}
}
