package com.example.recipeapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Activity to display recent recipes published by users that the
 * local user follows
 */
public class FollowingActivity extends AppCompatActivity {
    /** Local user's userId */
    private int userId;

    /**
     * onCreate method for FollowingActivity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        //get username from previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userId = extras.getInt("id");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_following);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.bottom_trending) {
                Intent intent = new Intent(getApplicationContext(), TrendingActivity.class);
                intent.putExtra("id", userId);
                startActivity(intent);
                return true;
            } else if (id == R.id.bottom_following) {
                //current activity, do nothing
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