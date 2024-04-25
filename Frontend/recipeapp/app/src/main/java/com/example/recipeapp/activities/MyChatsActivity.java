package com.example.recipeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.recipeapp.R;
import com.example.recipeapp.VolleySingleton;
import com.example.recipeapp.adapters.ProfileAdapter;
import com.example.recipeapp.objects.ProfileItemObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyChatsActivity extends AppCompatActivity {
    /** Local user's userId */
    private int userId;
    /** Local user's username */
    private String username;
    /** ProfileAdapter for the ListView of ProfileItemObjects */
    private ProfileAdapter adapter;
    /** ListView to store list of ProfileItemObjects */
    private ListView listView;
    /** BASE URL for user Volley requests for user's active chats */
    private String BASE_URL_CHATROOMS = "http://coms-309-018.class.las.iastate.edu:8080/chatrooms/";
    /** Specific URL for user Volley requests for user's active chats */
    private String SPECIFIC_URL_CHATROOMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_chats);

        //get userId from shared preferences
        SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);
        userId = saved_values.getInt(getString(R.string.USERID_KEY), -1);
        username = saved_values.getString(getString(R.string.USERNAME_KEY), null);

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
//        //for example
//        for(int i = 0; i < 5; i++){
//            String name = "ExampleName" + i;
//            // Create a ProfileItemObject and add it to the adapter
//            ProfileItemObject item = new ProfileItemObject(name);
//            adapter.add(item);
//        }


        //Added to go to ChatActivity when an item in listview is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //intent to selected chatroom
                Intent intent = new Intent(MyChatsActivity.this, ChatActivity.class);
                //send other chat users username as an extra in intent
                intent.putExtra("otherChatUser", adapter.getItem(i).getUsername());
                //start ChatActivity
                startActivity(intent);
            }
        });

        SPECIFIC_URL_CHATROOMS = BASE_URL_CHATROOMS + username;
        getChatroomsReq();
    }

    /**
     * Volley GET request to get list of user's active chats
     */
    private void getChatroomsReq() {
        JsonArrayRequest profileListReq = new JsonArrayRequest(
                Request.Method.GET,
                SPECIFIC_URL_CHATROOMS,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        // Parse the JSON array and add data to the adapter
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                String username = response.getString(i);
                                // Create a ProfileItemObject and add it to the adapter
                                ProfileItemObject item = new ProfileItemObject(username);
                                adapter.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
//                params.put("param1", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(profileListReq);
    }
}