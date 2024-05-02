package com.example.recipeapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeapp.R;

/**
 * Activity which allows client to choose to login to their account,
 * signup for an account, or continue as a guest user, and brings them to
 * the corresponding activity for their choice
 *
 * @author Ryan McFadden
 */
public class EntryActivity extends AppCompatActivity {
    /** TextView for main welcome message */
    private TextView messageText;
    /** TextView for sub message */
    private TextView subText;
    /** Button to bring client to LoginActivity */
    private Button loginButton;
    /** Button to bring client to SignupActivity */
    private Button signupButton;
    /** Button to bring client to ProfileActivity without logging them in */
    private Button guestButton;

    /**
     * onCreate method for EntryActivity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);             // link to entry activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.entry_msg_txt);      // link to message textview in the entry activity XML
        loginButton = findViewById(R.id.entry_login_btn);    // link to login button in the entry activity XML
        signupButton = findViewById(R.id.entry_signup_btn);  // link to signup button in the entry activity XML
        guestButton = findViewById(R.id.entry_guest_btn);  // link to logout button in the entry activity XML - ADDED
        subText = findViewById(R.id.entry_sub_msg_txt);      // link to sub message textview in the entry activity XML

        messageText.setText(R.string.entry_main_msg);
        subText.setText(R.string.entry_sub_msg);

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

        /* click listener on logout button pressed */
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when continue as guest button is pressed, use intent to switch to TrendingActivity without pass a user as an extra*/
                Intent intent = new Intent(EntryActivity.this, TrendingActivity.class);
                startActivity(intent);
            }
        });
    }
}
