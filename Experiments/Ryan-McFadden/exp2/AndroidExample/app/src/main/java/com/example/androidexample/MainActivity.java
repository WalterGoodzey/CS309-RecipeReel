package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;     // define message textview variable

    private TextView evenOddText;     // define second message textview variable - ADDED
    private Button counterButton;     // define counter button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        counterButton = findViewById(R.id.main_counter_btn);// link to counter button in the Main activity XML
        evenOddText = findViewById(R.id.even_or_odd_txt);

        /* extract data passed into this activity from another activity */
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            messageText.setText("Intent Example");
            evenOddText.setText("No data to show");
        } else {
            String number = extras.getString("NUM");  // this will come from LoginActivity
            messageText.setText("The number was " + number);
            int num = Integer.parseInt(number);
            if(num % 2 == 0) {
                evenOddText.setText("The number is even");
            } else {
                evenOddText.setText("The number is odd");
            }
        }

        /* click listener on counter button pressed */
        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when counter button is pressed, use intent to switch to Counter Activity */
                Intent intent = new Intent(MainActivity.this, CounterActivity.class);

//                if(extras != null){
//                    intent.putExtra("LASTNUM", extras.getString("NUM")); //key value to pass to Counter Activity - ADDED
//                }
                startActivity(intent);
            }
        });
    }
}