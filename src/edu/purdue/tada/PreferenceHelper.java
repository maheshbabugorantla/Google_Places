package edu.purdue.tada;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper {

	public static final String KEY_THEME = "theme";
	public static final String KEY_TIPS = "tips_option";
	
    private static SharedPreferences.Editor mEditor = null;
    private static SharedPreferences mPreferences = null;
    
	public PreferenceHelper(Context context){}
	
	//get a SharePreferences instance that points to the default file that is used by the preference framework in the given context
    private static SharedPreferences getSharedPreferences(Context paramContext) {
        if (mPreferences == null)
            mPreferences = PreferenceManager.getDefaultSharedPreferences(paramContext);
        return mPreferences;
    }
    //create an Editor to modify key-values in the default file
    private static SharedPreferences.Editor getEditor(Context paramContext) {
        if (mEditor == null)
            mEditor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
        return mEditor;
    }
    //get the value of KEY_THEME from the default file 
    public static int getTheme(Context context){
        return PreferenceHelper.getSharedPreferences(context).getInt(KEY_THEME, R.style.AppTheme_powderblue);
    }
    //modify the value of KEY_THEME
    public static void setTheme(Context context, int theme){
        getEditor(context).putInt(KEY_THEME, theme).commit();
    }
    //get the value of KEY_TIPS from the default file 
    public static boolean getTips(Context context){
        return PreferenceHelper.getSharedPreferences(context).getBoolean(KEY_TIPS, true);
    }
    //modify the value of KEY_TIPS
    public static void setTips(Context context, Boolean tips){
        getEditor(context).putBoolean(KEY_TIPS, tips).commit();
    }

}