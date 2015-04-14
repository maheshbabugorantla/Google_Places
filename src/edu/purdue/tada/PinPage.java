package edu.purdue.tada;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import edu.purdue.tada.ActivityBridge;

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
    public static Map<Integer,String> textId = new HashMap<Integer, String>();
    // global adapter for the autocompletetextview
    public static ArrayAdapter<String> databaseAdapter;
    //public static ArrayList<String> foodDatabase = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinpage);
		
		// Makes background image
		String imagePath = ActivityBridge.getInstance().getReviewImagePath();
		ImageView picture = (ImageView) findViewById(R.id.backgroundPicture);
		picture.setImageBitmap(BitmapFactory.decodeFile(recSaved + "/" + imagePath));

        // constructs food database from the .dat food DNSF file
        constructAdapter();
        // create default buttons base on tag file
        createButtons();
        // create UI buttons
        layoutButtons();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
    /*
        Author: Jason Lin
        Created: 4/8/2015
        Creates an array list of foods base on the .dat file provided by the course
     */
    public void constructAdapter() {
        ArrayList<String> foodDatabase = new ArrayList<String>();
        BufferedReader reader;
        try {
            final InputStream is = getAssets().open("food.dat");
            reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            line = reader.readLine();
            while(line != null) {
                String sp [] = line.split("\t");
                if(!foodDatabase.contains(sp[1])){
                    foodDatabase.add(sp[1]);
                }
                line = reader.readLine();
            }
            databaseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foodDatabase);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /*
        Author: Vicky Sun
        Created: 3/1/2015
        Initialize TextView's text, only works on initialization or new pin
        Modified @ 3/12/2015
        Modification: can resolve both key and manual input now
        Modified @ 4/13/2015
        Modification: centering the text according to the length
     */
    public TextView setTextBlock(String key, int w, int h) {
        TextView tt = new TextView(context);
        tt.setText(centerText(key));
        tt.setId(1000+i);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(w-25,h+10,0,0);
        tt.setLayoutParams(params);
        return tt;
    }
    /*
        Author: Vicky Sun & Jason Lin
        Created: 4/13/2015
        Basically moved Vicky's code out to a function to be more versatile.
        Made some changes to allow the function to be more flexible
     */
    public String centerText(String t) {
        // find the length of the
        int length = t.length();
        // empty string for spaces on the sides of the text
        String space = new String();
        // default is assuming the text is 18 characters long
        for(int i = 0; i < (18-length)/2; i++){
            space = space.concat(" ");
        }
        // adding the calculated spaces to the text
        String txt = space + t + space;
        return txt;
    }
    /*
     *  Author: Pan Di
     *  Created: 4/1/2015
     *  Delete selected button by finding id and text
     *  Modified @ 4/11/2015 by Jason Lin
     *  Modification: remove corresponding items in the hashmaps.
     */
    public void deleteButton(int i, RelativeLayout rl) {
        // find the button by id
        Button btn = (Button) findViewById(i);
        // find the text by id
        TextView tt = (TextView) findViewById(i+1000);
        // find the pin coordinate by id
        Integer [] coord = pinId.get(i);
        // remove the button from the layout
        rl.removeView(btn);
        // clear out the textview
        tt.setText("");
        // remove the pinCoord content
        pinCoord.remove(coord);
        // remove the text in the hashmap
        textId.remove(i);
        // remove the coordinate mapping to button id
        pinId.remove(i);

    }
    /*
        Author: Jason Lin
        Created: 4/13/2015
        converts the screen size coordinates to image size coordinate
     */
    public HashMap returnCoord() {
        HashMap<Integer [], List<String>> newCoord = new HashMap<Integer[], List<String>>();
        float screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        float screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        for (Integer [] key : pinCoord.keySet()) {
            float newKeyX = key[0]*2560/screenWidth;
            float newKeyY = key[1]*1920/screenHeight;
            Integer [] newKey = {(int) newKeyX, (int) newKeyY};
            newCoord.put(newKey,pinCoord.get(key));
        }
        return newCoord;
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
            TextView tx = setTextBlock(ActivityBridge.getInstance().getfoodPinsNames(key).get(0), (int)pinX, (int)pinY);
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
            btn.setWidth(50);
            btn.setHeight(50);
            btn.setBackgroundResource(R.drawable.rsz_pin);
            //btn.setText(ActivityBridge.getInstance().getfoodPinsNames(key).get(0));
            // set onclick implementation of onClick
            btn.setOnClickListener(new myOnClickListener(i, rl) {});
            // set onLongClick implementation of long click
            btn.setOnLongClickListener(new myOnLongClickListener(rm, rl,i));
            //System.out.println(pinNumber);
            params.setMargins((int)pinX-15, (int)pinY+50,0,0);
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
        Modified: 4/13/2015 by Pujitha Desiraju
        Modification: Add new button to save the pincoord when clicked
        Modified: 4/13/2015 by Jason Lin
        Modification: Recalls the previous activity when the button is clicked, and restores the pin coordinate to picture pixels
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
                                        newpin.setOnClickListener(new myOnClickListener(i, rl));
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
        // calls the confirm button by id
        Button savePins = (Button) findViewById(R.id.save);
        // confirm button onclick listener
        savePins.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // sends the pincoord to activitybridge for process

                ActivityBridge.getInstance().savePins(returnCoord());
                // toast message
                Toast toast = Toast.makeText(context, "Changes saved.", Toast.LENGTH_SHORT);
                toast.show();
                // recalls to the previous activity
                finish();
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
	 * Modified : 4/11/2015 by Jason Lin & Pan Di
	 * Modification: Added delete button functionality, removed redundant confirmation dialog
	 * 
	 * OnClickListener implementation
	 * onClick calls the arrayList, constructs the selections base on the values in the array,
	 * and changes the text upon selection and edit the arrayList
	 *
	 * Parameters: 	ArrayList<String> items - the list of food associated to the button generating.
	 * 				Int id					- the id associated to the button
	 * 			    RelativeLayout rl       - the child view that holds the button
	 */
    /* NOTE :: user input should link to food name database, and search according to the inputs*/
	public class myOnClickListener implements OnClickListener {
        List<String> items = new ArrayList<String>();
        int id;
        Integer coord [];
        RelativeLayout rl;

        public myOnClickListener(int id, RelativeLayout r) {
            this.id = id;
            this.rl = r;
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
                            tt.setText(centerText(items.get(which+1)));
                            // toast a message to show changes
                            Toast toast = Toast.makeText(context, "Changed to " + items.get(which + 1), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    })
                    // can use back key to cancel the dialog
                    .setCancelable(true)
                    /* Modified by Di Pan
                     * Modification: add the second prompt to allow the user to manually input the text
                     * Modified by Jason Lin @ 4/8/2015
                     * Modification: change the Editable textview to AutoCompleteTextView for database searching
                     * Bug: current API does not support AutoComplete in AlertDialog
                     */
                    .setPositiveButton("Insert", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //dialog.cancel();
                            // no longer cancels the dialog when clicking "cancel"
                            // building a new prompt
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Insert Food");
                            final AutoCompleteTextView input = new AutoCompleteTextView(context);
                            input.setAdapter(databaseAdapter);
                            // display what user types
                            builder.setView(input);
                            // confirms the changes
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String value = input.getText().toString();
                                    TextView tt = (TextView) findViewById(1000 + myOnClickListener.this.id);
                                    tt.setText(centerText(value));
                                    textId.put(myOnClickListener.this.id, value.toString());
                                    List<String> newItemOrder = Arrays.asList(value.toString(), items.get(1), items.get(2), items.get(3), items.get(4));
                                    pinCoord.put(coord, newItemOrder);
                                }
                            });
                            builder.show();
                        }
                    })
                    .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           deleteButton(myOnClickListener.this.id, myOnClickListener.this.rl);
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
                        /* temporarily removed due to causing precision errors when creating shadowing effect. */
                        case MotionEvent.ACTION_MOVE:
                            // Temporarily removed because shadowing causing too much bugs
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
                            newpin.setOnClickListener(new myOnClickListener(id, rl));
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
