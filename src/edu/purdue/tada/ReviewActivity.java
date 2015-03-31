package edu.purdue.tada;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.text.DateFormat; 

/**
 * Review activity generates a list based on the images taken using the app.
 * The user goes through each entry of the list to review their activity and
 * to confirm the server's food guesses.
 * 
 * @author Ben Klutzke, Parth Patel
 * 
 */
public class ReviewActivity extends BaseActivity{
	
	// Note that the refresh button should do adapter.notifyDataSetChanged();
	
	protected static final int TAG_REQUEST = 101;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.review_layout);
		
		ListView lv = (ListView) findViewById(R.id.reviewList);
		Button bv = (Button) findViewById(R.id.refresh);
		//Button more = (Button)findViewById(R.id.more_button);
		Button searchButton = (Button) findViewById(R.id.reviewSearch);
		
		//Adding Button to listview at footer Parth Patel 3.28.15
		Button more = new Button(this);
		more.setText("Load More");
		lv.addFooterView(more);
		
		//Getting adapter
		final ReviewAdapter adapter = generateReviewAdapter(); 
		
		if(adapter != null)
			lv.setAdapter(adapter);
		
		// Date search dialog
		Calendar c = Calendar.getInstance();		
		final DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {			
			public void onDateSet(DatePicker view, int year, int month, int day) {
				adapter.filterByDate(day, month, year);
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		dpd.setTitle("Select Date");
		
		final LayoutInflater inflater = getLayoutInflater();
		
		// onselect
		lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
				if(adapter.getItemViewType(position) == adapter.HEADER_TYPE){
					// Header was clicked, collapse section (eventually)
					System.out.println("Header");
				}else{
					// Item was clicked, open up request tag from server
					System.out.println("Item");
					ActivityBridge.getInstance().setReviewImagePath(((ReviewItem)adapter.getItem(position)).getImage1());
					Intent intent = new Intent();
					intent.setClass(ReviewActivity.this,HttpsReceiveTag.class);
	        		startActivityForResult(intent, TAG_REQUEST);
				}
				
			}
		});
		
		searchButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				View searchDialogLayout = inflater.inflate(R.layout.search_dialog_layout, null);
				final Dialog d = new Dialog(ReviewActivity.this);
				d.setContentView(searchDialogLayout);
				d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
				d.setCanceledOnTouchOutside(true);
				d.show();
				
				Button dateButton = (Button) d.findViewById(R.id.review_search_date);
				Button foodButton = (Button) d.findViewById(R.id.review_search_food);
				Button mealButton = (Button) d.findViewById(R.id.review_search_meal);

				dateButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						d.dismiss();
						dpd.show();
					}			
				});
				
				foodButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						d.dismiss();
						// Food search dialog
						// This must be in here for the dialog to be created multiple times
						// If this is in the onCreate for the activity, app will crash on second open (not an issue for datepickerdialog)
						final AlertDialog.Builder fd = new AlertDialog.Builder(ReviewActivity.this);
						final EditText input = new EditText(ReviewActivity.this);
						input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS); // This prevents a weird non-fatal error about spans with zero length
						fd.setTitle("Enter Food");
						fd.setView(input);
						fd.setPositiveButton("OK", new DialogInterface.OnClickListener() {			
							@Override
							public void onClick(DialogInterface dialog, int which) {
								adapter.filterByFood(input.getText().toString().trim());
							}
						});
						fd.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();				
							}
						});
						fd.show();
					}			
				});
				
				mealButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						d.dismiss();
						final AlertDialog.Builder md = new AlertDialog.Builder(ReviewActivity.this);
						md.setTitle("Select Meal Type");
						CharSequence[] meals = {"Breakfast", "Lunch", "Dinner"};
						md.setItems(meals, new DialogInterface.OnClickListener(){	
							@Override
							public void onClick(DialogInterface dialog, int which) {
								adapter.filterByMeal(which);
							}
						});
						md.show();
					}
				});
			}			
		});
		
		//Gets more reviews *Parth Patel 3/10/15*
		more.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
						//code to generate the next 20 reviews
						//generateReviewAdapter();
						//new loadMorelistView().execute();
				}
		});
	}
	
	private ReviewAdapter generateReviewAdapter(){
		ReviewAdapter adapter = new ReviewAdapter(this);
		InputStream in = null;
		int count = 0;
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
	        while((line = reader.readLine()) != null/* && count <= 10*/) {
	        	// Removed count limit:
	        	// Because the entries of rec_sent.txt are listed with most recent as the last, limiting their display will have to be done differently
	        	// Using this as a limit will only display the 10 oldest entries and new ones are not shown.
	        	lines.add(line);
	        	count = count + 1; //will limit the reviews to be displayed at 10 lines only
	        }
	        
	        in.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
		// Parses .rec files into meaningful data
		ArrayList<ReviewContainer> list = generateList(lines);
		
		// Sort contents of the list appropriately
		// Reverse Order because we want the most recent items to appear top of the list
		Collections.sort(list, Collections.reverseOrder());
		
		// Sets up list adapter
		for(ReviewContainer rc : list){
			Collections.sort(rc.getItems(), Collections.reverseOrder());
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
			String type = "month";
			
			/*Gets Current Date Parth Patel*/
			DateFormat dateFormat = new SimpleDateFormat("MMMM/dd/yyyy");
			String date = dateFormat.format(ri.getDate()).toString();
			Date currentDate = new Date(); 
			String currDate = dateFormat.format(currentDate); // <-- dateFormat.format needs a Date object as parameter, not a string
			if((date).equals(currDate)){
					key = currDate; //sets a new key to Today
					type = "today";
			}
			
			/*Gets Yesterday's Date Parth Patel*/
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE,-1);
//			System.out.println(cal.toString());
			String yesterday = dateFormat.format(cal.getTime());
			if((date).equals(yesterday)){
					key = yesterday; //sets a new key to Yesterday
					type = "yesterday";
			}
			
			ri.setSubDate(new SimpleDateFormat("E, MMMM d h:mm a").format(ri.getDate()).toString());
			
			// Basically sorts the .recs by their month and year. This will change when there are more .recs to mess with
			if(map.containsKey(key)){
				map.get(key).addItem(ri);
			}else{
				ReviewContainer rc = new ReviewContainer(key, type);
				rc.addItem(ri);
				list.add(rc);
				map.put(key, rc);
			}
		}
		
		return list;
	}
	
	/*private class BackgroundThread extends AsyncTask<Void,Void,Void>{
		@Override
		protected void onPreExecute(){}
		protected Void doInBackground(Void... unused){
			runOnUiThread(new Runnable(){
				public void run(){}
			});
			return(null);
		}
		protected void onPostExecute(Void unused){}
	}
	*/
