package com.example.recipeapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.recipeapp.R;
import com.example.recipeapp.adapters.ProfileAdapter;
import com.example.recipeapp.objects.ProfileItemObject;

import java.util.ArrayList;

public class MyChatsActivity extends AppCompatActivity {
    /** Local user's userId */
    private int userId;
    /** ProfileAdapter for the ListView of ProfileItemObjects */
    private ProfileAdapter adapter;
    /** ListView to store list of ProfileItemObjects */
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_chats);

        //get userId from shared preferences
        SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);
        userId = saved_values.getInt(getString(R.string.USERID_KEY), -1);

        /* options toolbar at top */
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_chats_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("MyChats");
        }

        //profile list setup and operation
        listView = findViewById(R.id.myChatsListView);

        // Initialize the adapter with an empty list (data will be added later)
        adapter = new ProfileAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);


        //for example
        for(int i = 0; i < 5; i++){
            String name = "ExampleName" + i;
            // Create a ProfileItemObject and add it to the adapter
            ProfileItemObject item = new ProfileItemObject(name);
            adapter.add(item);
        }
    }
}