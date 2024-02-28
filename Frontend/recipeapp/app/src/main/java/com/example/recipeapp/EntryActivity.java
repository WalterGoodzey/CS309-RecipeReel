package com.example.recipeapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * @author Ryan McFadden
 */
public class EntryActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private TextView subText;       // define sub message textview variable - ADDED
    private TextView usernameText;  // define username textview variable
    private Button loginButton;     // define login button variable
    private Button signupButton;    // define signup button variable
    private Button guestButton;    // define guest button variable



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);             // link to entry activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.entry_msg_txt);      // link to message textview in the entry activity XML
//        usernameText = findViewById(R.id.entry_username_txt);// link to username textview in the entry activity XML
        loginButton = findViewById(R.id.entry_login_btn);    // link to login button in the entry activity XML
        signupButton = findViewById(R.id.entry_signup_btn);  // link to signup button in the entry activity XML
        guestButton = findViewById(R.id.entry_guest_btn);  // link to logout button in the entry activity XML - ADDED
        subText = findViewById(R.id.entry_sub_msg_txt);      // link to sub message textview in the entry activity XML

        messageText.setText("Welcome Message");
        subText.setText("Login or Signup");


//        /* extract data passed into this activity from another activity */
//        Bundle extras = getIntent().getExtras();
//        if(extras == null) {
//            subText.setText("Login or Signup"); //modified to subtext
//            usernameText.setVisibility(View.INVISIBLE);             // set username text invisible initially
//            logoutButton.setVisibility(View.INVISIBLE);             // set logout button invisible
//        } else {
//            subText.setText("Welcome"); //modified to subtext
//            usernameText.setText(extras.getString("USERNAME")); // this will come from LoginActivity
//            loginButton.setVisibility(View.INVISIBLE);              // set login button invisible
//            signupButton.setVisibility(View.INVISIBLE);             // set signup button invisible
//        }



        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(EntryActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(EntryActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        /* click listener on logout button pressed - ADDED */
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when continue as guest button is pressed, use intent to switch to ProfileActivity without pass a user as an extra*/
                Intent intent = new Intent(EntryActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