/*	private class loadMoreListView extends AsyncTask<Void,Void,Void>{
		@Override
		protected void onPreExecute(){
			pDialog = new ProgressDialog(
	                ReviewActivity.this);
	        pDialog.setMessage("Please wait..");
	        pDialog.setIndeterminate(true);
	        pDialog.setCancelable(false);
	        pDialog.show();
		}
		
	}*/
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
	        hash = reader.readLine(); // Not really a hash, it's the same everywhere
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Starts the pin page activity after receiving tag file from server
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("Review gets result: "+resultCode);
		if (requestCode == TAG_REQUEST) {
			System.out.println("Successfully requested tag file");
			Intent intent = new Intent();
			intent.setClass(ReviewActivity.this, PinPage.class);
			startActivity(intent);
		}
	}
	
	/**
	 * Adapter for the review list. Handles pinned section headers.
	 * 
	 * @author Ben Klutzke
	 * 
	 */
	private class ReviewAdapter extends BaseAdapter implements PinnedSectionListAdapter {		
		private ArrayList<ReviewContainer> list = new ArrayList<ReviewContainer>();
		LayoutInflater inflater = null;
		
		private final int HEADER_TYPE = 0;
		private final int ITEM_TYPE = 1;

		public ReviewAdapter(Context context){
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		public void filterByMeal(int which) {
			Context context = getApplicationContext();
			String text = "List Filtered by Meal Type: ";
			// Breakfast = 0; Lunch = 1; Dinner = 2; Cannot be other values unless dialog is changed
			text += (which == 0) ? "Breakfast" : (which == 1) ? "Lunch" : "Dinner";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}

		public void filterByFood(String trim) {
			Context context = getApplicationContext();
			String text = "List Filtered by Food: " + trim;
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}

		public void filterByDate(int day, int month, int year) {
			Context context = getApplicationContext();
			// For some reason it adds 1900 to the year so let's get rid of that
			Date date = new Date(year - 1900, month, day);
			String text = "List Filtered by Date: " + new SimpleDateFormat("MMMM/dd/yyyy").format(date).toString();
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}

		public void addSection(ReviewContainer rc){
			list.add(rc);
		}
		
		// convertView is a previously used view of the same view type supplied by ListView to improve performance
		public View getView(int position, View convertView, ViewGroup parent){
			// Determines whether the view at this position is a section header or an item
			int viewType = getItemViewType(position);
			
			// Inflate a view from the appropriate layout if convertView is not supplied
			if(convertView == null){
				if(viewType == HEADER_TYPE){
					convertView = inflater.inflate(R.layout.review_list_container, parent, false);
				}else{
					convertView = inflater.inflate(R.layout.review_list_item, parent, false);
				}
			}
			
			// Provide appropriate contents for each view
			if(viewType == HEADER_TYPE){
				ReviewContainer rc = (ReviewContainer) getItem(position);
				TextView tv = (TextView) convertView.findViewById(R.id.list_header_title);
				
				/*Creates Tabs for Today, Yesterday, and Month Parth Patel*/
				if(rc.getType() == "today"){   //Gets type from reviewContainer.java
						tv.setText("Today");  
				}
				else if(rc.getType() == "yesterday"){
						tv.setText("Yesterday");
				}
				else{
					tv.setText(rc.getDateString()); //If it is not today or yesterday
				}
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
				bld.setText(ri.getBLD());
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

	/**
	 * Review activity list is split into sections. This class details the contents
	 * of each section
	 * 
	 * @author Ben Klutzke
	 * 
	 */
	
	private class ReviewContainer implements Comparable<ReviewContainer>{
		private Date date;
		private ArrayList<ReviewItem> items;
		private String type;
		
		public ReviewContainer(String d, String type){
			this.type = type;
			try {
				if(type.equals("month"))
					this.date = new SimpleDateFormat("MMMM, yyyy", Locale.ENGLISH).parse(d);
				else // today or yesterday
					this.date = new SimpleDateFormat("MMMM/dd/yyyy", Locale.ENGLISH).parse(d);
					
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.date = null;
			}
			items = new ArrayList<ReviewItem>();
		}
		public String getType(){ //returns private variable "type" Parth Patel
			return type;
		}
		public void addItem(ReviewItem ri){
			items.add(ri);
		}
		public Date getDate(){
			return date;
		}	
		public String getDateString(){
			return (type.equals("month")) ? new SimpleDateFormat("MMMM, yyyy").format(date).toString() : new SimpleDateFormat("MMMM/dd/yyyy").format(date).toString();
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


	/**
	 * Class detailing the contents of each row for the review activity
	 * 
	 * @author Ben Klutzke
	 * 
	 */
	
	private class ReviewItem implements Comparable<ReviewItem> {
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
}
