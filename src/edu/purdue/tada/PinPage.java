package edu.purdue.tada;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import edu.purdue.tada.ActivityBridge;


public class PinPage extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinpage);
		/*	for debugging purpose
		String faketagString = "FFFFFFFF6638EC19F5BD42C8AC0EF9E86EA0831C\n3\n" +
				"0\n2576\t791\n6310100\tapple\n63101000\tapple\n63107010\tbanana\n" +
				"14010100\tcheese\n63101000\tapple\n0\n1455\t1269\n21500100\tground beef\n" +
				"21500100\tground beef\n41201010\tbeans\n63107010\tbanana\n63101000\tapple\n" +
				"0\n144\t1497\n11112110\tmilk\n11112110\tmilk\n91501010\tjello\n41201010\tbeans\n" +
				"63107010\tbanana\n";
		
		String[] tokens = faketagString.split("\n|\t"); 			// tokenize the input stream by splitting \t and \n
		int size = Integer.parseInt(tokens[1]);						// size is how many food pins tag files provide
		String [] pinCoord = new String[size];						// a string to catch the pins' coordinates
		ArrayList<String> foodNames = new ArrayList<String>();		// an array list to store food names on each pin
		ArrayList<ArrayList<String>> parts = new ArrayList<ArrayList<String>>();	// creating sub list for foodNames
		
		// iterating through the pin coordinates, saves into the array of string for later use
		for(int i = 0; i < size; i++) {
			pinCoord[i] = tokens[i*13+3] + "," + tokens[i*13+4];
		}
		// adding all food names into a big array
		for(int i = 0; i < size; i++) {
			foodNames.add(tokens[i*13+6]);
			foodNames.add(tokens[i*13+8]);
			foodNames.add(tokens[i*13+10]);
			foodNames.add(tokens[i*13+12]);
			foodNames.add(tokens[i*13+14]);
		}
		// splitting the foodNames into small sub array to store into the mapping
		for(int i = 0; i < foodNames.size(); i+=5) {
			parts.add(new ArrayList<String>(foodNames.subList(i,i+5)));
		}
		// mapping the coordinates to the array of food names
		for(int i = 0; i < size; i++) {
			foodPins.put(pinCoord[i],parts.get(i));
		}*/
		// above code is from tag file parsing
		
		int pinNumber = ActivityBridge.getInstance().getfoodPinsSize();
		final RelativeLayout rm = (RelativeLayout) findViewById(R.id.relativePins);
	    RelativeLayout.LayoutParams params;
	    int i = 1;
	    for(String key : ActivityBridge.getInstance().getfoodPinsKeys()) {
	    	// Extracting the x,y coordinates from the key
	    	String [] coord = key.split(",");
	    	int xcoord = Integer.parseInt(coord[0]);
	    	int ycoord = Integer.parseInt(coord[1]);
	    	
	    	// dynamically reallocating the params
	    	params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	    	// Creating a Relative view to hold the button
	    	RelativeLayout rl = new RelativeLayout(this);
	    	// Creating a button
		    final Button btn = new Button(this);
		    // button settings
		    btn.setId(i++);
		    btn.setText(ActivityBridge.getInstance().getfoodPinsNames(key).get(0));
		    //System.out.println(pinNumber);
		    params.setMargins(xcoord/1028*100, ycoord/512*100,0,0);
		    btn.setLayoutParams(params);
		    
		    rl.addView(btn);
		    rm.addView(rl);
		    Log.d("Button",key);
	    }
		
	}

}
