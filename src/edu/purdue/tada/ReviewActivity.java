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
import java.util.LinkedHashMap;
import java.util.Map;

import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class ReviewActivity extends BaseActivity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.review_layout);
		
		ListView lv = (ListView) findViewById(R.id.reviewList);
		
		// Later, caching the information need for the adapter will be optimal
		ReviewAdapter adapter = generateReviewAdapter(); 
		
		if(adapter != null)
			lv.setAdapter(adapter);
	}
	
	private ReviewAdapter generateReviewAdapter(){
		ReviewAdapter adapter = new ReviewAdapter(this);
		// Refresh button may call this but will need to clear the table first
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
		Map<String, ReviewContainer> map = generateMap(lines);
		
		// Sets up list adapter
		ItemAdapter itemAdapter = null;
		for(String key : map.keySet()){
			ReviewContainer rc = map.get(key);			
			itemAdapter = new ItemAdapter(this, R.layout.review_list_item);
			
			itemAdapter.setList(rc.getItems());
			
			adapter.addSection(key, itemAdapter);
		}
		
		return adapter;
	}
	
	private Map<String, ReviewContainer> generateMap(ArrayList<String> lines){
		Map<String, ReviewContainer> map = new HashMap<String, ReviewContainer>();
		
		for(String line : lines){
			// Parses .rec file
			ReviewItem ri = generateReviewItem(line);
			
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
				map.put(key, rc);
			}
		}
		
		return map;
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
	        imageCnt = reader.readLine();
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
	
	private class ItemAdapter extends ArrayAdapter<String> {
		ArrayList<ReviewItem> itemList = new ArrayList<ReviewItem>();
		private Context context;
		private int resource;
		
		public ItemAdapter(Context context, int resource) {
			super(context, resource);
			this.context = context;
			this.resource = resource;
		}
		public View getView(int position, View convertView, ViewGroup parent){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View view = inflater.inflate(resource, parent, false);
		    ImageView picture = (ImageView) view.findViewById(R.id.reviewPicture);
		    TextView time = (TextView) view.findViewById(R.id.reviewTime);
		    TextView bld = (TextView) view.findViewById(R.id.reviewBLD);
		    TextView status = (TextView) view.findViewById(R.id.status);
		    
		    // Probably wrong
		    ReviewItem item = itemList.get(position);
		    
		    // Sets food picture
		    Bitmap bm = BitmapFactory.decodeFile(recSaved + "/" + item.getImage1());
			picture.setImageBitmap(bm);
			
			// Sets time subtext
			time.setText(item.getSubDate());
			
			// Sets status text (food or not yet reviewed)
			status.setText("Not yet reviewed");
			
			// Sets BLD text (not yet implemented)
			bld.setText("B|L|D");		

		    return view;
		}
		
		public void setList(ArrayList<ReviewItem> al){
			itemList = al;
			for(ReviewItem ri : al){
				add(ri.getSubDate());
			}
		}
	}
	
	private class ReviewAdapter extends BaseAdapter implements PinnedSectionListAdapter {
		private ArrayAdapter<String> headers;
		private Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();

		public ReviewAdapter(Context context){
			headers = new ArrayAdapter<String>(context, R.layout.review_list_container, R.id.list_header_title);
		}
		
		public void addSection(String header, Adapter adapter){
			this.headers.add(header);
			this.sections.put(header, adapter);
		}
		
		public View getView(int position, View convertView, ViewGroup parent){			
			int sectionnum = 0;
            for (String section : this.sections.keySet()) {
                Adapter adapter = sections.get(section);
                int size = adapter.getCount() + 1;

                // check if position inside this section                    
                if (position == 0){ // Null the convertView to have it auto inflate from correct layout (it gets confused otherwise)
                	return headers.getView(sectionnum, null, parent);
                }else if (position < size){
                	return adapter.getView(position - 1, null, parent);
                }else{
                    // otherwise jump into next section
                    position -= size;
                    sectionnum++;
                }
            }
            return null;
		}

		@Override
		public int getCount() {
			// total together all sections, plus one for each section header
            int total = 0;
            for (Adapter adapter : this.sections.values())
                total += adapter.getCount() + 1;
            return total;
		}

		@Override
		public Object getItem(int position) {
			for (String section : this.sections.keySet()) {
                Adapter adapter = sections.get(section);
                int size = adapter.getCount() + 1;

                // check if position inside this section
                if (position == 0) return section;
                if (position < size) return adapter.getItem(position - 1);

                // otherwise jump into next section
                position -= size;
            }
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		
		public int getItemViewType(int position) {  
	        int type = 1;  
	        for(String section : this.sections.keySet()) {  
	            Adapter adapter = sections.get(section);  
	            int size = adapter.getCount() + 1;  
	              
	            // check if position inside this section   
	            if(position == 0) return 0;  
	            if(position < size) return type + adapter.getItemViewType(position - 1);  
	  
	            // otherwise jump into next section  
	            position -= size;  
	            type += adapter.getViewTypeCount();  
	        }  
	        return -1;  
	    }	
		
		// We implement this method to return 'true' for all view types we want to pin
	      @Override
	      public boolean isItemViewTypePinned(int viewType) {
	          return viewType == 0;
	      }
	}
}