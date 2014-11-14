package edu.purdue.tada;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ReviewActivity extends BaseActivity{
	
	// Note that the refresh button should do adapter.notifyDataSetChanged();
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.review_layout);
		
		ListView lv = (ListView) findViewById(R.id.reviewList);
		
		ReviewAdapter adapter = generateReviewAdapter(); 
		
		if(adapter != null)
			lv.setAdapter(adapter);
	}
	
	private ReviewAdapter generateReviewAdapter(){
		ReviewAdapter adapter = new ReviewAdapter(this);
		InputStream in = null;
		ArrayList<String> lines = new ArrayList<String>();
		
		try{
			String rec_sent = "rec_sent.txt"; // Going to use rec_sent.txt because it has test data that can easily be added to
	        File sentRec = new File(recSaved,rec_sent);
	        if (!sentRec.exists()) {
				System.out.println("No Rec File to Test Stuff With");
				return null;
			}
	        
	        in = new BufferedInputStream(new FileInputStream(sentRec));
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	        String line;
	        // Each line will be a .rec file
	        while((line = reader.readLine()) != null) {
	        	lines.add(line);
	        }
	        
	        in.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
		// Parses .rec files into meaningful data
		ArrayList<ReviewContainer> list = generateList(lines);
		
		// Sets up list adapter
		for(ReviewContainer rc : list){
			adapter.addSection(rc);
		}
		
		return adapter;
	}
	
	// Will need heavy modifications for sorting the dates by today, yesterday, this week, last week, etc
	private ArrayList<ReviewContainer> generateList(ArrayList<String> lines){
		Map<String, ReviewContainer> map = new HashMap<String, ReviewContainer>();
		ArrayList<ReviewContainer> list = new ArrayList<ReviewContainer>();
		
		for(String line : lines){
			// Parses .rec file
			ReviewItem ri = generateReviewItem(line);
			ri.setThumbnail(recSaved);
			
			// Generates a string which is the month and year the .rec was made
			String month = new SimpleDateFormat("MMMM").format(ri.getDate()).toString();
			String year = new SimpleDateFormat("yyyy").format(ri.getDate()).toString();		
			String key = month + ", " + year;
			
			ri.setSubDate(new SimpleDateFormat("E, MMMM d h:mm a").format(ri.getDate()).toString());
			
			// Basically sorts the .recs by their month and year. This will change when there are more .recs to mess with
			if(map.containsKey(key)){
				map.get(key).addItem(ri);
			}else{
				ReviewContainer rc = new ReviewContainer(key);
				rc.addItem(ri);
				list.add(rc);
				map.put(key, rc);
			}
		}
		
		return list;
	}
	
	private ReviewItem generateReviewItem(String fileName){
		InputStream in = null;
		String hash;
		String date;
		String imageCnt;
		String image1;
		String image2;
		
		// Reads .rec file to get time and image names 
		try{
	        File rec = new File(fileName);
	        if (!rec.exists()) {
				System.out.println("File "+ fileName + "Doesn't exist???");
				return null;
			}
	        
	        in = new BufferedInputStream(new FileInputStream(rec));
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	        
	        // Big assumptions made here about the contents of .rec files
	        hash = reader.readLine();
	        date = reader.readLine();
	        imageCnt = reader.readLine(); // Not used for anything really
	        image1 = reader.readLine();
	        image2 = reader.readLine();	        
	        
	        in.close();	      
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
		return new ReviewItem(hash, date, image1, image2);
	}
	
	private class ReviewAdapter extends BaseAdapter implements PinnedSectionListAdapter {		
		private ArrayList<ReviewContainer> list = new ArrayList<ReviewContainer>();
		LayoutInflater inflater = null;
		
		private final int HEADER_TYPE = 0;
		private final int ITEM_TYPE = 1;

		public ReviewAdapter(Context context){
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		public void addSection(ReviewContainer rc){
			list.add(rc);
		}
		
		// convertView is a previously used view of the same view type supplied by ListView to improve performance
		public View getView(int position, View convertView, ViewGroup parent){
			int viewType = getItemViewType(position);
			if(convertView == null){
				if(viewType == HEADER_TYPE){
					convertView = inflater.inflate(R.layout.review_list_container, parent, false);
				}else{
					convertView = inflater.inflate(R.layout.review_list_item, parent, false);
				}
			}
			
			if(viewType == HEADER_TYPE){
				ReviewContainer rc = (ReviewContainer) getItem(position);
				TextView tv = (TextView) convertView.findViewById(R.id.list_header_title);
				tv.setText(rc.getDate());
			}else{
				ReviewItem ri = (ReviewItem) getItem(position);
				ImageView picture = (ImageView) convertView.findViewById(R.id.reviewPicture);
			    TextView time = (TextView) convertView.findViewById(R.id.reviewTime);
			    TextView bld = (TextView) convertView.findViewById(R.id.reviewBLD);
			    TextView status = (TextView) convertView.findViewById(R.id.status);
			    
			    // Sets the thumbnail image
				picture.setImageBitmap(ri.getThumbnail());
				
				// Sets time subtext
				time.setText(ri.getSubDate());
				
				// Sets status text (food or not yet reviewed)
				status.setText("Not yet reviewed");
				
				// Sets BLD text (not yet implemented)
				bld.setText("B|L|D");
			}
			return convertView;
		}
		
		@Override
		public int getCount(){
			int total = 0;
			
			for(ReviewContainer rc : list){
				total++; // One for the header
				total += rc.getItemCount(); // Adds one for each review item
			}
			
			return total;
			
		}

		@Override
		public Object getItem(int position) {
			for(ReviewContainer rc : list){
				 int size = rc.getItemCount() + 1;
				 
				 // Is position inside this section
				 if(position == 0)
					 return rc;
				 else if(position < size)
					 return rc.getItems().get(position - 1);
				 
				 // Jump to next section
				 position -= size;				 
			}
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		
		public int getItemViewType(int position){
			for(ReviewContainer rc : list){
				 int size = rc.getItemCount() + 1;
				 
				 // Is position inside this section
				 if(position == 0)
					 return HEADER_TYPE;
				 else if(position < size)
					 return ITEM_TYPE;
				 
				 // Jump to next section
				 position -= size;				 
			}
			return -1;
		}
		
		public int getViewTypeCount(){
			return 2;
		}
		
		// We implement this method to return 'true' for all view types we want to pin
	      @Override
	      public boolean isItemViewTypePinned(int viewType) {
	          return viewType == HEADER_TYPE;
	      }
	}
}