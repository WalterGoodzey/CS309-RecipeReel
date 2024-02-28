package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {


    private TextView headerText;
    private TextView usernameText;

    private Button logoutButton;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        headerText = findViewById(R.id.profile_header_txt);
        usernameText = findViewById(R.id.profile_username_txt);
        logoutButton = findViewById(R.id.profile_logout_btn);

        //see if user is logged in and setup the screen accordingly
//        Bundle extras = getIntent().getExtras();
//        if(extras == null){
//            usernameText.setText("No user signed in");
//            logoutButton.setVisibility(View.INVISIBLE);
//        } else {
//            username = extras.getString("USERNAME");
//            usernameText.setText(username);
//        }
        Intent createIntent = getIntent();
        /* if intent's USERNAME is not null, show user's profile page. If not, user is not signed in*/
        if(createIntent.hasExtra("USERNAME") && createIntent.getStringExtra("USERNAME") != null){
            username = createIntent.getStringExtra("USERNAME");
            usernameText.setText(username);
        } else {
            usernameText.setText("No user signed in");
            logoutButton.setVisibility(View.INVISIBLE);
            //TODO - add login page here!
        }

        /* click listener on logout button pressed */
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when logout button is pressed, use intent to switch to Entry Activity without sending any extras (logging user out) */
                Intent intent = new Intent(ProfileActivity.this, EntryActivity.class);
                startActivity(intent);
            }
        });


        //bottom navigation bar setup and functionality
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.bottom_trending) {
                Intent intent = new Intent(getApplicationContext(), TrendingActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
                return true;
            } else if (id == R.id.bottom_following) {
                Intent intent = new Intent(getApplicationContext(), FollowingActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
                return true;
            } else if (id == R.id.bottom_search) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
                return true;
            } else if (id == R.id.bottom_saved) {
                Intent intent = new Intent(getApplicationContext(), SavedActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
                return true;
            } else if (id == R.id.bottom_profile) {
                //current activity, do nothing
                return true;
            }
            return false;
        });
    }
}