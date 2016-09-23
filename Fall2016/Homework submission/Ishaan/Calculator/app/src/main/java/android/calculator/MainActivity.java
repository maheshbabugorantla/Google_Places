package android.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    String Total = "";
    double V1, V2;
    String Sign = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClick(View V)
    {
        Button button = (Button)V;
        String str = button.getText().toString();
        Total += str;
        EditText edit = (EditText)findViewById(R.id.editText);
        edit.setText(Total);
    }

    public void OnAdd(View V)
    {
        V1 = Double.parseDouble(Total);
        Total = "";
        Button button = (Button)V;
        String str = button.getText().toString();
        Sign = str;
        EditText edit = (EditText)findViewById(R.id.editText);
        edit.setText("");
    }

    public void OnCalculate(View V)
    {
        EditText edit = (EditText)findViewById(R.id.editText);
        String str2 = edit.getText().toString();
        V2 = Double.parseDouble(str2);
        double Grand_Total = 0;
        if(Sign.equals("+"))
        {
            Grand_Total = V1 + V2;
        }
        else if(Sign.equals("-"))
        {
            Grand_Total = V1 - V2;
        }
        else if(Sign.equals("X"))
        {
            Grand_Total = V1 * V2;
        }
        else if(Sign.equals("/"))
        {
            Grand_Total = V1 / V2;
        }

        edit.setText(Grand_Total+"");
    }

    public void OnClear(View V)
    {
        EditText edit = (EditText)findViewById(R.id.editText);
        edit.setText("");
        Total = "";
    }

}
