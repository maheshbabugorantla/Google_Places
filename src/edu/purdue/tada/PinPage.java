package edu.purdue.tada;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import edu.purdue.tada.ActivityBridge;
import edu.purdue.tada.images.TouchImageView;

/*
 * Pin page displays an image and the food tags for the user to change
 * and confirm.
 */

/* Issues:
 * TextView cannot be updated since the space is occupied by same relative layout
 * Precision loss in the coordinates
 * Shadowing not working
 * Dropping have issue on the edge which goes out of bound
 */

public class PinPage extends BaseActivity {
    int i;
	Context context = this;
    // mapping pins' Id to the coordinates to prevent precision loss
    public static Map<Integer[],List<String>> pinCoord = new HashMap<Integer[], List<String>>();
    // an array of pin coordinates that was created/modified, index is the id number
    public static Map<Integer,Integer[]> pinId = new HashMap<Integer, Integer[]>();
    // mapping textViews' Id to the coordinate to follow any changes in the pin
    public static Map<Integer,String> textId = new HashMap<Integer, String>(); //
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinpage);
		
		// Makes background image
		String imagePath = ActivityBridge.getInstance().getReviewImagePath();
		TouchImageView picture = (TouchImageView) findViewById(R.id.backgroundPicture);
		picture.setImageBitmap(BitmapFactory.decodeFile(recSaved + "/" + imagePath));

        // create default buttons base on tag file
        createButtons();
        // create UI buttons
        layoutButtons();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
    /*
        Author: Vicky Sun
        Created: 3/1/2015
        Initialize TextView's text, only works on initialization or new pin
        Modified @ 3/12/2015
        Modification: can resolve both key and manual input now
     */
    public TextView setTextBlock(String key, int w, int h) {
        TextView tt = new TextView(context);
        try {
            tt.setText(ActivityBridge.getInstance().getfoodPinsNames(key).get(0));
        } catch (Exception e) {
            tt.setText(key);
        }
        tt.setId(1000+i);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(w,h,0,0);
        tt.setLayoutParams(params);
        return tt;
    }
    /*
        Author: Jason Lin
        Created: 12/2/2014
        createButtons function, creates default buttons on the page base on the tag file retrieved
        Modified: 2/11/2015 by Jason Lin
        modification: this code segment was in the main block, moved it to a function block for cleaner look
        Modified: 2/17/2015 by Jason Lin
        modification: add user input into the alertDialog
        Modified: 1/23/2015 by Vicky Sun
        modification: adjust the params so the pins are on the exact coordinate, pin is no longer a box
     */
    public void createButtons() {
        //int pinNumber = ActivityBridge.getInstance().getfoodPinsSize();
        final RelativeLayout rm = (RelativeLayout) findViewById(R.id.relativePins);
        // dynamic creating buttons on the page base on the tagfile parsed
        RelativeLayout.LayoutParams params;
        // getting the actual screen size
        float screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        float screenHeight = getWindowManager().getDefaultDisplay().getHeight();


        i = 1;
        for(String key : ActivityBridge.getInstance().getfoodPinsKeys()) {
            // Extracting the x,y coordinates from the key
            String [] coord = key.split(",");
            // the coordinates of original tag file
            float xcoord = Integer.parseInt(coord[0]);
            float ycoord = Integer.parseInt(coord[1]);
            // modified coordinates for the text with offsets
            float pinX = xcoord/2560*screenWidth;
            float pinY = ycoord/1920*screenHeight;

            // dynamically reallocating the params
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            // Creating a Relative view to hold the button
            RelativeLayout rl = new RelativeLayout(this);
            // Creating a button
            final Button btn = new Button(this);
            // Creating a textView for the text floating above pin
            TextView tx = setTextBlock(key, (int)pinX, (int)pinY-20);
            // setup the internal lists
            Integer [] newcoord = {(int)pinX, (int)pinY};
            // creating a new copy of coordinate : foods
            pinCoord.put(newcoord, ActivityBridge.getInstance().getfoodPinsNames(key));
            // mapping the pin id to the coordinate
            pinId.put(i, newcoord);
            // mapping the text id to the coordinate
            textId.put(i,pinCoord.get(newcoord).get(0));
            // button settings
            btn.setId(i);
            btn.setWidth(80);
            btn.setHeight(80);
            btn.setBackgroundResource(R.drawable.rsz_pin);
            //btn.setText(ActivityBridge.getInstance().getfoodPinsNames(key).get(0));
            // set onclick implementation of onClick
            btn.setOnClickListener(new myOnClickListener(i) {});
            // set onLongClick implementation of long click
            btn.setOnLongClickListener(new myOnLongClickListener(rm, rl,i));
            //System.out.println(pinNumber);
            params.setMargins((int)pinX-20, (int)pinY+25,0,0);
            btn.setLayoutParams(params);
            // add the button to views
            rl.addView(btn);
            rm.addView(tx);
            rm.addView(rl);
            // increment id
            i++;
            Log.d("ButtonTag",key);
        }
    }
    /*
        Author: Jason Lin
        Created: 1/12/2015
        layoutButtons function, creating all UI buttons on the screen
        Modified: 2/21/2015
        Modification: New pin created now is adjusted accordingly to the input length
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
                final int width_c = getWindowManager().getDefaultDisplay().getWidth()/2;
                final int height_c = getWindowManager().getDefaultDisplay().getHeight()/2;
                // calling the prompt view
                final Button newpin = new Button(context);
                // creating a relative view on the main frame
                LayoutInflater rm = LayoutInflater.from(context);
                // finding the prompt page to fit into the rm
                View promptsView = rm.inflate(R.layout.addprompt,null);
                // calling an alert dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // use the prompt page as the layout of alert dialog
                alertDialogBuilder.setView(promptsView);
                // finding the components in the layout
                final EditText userInput = (EditText) promptsView.findViewById(R.id.input);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // create a relative view on the main frame
                                        final RelativeLayout rm = (RelativeLayout) findViewById(R.id.relativePins);
                                        RelativeLayout.LayoutParams params;
                                        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        // create a frame to adjust the button view
                                        RelativeLayout rl = new RelativeLayout(context);
                                        // initialize the button to center of screen
                                        params.setMargins(width_c-20, height_c+25, 0, 0);
                                        // create data for the internal lists
                                        Integer[] newPinCoord = {width_c, height_c};
                                        // update the internal lists with new data
                                        List<String> newFood = Arrays.asList(userInput.getText().toString(), userInput.getText().toString(), userInput.getText().toString(), userInput.getText().toString(), userInput.getText().toString());
                                        pinCoord.put(newPinCoord, newFood);
                                        pinId.put(i, newPinCoord);
                                        textId.put(i, userInput.getText().toString());
                                        // new button setting
                                        newpin.setId(i);
                                        newpin.setBackgroundResource(R.drawable.rsz_pin);
                                        /* add button functionality to the new buttons*/
                                        newpin.setOnClickListener(new myOnClickListener(i));
                                        newpin.setOnLongClickListener(new myOnLongClickListener(rm, rl, i));
                                       // newpin.setOnLongClickListener(new myOnLongClickListener(i));
                                        // show floating texts
                                        TextView tt = setTextBlock(userInput.getText().toString(), width_c, height_c);
                                        //newpin.setText(userInput.getText());
                                        newpin.setLayoutParams(params);
                                        //newpin.setOnTouchListener(new myTouchListener());
                                        rl.addView(newpin);
                                        rm.addView(tt);
                                        rm.addView(rl);
                                        // update the pin id number
                                        i++;
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
	 * Modified: 2/25/2015 by Di Pan
	 * Modification: Added a prompt to ask the user if they want to manually type food when
	 *               cancels the first prompt.
	 * Modified: 3/4/2015 by Di Pan
	 * Modification: Added a editable input block for the user, and passes the result string
	 *               to Vicky Sun's function which changes the text above the pin
	 * 
	 * OnClickListener implementation
	 * onClick calls the arrayList, constructs the selections base on the values in the array,
	 * and changes the text upon selection and edit the arrayList
	 *
	 * Parameters: 	ArrayList<String> items - the list of food associated to the button generating.
	 * 				Int id					- the id associated to the button
	 */
    /* NOTE :: user input should link to food name database, and search according to the inputs*/
	public class myOnClickListener implements OnClickListener {
        List<String> items = new ArrayList<String>();
        int id;
        Integer coord [];

        public myOnClickListener(int id) {
            this.id = id;
            this.coord = pinId.get(id);
            this.items = pinCoord.get(coord);
        }

        @Override
        public void onClick(View arg0) {
            // allocate food items of the button clicked
            CharSequence[] array = {items.get(1), items.get(2), items.get(3), items.get(4)};
            // create alert dialog for food items choices
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Choices of food:");
            alertDialogBuilder
                    .setItems(array, new DialogInterface.OnClickListener() {
                        @Override
                        // set the food items displayed in alert dialog to be clickable
                        public void onClick(DialogInterface dialog, int which) {
                            // put the selected item into the textId list according to the id
                            textId.put(id,items.get(which+1));
                            // construct a new array in the order of selected item as the first in array
                            List<String> newItemOrder = Arrays.asList(items.get(which+1),items.get(1),items.get(2),items.get(3),items.get(4));
                            // re-map the new array to the current coordinate
                            pinCoord.put(coord, newItemOrder);
                            // call the textview correspond to this button
                            TextView tt = (TextView) findViewById(1000+id);
                            // update the textview's text
                            tt.setText(items.get(which+1));
                            // toast a message to show changes
                            Toast toast = Toast.makeText(context, "Changed to " + items.get(which + 1), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    })
                    .setCancelable(false)
                    /* Modified by Di Pan
                     * Modification: add the second prompt to allow the user to manually input the text
                     */
                    .setNegativeButton("Insert/Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //dialog.cancel();
                            // no longer cancels the dialog when clicking "cancel"
                            // building a new prompt
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Insert food/ Exit prompt")
                                    .setMessage("Do you want to manually insert food?")
                                    .setNegativeButton("no", null)
                                            // show editable input for the user to type
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                            builder1.setMessage("Enter type of foods");
                                            // Set an EditText view to get user input
                                            final EditText input = new EditText(context);
                                            // display what user types
                                            builder1.setView(input);
                                            // confirms the changes
                                            builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    Editable value = input.getText();
                                                    TextView tt = (TextView) findViewById(1000+myOnClickListener.this.id);
                                                    tt.setText(value.toString());
                                                    textId.put(myOnClickListener.this.id,value.toString());
                                                    List<String> newItemOrder = Arrays.asList(value.toString(),items.get(1),items.get(2),items.get(3),items.get(4));
                                                    pinCoord.put(coord,newItemOrder);
                                                    /* To Pan Di change the text view text according to my previous code*/
                                                }
                                            });
                                            // finally cancelling all the dialogs
                                            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    dialog.cancel();
                                                }
                                            });
                                            builder1.show();
                                        }
                                    });
                            builder.show();

                        }
                    });
            // display all the dialogs and sub dialogs
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    /*
     * Author: Jason Lin
     * Created 2/2/2015
     * OnLongClickListener Implementation
     * Parameters:  RelativeLayout rm  -    the main layout that the button reside in
     *              RelativeLayout rl  -    the layout of the button
      *             String text        -    the text on the button
     * Drag and Drop implementation.
     */
    public class myOnLongClickListener implements View.OnLongClickListener {
        RelativeLayout rm;
        RelativeLayout rl;
        int id;
        float screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        float screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        public myOnLongClickListener(RelativeLayout main, RelativeLayout sub, int i) {
            this.rm = main;
            this.rl = sub;
            this.id = i;
        }
        @Override
        public boolean onLongClick(View v){
            // find the button's coordinate
            final Integer [] coord = pinId.get(id);
            // extract X and Y coordinate
            final int oldX = coord[0];
            final int oldY = coord[1];
            v.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getActionMasked()) {
                        // Shadowing finger movement
                        case MotionEvent.ACTION_MOVE:
                            /*TextView tt = (TextView) findViewById(1000+id);
                            rm.removeView(tt);
                            rm.removeView(rl);
                            int updateX = (int)event.getX();
                            int updateY = (int)event.getY();
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(oldX+updateX, oldY+updateY,0,0);
                            RelativeLayout rl = new RelativeLayout(context);
                            Button shadowPin = new Button(context);
                            shadowPin.setBackgroundResource(R.drawable.rsz_pin);
                            shadowPin.setLayoutParams(params);
                            rl.addView(shadowPin);
                            myOnLongClickListener.this.rl = rl;
                            rm.addView(rl);*/
                            break;
                        // finalize the updates, and change any necessary data
                        case MotionEvent.ACTION_UP:
                            // remove the old view
                            TextView newT = (TextView) findViewById(1000+id);
                            rm.removeView(newT);
                            rm.removeView(myOnLongClickListener.this.rl);
                            // get the movement X and Y
                            int newX = (int)event.getX();
                            int newY = (int)event.getY();
                            // update the internal lists
                            Integer [] newCoord = {oldX+newX, oldY+newY};
                            // update the pinId
                            pinId.put(id,newCoord);
                            // add new coordinate to the pinCoord list, and remove the old one
                            pinCoord.put(newCoord,pinCoord.get(coord));
                            pinCoord.remove(coord);
                            // create a new view to update the
                            RelativeLayout.LayoutParams final_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                            // change the coordinate of the view
                            final_params.setMargins(oldX+newX, oldY+newY,0,0);
                            RelativeLayout rf = new RelativeLayout(context);
                            // create a button on the new location
                            Button newpin = new Button(context);
                            newpin.setBackgroundResource(R.drawable.rsz_pin);
                            // add button functionality
                            newpin.setOnClickListener(new myOnClickListener(id));
                            newpin.setOnLongClickListener(new myOnLongClickListener(rm,rf,id));
                            // create the textView
                            newT = setTextBlock(pinCoord.get(newCoord).get(0), oldX+newX, oldY+newY-20);
                            newT.setId(1000+id);
                            // update the view coordinate
                            newpin.setLayoutParams(final_params);
                            // display the views
                            rf.addView(newpin);
                            rm.addView(newT);
                            rm.addView(rf);
                            break;
                    }
                    return true;
                }
            });

            return false;
        }

    }

}
