package edu.purdue.tada;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import edu.purdue.tada.ActivityBridge;

/**
 * Pin page displays an image and the food tags for the user to change
 * and confirm.
 */
public class PinPage extends BaseActivity {
    int i;
	Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinpage);
		
		// Makes background image
		String imagePath = ActivityBridge.getInstance().getReviewImagePath();
		ImageView picture = (ImageView) findViewById(R.id.backgroundPicture);
		picture.setImageBitmap(BitmapFactory.decodeFile(recSaved + "/" + imagePath));

        // create default buttons base on tag file
        createButtons();
        // create UI buttons
        layoutButtons();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
    /*
        Author: Jason Lin
        Created: 12/2/2014
        createButtons function, creates default buttons on the page base on the tag file retrieved
        Modified: 2/11/2015
        modification: this code segment was in the main block, moved it to a function block for cleaner look
     */
    public void createButtons() {
        //int pinNumber = ActivityBridge.getInstance().getfoodPinsSize();
        final RelativeLayout rm = (RelativeLayout) findViewById(R.id.relativePins);
        float screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        float screenHeight = getWindowManager().getDefaultDisplay().getHeight();

        // dynamic creating buttons on the page base on the tagfile parsed
        RelativeLayout.LayoutParams params;
        i = 1;
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
            btn.setId(i);
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
        Author: Jason Lin
        Created: 1/12/2015
        layoutButtons function, creating all UI buttons on the screen
     */
    public void layoutButtons() {
        // creating edit text for the prompt
        final EditText txt = new EditText(this);
        // Creating OK button in prompt
        final Button addOK = new Button(this);
        // Creating Cancel button in prompt
        final Button addNo = new Button(this);
        // adding onClick to "add" button
        Button addpin = (Button) findViewById(R.id.addpin);
        addpin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final int width_c = getWindowManager().getDefaultDisplay().getWidth()/2-50;
                final int height_c = getWindowManager().getDefaultDisplay().getHeight()/2-80;
                // calling the prompt view
                final Button newpin = new Button(context);
                LayoutInflater rm = LayoutInflater.from(context);
                View promptsView = rm.inflate(R.layout.addprompt,null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView.findViewById(R.id.input);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        final RelativeLayout rm = (RelativeLayout) findViewById(R.id.relativePins);
                                        RelativeLayout.LayoutParams params;
                                        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        RelativeLayout rl = new RelativeLayout(context);
                                        // initialize the button to center of screen
                                        params.setMargins(width_c,height_c, 0, 0);
                                        newpin.setId(i);
                                        newpin.setWidth(100);
                                        newpin.setHeight(100);
                                        newpin.setText(userInput.getText());
                                        newpin.setLayoutParams(params);
                                        //newpin.setOnTouchListener(new myTouchListener());
                                        rl.addView(newpin);
                                        rm.addView(rl);
                                        // update the tagfile with coordinate
                                        int x = (width_c+50)*2560/((width_c+50)*2);
                                        int y = (height_c+80)*1920/((height_c+80)*2);
                                        String c = Integer.toString(x)+','+Integer.toString(y);
                                        ActivityBridge.getInstance().addNewPin(userInput.getText().toString(),c);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
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
