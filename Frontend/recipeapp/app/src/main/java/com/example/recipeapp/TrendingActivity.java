package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TrendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_saved);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.bottom_trending) {
                //current activity, do nothing
                return true;
            } else if (id == R.id.bottom_following) {
                startActivity(new Intent(getApplicationContext(), FollowingActivity.class));
                return true;
            } else if (id == R.id.bottom_search) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                return true;
            } else if (id == R.id.bottom_saved) {
                startActivity(new Intent(getApplicationContext(), SavedActivity.class));
                return true;
            } else if (id == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
}