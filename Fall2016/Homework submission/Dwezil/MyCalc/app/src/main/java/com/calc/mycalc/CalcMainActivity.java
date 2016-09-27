package com.calc.mycalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//additional imports
import android.view.View; //using widgets
import android.widget.Button; //using buttons
import android.widget.TextView; //using output view


public class CalcMainActivity extends AppCompatActivity {

    public String sign ="";
    public Double tmp=0.00, tmp2=0.00, total;
    public boolean result=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_main);

        final Button buttonZero = (Button)findViewById(R.id.buttonZero);
        Button buttonOne = (Button)findViewById(R.id.buttonOne);
        Button buttonTwo = (Button)findViewById(R.id.buttonTwo);
        Button buttonThree = (Button)findViewById(R.id.buttonThree);
        Button buttonFour = (Button)findViewById(R.id.buttonFour);
        Button buttonFive = (Button)findViewById(R.id.buttonFive);
        Button buttonSix = (Button)findViewById(R.id.buttonSix);
        Button buttonSeven = (Button)findViewById(R.id.buttonSeven);
        Button buttonEight = (Button)findViewById(R.id.buttonEight);
        Button buttonNine = (Button)findViewById(R.id.buttonNine);

        Button buttonAdd = (Button)findViewById(R.id.buttonAdd);
        Button buttonSubtract = (Button)findViewById(R.id.buttonSubtract);
        Button buttonMultiply = (Button)findViewById(R.id.buttonMultiply);
        Button buttonDivide = (Button)findViewById(R.id.buttonDivide);

        Button buttonC = (Button)findViewById(R.id.buttonC);
        Button buttonEquals = (Button)findViewById(R.id.buttonEquals);

        buttonZero.setOnClickListener(
                //Button0 interface
                new Button.OnClickListener() {
                    //Button0 callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(result==true){
                            output.setText("0");
                            result=false;
                            return;
                        }
                        output.append("0");
                    }
                }
        );

        buttonOne.setOnClickListener(
                //Button1 interface
                new Button.OnClickListener() {
                    //Button1 callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(result==true){
                            output.setText("1");
                            result=false;
                            return;
                        }
                        output.append("1");
                    }
                }
        );

        buttonTwo.setOnClickListener(
                //Button2 interface
                new Button.OnClickListener() {
                    //Button2 callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(result==true){
                            output.setText("2");
                            result=false;
                            return;
                        }
                        output.append("2");
                    }
                }
        );

        buttonThree.setOnClickListener(
                //Button3 interface
                new Button.OnClickListener() {
                    //Button3 callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(result==true){
                            output.setText("3");
                            result=false;
                            return;
                        }
                        output.append("3");
                    }
                }
        );

        buttonFour.setOnClickListener(
                //Button4 interface
                new Button.OnClickListener() {
                    //Button4 callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(result==true){
                            output.setText("4");
                            result=false;
                            return;
                        }
                        output.append("4");
                    }
                }
        );

        buttonFive.setOnClickListener(
                //Button5 interface
                new Button.OnClickListener() {
                    //Button5 callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(result==true){
                            output.setText("5");
                            result=false;
                            return;
                        }
                        output.append("5");
                    }
                }
        );

        buttonSix.setOnClickListener(
                //Button6 interface
                new Button.OnClickListener() {
                    //Button6 callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(result==true){
                            output.setText("6");
                            result=false;
                            return;
                        }
                        output.append("6");
                    }
                }
        );

        buttonSeven.setOnClickListener(
                //Button7 interface
                new Button.OnClickListener() {
                    //Button7 callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(result==true){
                            output.setText("7");
                            result=false;
                            return;
                        }
                        output.append("7");
                    }
                }
        );

        buttonEight.setOnClickListener(
                //Button8 interface
                new Button.OnClickListener() {
                    //Button8 callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(result==true){
                            output.setText("8");
                            result=false;
                            return;
                        }
                        output.append("8");
                    }
                }
        );

        buttonNine.setOnClickListener(
                //Button9 interface
                new Button.OnClickListener() {
                    //Button9 callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(result==true){
                            output.setText("9");
                            result=false;
                            return;
                        }
                        output.append("9");
                    }
                }
        );

        buttonC.setOnClickListener(
                //ButtonC interface
                new Button.OnClickListener() {
                    //ButtonC callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        output.setText("");
                    }
                }
        );

        buttonAdd.setOnClickListener(
                //ButtonAdd interface
                new Button.OnClickListener() {
                    //ButtonAdd callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(output.getText().toString()!="") {
                            tmp = Double.parseDouble(output.getText().toString());
                            sign = "+";
                            output.setText("");
                        }
                    }
                }
        );

        buttonSubtract.setOnClickListener(
                //ButtonSubtract interface
                new Button.OnClickListener() {
                    //ButtonSubtract callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(output.getText().toString()!="") {
                            tmp = Double.parseDouble(output.getText().toString());
                            sign = "-";
                            output.setText("");
                        }
                    }
                }
        );

        buttonMultiply.setOnClickListener(
                //ButtonMultiply interface
                new Button.OnClickListener() {
                    //ButtonMultiply callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(output.getText().toString()!="") {
                            tmp = Double.parseDouble(output.getText().toString());
                            sign = "*";
                            output.setText("");
                        }
                    }
                }
        );

        buttonDivide.setOnClickListener(
                //ButtonDivide interface
                new Button.OnClickListener() {
                    //ButtonDivide callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(output.getText().toString()!="") {
                            tmp = Double.parseDouble(output.getText().toString());
                            sign = "/";
                            output.setText("");
                        }
                    }
                }
        );

        buttonEquals.setOnClickListener(
                //ButtonEquals interface
                new Button.OnClickListener() {
                    //ButtonEquals callback method
                    public void onClick(View v){
                        TextView output = (TextView)findViewById(R.id.editText);
                        if(output.getText().toString()=="") return;
                        tmp2 = Double.parseDouble(output.getText().toString());

                        if(sign=="+"){
                            total = tmp + tmp2;
                        } else if(sign=="-"){
                            total = tmp - tmp2;
                        } else if (sign=="*"){
                            total = tmp * tmp2;
                        } else if (sign=="/"){
                            if(tmp2 == 0){
                                output.setText("ERROR");
                                tmp=0.0;
                                tmp2=0.0;
                                sign="";
                                return;
                            } else{
                                total = tmp/tmp2;
                            }
                        } else{
                            total = tmp2;
                        }
                        tmp=0.0;
                        tmp2=0.0;
                        sign="";
                        output.setText(total.toString());
                        result=true;
                    }
                }
        );
    }
}
