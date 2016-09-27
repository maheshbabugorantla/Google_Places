package com.example.jasmine.calculator;
/** Jasmine Jia
 * Sep 1 2016
 * 4-functional calculator*/
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
public class MainActivity extends Activity implements View.OnClickListener {

    EditText a;
    EditText b;

    Button btnAdd;
    Button btnSub;
    Button btnMult;
    Button btnDiv;

    TextView TvResult;

    String oper = "";

    /** when the activity is first called*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find id of the elements
        a = (EditText) findViewById(R.id.num1);
        b = (EditText) findViewById(R.id.num2);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMult = (Button) findViewById(R.id.btnMult);
        btnDiv = (Button) findViewById(R.id.btnDiv);

        TvResult = (TextView) findViewById(R.id.result);

        // set listener for bottons
        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMult.setOnClickListener(this);
        btnDiv.setOnClickListener(this);

    }
    /** onclick activity */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        float num1 = 0;
        float num2 = 0;
        float result = 0;

        // check if users have put any numbers in the field
        if (TextUtils.isEmpty(a.getText().toString())
                || TextUtils.isEmpty(b.getText().toString())) {
            return;
        }

        // read from EditText to get the 2 numbers as float
        num1 = Float.parseFloat(a.getText().toString());
        num2 = Float.parseFloat(b.getText().toString());

        // perform corresponding operation and store it in oper
        switch (v.getId()) {
            case R.id.btnAdd:
                oper = "+";
                result = num1 + num2;
                break;
            case R.id.btnSub:
                oper = "-";
                result = num1 - num2;
                break;
            case R.id.btnMult:
                oper = "*";
                result = num1 * num2;
                break;
            case R.id.btnDiv:
                oper = "/";
                result = num1 / num2;
                break;
            default:
                break;
        }

        // print out the result line
        TvResult.setText(num1 + " " + oper + " " + num2 + " = " + result);
    }
}