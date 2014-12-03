package edu.purdue.tada;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import edu.purdue.tada.ActivityBridge;

/**
 * Pin page displays an image and the food tags for the user to change
 * and confirm.
 */
public class PinPage extends BaseActivity {
	Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinpage);
		
		// Makes background image
		String imagePath = ActivityBridge.getInstance().getReviewImagePath();
		ImageView picture = (ImageView) findViewById(R.id.backgroundPicture);
		picture.setImageBitmap(BitmapFactory.decodeFile(recSaved + "/" + imagePath));
		
		//int pinNumber = ActivityBridge.getInstance().getfoodPinsSize();
		final RelativeLayout rm = (RelativeLayout) findViewById(R.id.relativePins);
		float screenWidth = getWindowManager().getDefaultDisplay().getWidth();
	    float screenHeight = getWindowManager().getDefaultDisplay().getHeight();
	    
	    RelativeLayout.LayoutParams params;
	    int i = 1;
	    for(String key : ActivityBridge.getInstance().getfoodPinsKeys()) {
	    	// Extracting the x,y coordinates from the key
	    	String [] coord = key.split(",");
	    	float xcoord = Integer.parseInt(coord[0]);
	    	float ycoord = Integer.parseInt(coord[1]);
	    	
	    	// dynamically reallocating the params
	    	params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	    	// Creating a Relative view to hold the button
	    	RelativeLayout rl = new RelativeLayout(this);
	    	// Creating a button
		    final Button btn = new Button(this);
		    // button settings
		    btn.setId(i++);
		    btn.setText(ActivityBridge.getInstance().getfoodPinsNames(key).get(0));
		    btn.setOnClickListener(new myOnClickListener(ActivityBridge.getInstance().getfoodPinsNames(key),i++) {});
		    float w = xcoord/2560*screenWidth;
		    float h = ycoord/1920*screenHeight;
		    //System.out.println(pinNumber);
		    params.setMargins((int)w, (int)h,0,0);	
		    btn.setLayoutParams(params);
		    
		    rl.addView(btn);
		    rm.addView(rl);
		    Log.d("ButtonTag",key);
	    }
		
	}
	/* 
	 * Author: Jason Lin
	 * Created: 12/2/2014
	 * 
	 * OnClickListener implementation
	 * Parameters: 	ArrayList<String> items - the list of food associated to the button generating.
	 * 				Int id					- the id associated to the button
	 */
	public class myOnClickListener implements OnClickListener {
		ArrayList<String> items = new ArrayList<String>();
		int id;
		public myOnClickListener(ArrayList<String> i, int id) {
			this.items = i;
			this.id = id;
		}
		@Override
		public void onClick(View arg0) {
			CharSequence [] array = {items.get(1),items.get(2), items.get(3), items.get(4)};
    		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
    		alertDialogBuilder.setTitle("Choices of food:");
    		alertDialogBuilder
    			.setItems(array, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Button btn = (Button) findViewById(id);
						btn.setText(items.get(which+1));
						Toast toast = Toast.makeText(context, "Changed to "+items.get(which+1), Toast.LENGTH_SHORT);
						toast.show();
					}
				})
    			.setCancelable(false)
    			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int id) {
    					dialog.cancel();
    				}
    			});
    		AlertDialog alertDialog = alertDialogBuilder.create();
    		alertDialog.show();
		}
	}	

}
