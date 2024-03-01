package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TrendingActivity extends AppCompatActivity {

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);

        //get username from previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userId = extras.getInt("id");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_trending);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.bottom_trending) {
                //current activity, do nothing
                return true;
            } else if (id == R.id.bottom_following) {
                Intent intent = new Intent(getApplicationContext(), FollowingActivity.class);
                intent.putExtra("id", userId);
                startActivity(intent);
                return true;
            } else if (id == R.id.bottom_search) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("id", userId);
                startActivity(intent);
                return true;
            } else if (id == R.id.bottom_saved) {
                Intent intent = new Intent(getApplicationContext(), SavedActivity.class);
                intent.putExtra("id", userId);
                startActivity(intent);
                return true;
            } else if (id == R.id.bottom_profile) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("id", userId);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}