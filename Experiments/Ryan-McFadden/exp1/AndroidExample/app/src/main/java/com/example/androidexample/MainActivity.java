package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable

    private Button mainBtn;         // define button to go to SecondActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        mainBtn = findViewById(R.id.main_move_btn);         // link to main button in the Main activity XML
        messageText.setText("Hello World");

        /* click listener on main button pressed */
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when main button is pressed, use intent to switch to SecondActivity */
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }


}