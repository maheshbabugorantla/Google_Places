package edu.purdue.tada;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;


public class ReviewActivity extends BaseActivity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.review_layout);
		
		ListView lv = (ListView) findViewById(R.id.reviewList);		
		ArrayList<ReviewContainer> review = new ArrayList<ReviewContainer>();
		
		for(int i = 1; i < 6; i++){
			ReviewContainer rc = new ReviewContainer("Date " + i);
			for(int j = 0; j < i + 1; j++){
				rc.addItem("imageString", "time " + j);
			}
			review.add(rc);
		}	
		
		ReviewAdapter adapter = new ReviewAdapter(this);
		ArrayAdapter<String> itemAdapter = null;
		
		for(ReviewContainer rc : review){
			itemAdapter = new ArrayAdapter<String>(this, R.layout.review_list_item, R.id.reviewTime);
			for(ReviewItem ri : rc.getItems()){
				itemAdapter.add(ri.getTime());
			}
			adapter.addSection(rc.getDate(), itemAdapter);
		}	
		
		lv.setAdapter(adapter);
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