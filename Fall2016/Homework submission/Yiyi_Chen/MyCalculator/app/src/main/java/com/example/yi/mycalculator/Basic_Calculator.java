//Created by Yiyi Chen
//Created on Sep 5th 2016
//Simple four function calculator

package com.example.yi.mycalculator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Pattern;


public class Basic_Calculator extends Activity {
    //Initial the screen display and operator to be none
    private TextView _screen;
    private String display = "";
    private String currentOperator = "";
    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic__calculator);
        _screen = (TextView)findViewById(R.id.textView);
        _screen.setText(display);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_basic__calculator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Important update function that will refresh the screen every time an activity happen
    private void updateScreen(){
        _screen.setText(display);
    }

    //handle the action about click a number
    public void onClickNumber(View v){
        //if there exist an operation result from last operation, it needs to be cleared first
        if(result != ""){
            clear();
            updateScreen();
        }
        //get the click number and append to the existing number
        Button b = (Button) v;
        display += b.getText();
        updateScreen();
    }

        //function to check whether a action is on operator
    private boolean isOperator(char op){
        switch (op){
            case '+' : return true;
            case '-' : return true;
            case '*' : return true;
            case '/' : return true;
            default: return false;
        }
    }

        //handle click operator action
    public void onClickOperator(View v){
        //if there is nothing on the screen, just return
        if(display == "") return;

        Button b = (Button) v;

        if(result != ""){
            String _display = result;
            clear();
            display = _display;
        }

        //if else statement to check to replace the operator or do the calculation
        if(currentOperator != ""){
            Log.d("CalcX", ""+display.charAt(display.length()-1));
            if(isOperator(display.charAt(display.length()-1))){
                display = display.replace(display.charAt(display.length()-1), b.getText().charAt(0));
                updateScreen();
                return;
            }else{
                getResult();
                display = result;
                result = "";
            }
            currentOperator = b.getText().toString();
        }

        display+= b.getText();
        currentOperator = b.getText().toString();
        updateScreen();
    }

    //clear all existing operation
    private void clear(){
        display = "";
        currentOperator = "";
        result = "";
    }

    //Clear the all operation and clean the screen
    public void onClickClear(View v){
        clear();
        updateScreen();
    }

    //Basic calculator operation
    private double operate(String a, String b, String op){
        switch (op){
            case "+": return Double.valueOf(a) + Double.valueOf(b);
            case "-": return Double.valueOf(a) - Double.valueOf(b);
            case "*": return Double.valueOf(a) * Double.valueOf(b);
            case "/": try {
                return Double.valueOf(a) / Double.valueOf(b);
            }catch (Exception e){
                Log.d("Calc", e.getMessage());
            }
            default: return -1;
        }
    }

    //get the result from calculation
    private boolean getResult(){
        if(currentOperator == "") return false;
        String[] operation = display.split(Pattern.quote(currentOperator));
        if(operation.length < 2) return false;
        result = String.valueOf(operate(operation[0], operation[1], currentOperator));
        return true;
    }

    //the special condition when user click equal operator
    public void onClickEqual(View v){
        if(display == "")return;
        if(!getResult()) return;
        _screen.setText(display + "\n" + String.valueOf(result));
    }
}
