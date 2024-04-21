package com.example.recipeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeapp.R;

public class SplashActivity extends AppCompatActivity {

    /** constant key for shared preferences */
    public static final String SHARED_PREFS = "shared_prefs";
    /** constant key for storing user's userId */
    public static final String USERID_KEY = "userid_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        Thread thread = new Thread() {
            @Override
            public void run() {
                try{
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    SharedPreferences saved_values = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                    if(saved_values.getInt(USERID_KEY, -1) < 0){
                        //user is not logged in, send to entry activity
                        startActivity(new Intent(SplashActivity.this, EntryActivity.class));
                    } else {
                        //user is logged in, send them to their profile page
                        startActivity(new Intent(SplashActivity.this, MyProfileActivity.class));
                    }
                }
            }
        };
        thread.start();
    }
}