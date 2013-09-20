package edu.purdue.tada;

import android.app.ActivityGroup;  
import android.content.Intent;  
import android.os.Bundle;  
import android.view.View;  
import android.view.Window;  
  
public class SettingsGroup extends ActivityGroup{  
      
    public static ActivityGroup group;    
        
    @Override    
    protected void onCreate(Bundle savedInstanceState) {    
        // TODO Auto-generated method stub    
        super.onCreate(savedInstanceState);
        System.out.println("settings group on create.");
        group = this; 
        Intent intent = new Intent(this, SettingsActivity.class).    
        		addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);        
		Window w = group.getLocalActivityManager().startActivity("SettingsActivity",intent);    
		View view = w.getDecorView();    
		group.setContentView(view); 
            
    }    
    
}  