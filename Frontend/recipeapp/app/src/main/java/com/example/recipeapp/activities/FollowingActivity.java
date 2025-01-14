package com.example.recipeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.ImageRequest;
import com.example.recipeapp.R;
import com.example.recipeapp.VolleySingleton;
import com.example.recipeapp.adapters.RecipeAdapter;
import com.example.recipeapp.objects.RecipeItemObject;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity to display recent recipes published by users that the
 * local user follows
 */
public class FollowingActivity extends AppCompatActivity {
    /** Local user's userId */
    private int userId;
    /** RecipeAdapter for the ListView of RecipeItemObjects */
    private RecipeAdapter adapter;
    /** ListView to store list of RecipeItemObjects */
    private ListView listView;

    /** Base URL for Volley requests with server */
    private String URL_SERVER = "http://coms-309-018.class.las.iastate.edu:8080/";

    /** photo bitmap of current item */
    private Bitmap itemPhotoBitmap;
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

        //get userId from shared preferences
        SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);
        userId = saved_values.getInt(getString(R.string.USERID_KEY), -1);

        /* options toolbar at top */
        Toolbar toolbar = (Toolbar) findViewById(R.id.following_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Following");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_following);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.bottom_trending) {
                Intent intent = new Intent(getApplicationContext(), TrendingActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.bottom_following) {
                //current activity, do nothing
                return true;
            } else if (id == R.id.bottom_search) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.bottom_saved) {
                Intent intent = new Intent(getApplicationContext(), SavedActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.bottom_profile) {
                Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        //recipe list setup and operation
        listView = findViewById(R.id.followingListView);

        // Initialize the adapter with an empty list (data will be added later)
        adapter = new RecipeAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);

        //Added to go to ViewRecipeActivity when an item in listview is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //intent to view single recipe
                Intent intent = new Intent(FollowingActivity.this, ViewRecipeActivity.class);
                //send full JSON recipe as a string to be used in recipe view Activity
                intent.putExtra("id", adapter.getItem(i).getRecipeId());
                //start ViewRecipeActivity
                startActivity(intent);
            }
        });

        makeRecipeListReq();
    }

    private void makeRecipeListReq() {
        JsonArrayRequest recipeListReq = new JsonArrayRequest(
                Request.Method.GET,
                URL_SERVER + "users/" + userId + "/following-recipes",
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        // Parse the JSON array and add data to the adapter using makeItemRequestAndAdd
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                makeItemRequestAndAdd(response.getJSONObject(i));
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(recipeListReq);
    }

    /**
     * Making image request for the current item
     * */
    private void makeItemRequestAndAdd(JSONObject recipeObj) {
        long photoID = -2L;
        try{
            photoID = recipeObj.getLong("photoID");
        } catch (JSONException e) {
//            throw new RuntimeException(e);
        }

        ImageRequest imageRequest = new ImageRequest(
                URL_SERVER + "image/" + photoID,
                //"http://sharding.org/outgoing/temp/testimg3.jpg", //for testing only!

                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        try {
                            int recipeId = recipeObj.getInt("id");
                            String title = recipeObj.getString("title");
                            String author = recipeObj.getString("username");
                            String description = recipeObj.getString("description");
                            // Create a ListItemObject and add it to the adapter
                            RecipeItemObject item = new RecipeItemObject(recipeId, title, author, description, recipeObj, response);
                            adapter.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Volley Error", error.toString());

                    }
                }
        );

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }


}