package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity{

    private TextView messageText;   // define message textview variable

    private Button returnBtn;       // define button to return to main
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);             // link to Second activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.second_message_txt);      // link to message textview in the Main activity XML
        returnBtn = findViewById(R.id.second_button);
        messageText.setText("Welcome to the Second Activity!");

        /* when second btn is pressed, switch to SecondActivity */
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
