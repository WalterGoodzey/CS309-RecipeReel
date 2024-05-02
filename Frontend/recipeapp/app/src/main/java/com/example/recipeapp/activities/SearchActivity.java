package com.example.recipeapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.recipeapp.R;
import com.example.recipeapp.adapters.RecipeAdapter;
import com.example.recipeapp.objects.RecipeItemObject;
import com.example.recipeapp.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity for user to search for recipes by name or category
 */
public class SearchActivity extends AppCompatActivity {
    /**
     * RecipeAdapter for the ListView of RecipeItemObjects
     */
    private RecipeAdapter adapter;

    /**
     * Local user's userId
     */
    private int userId;
    /**
     * Button to send Search request
     */
    private Button searchButton;
    /**
     * ListView to store list of RecipeItemObjects
     */
    private ListView searchList;
    /**
     * EditText to store the searchString
     */
    private EditText searchString;
    /**
     * EditText to store the searchTags
     */
    private EditText searchTags;
    /**
     * Base URL for Volley Get request with Volley server
     */
    private static final String URL_SERVER = "http://coms-309-018.class.las.iastate.edu:8080/";

    /**
     * String for concat with server url
     */ //TODO - find a better way to word this
    /**
     * onCreate method for SearchActivity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        adapter = new RecipeAdapter(this, new ArrayList<>());

        searchButton = findViewById(R.id.search_button);
        searchString = findViewById(R.id.search_bar);
        searchTags = findViewById(R.id.search_tag);

        searchList = findViewById(R.id.search_results);
        searchList.setAdapter(adapter);

        //get username from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getInt("id");
        }

        /* options toolbar at top */
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Search");
        }

        /* click listener on search button pressed */
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchString.getText().toString();
                String searchTagsTerm = searchTags.getText().toString();

                // Build the search URL based on filled fields
                String searchUrl;
                if (!searchTerm.isEmpty() && !searchTagsTerm.isEmpty()) {
                    // Search by both name and tags
                    searchUrl = URL_SERVER + "recipes/search/both/" + searchTerm + "/" + searchTagsTerm;
                } else if (!searchTerm.isEmpty()) {
                    // Search by name
                    searchUrl = URL_SERVER + "recipes/search/string/" + searchTerm;
                } else if (!searchTagsTerm.isEmpty()) {
                    // Search by tags
                    searchUrl = URL_SERVER + "recipes/search/tag/" + searchTagsTerm;
                } else {
                    // Notify user to enter a search term
                    Toast.makeText(SearchActivity.this, "Please enter a search term", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Make the recipe list request
                makeRecipeListReq(searchUrl);
            }
        });


        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //intent to view single recipe
                Intent intent = new Intent(SearchActivity.this, ViewRecipeActivity.class);
//                //send full JSON recipe as a string to be used in recipe view Activity
//                intent.putExtra("RecipeJsonAsString", adapter.getItem(i).getFullRecipe().toString());
                intent.putExtra("id", adapter.getItem(i).getRecipeId());
                //start ViewRecipeActivity
                startActivity(intent);
            }
        });

        adapter = new RecipeAdapter(this, new ArrayList<>());
        searchList.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.bottom_trending) {
                Intent intent = new Intent(getApplicationContext(), TrendingActivity.class);
                intent.putExtra("id", userId);
                startActivity(intent);
                return true;
            } else if (id == R.id.bottom_following) {
                Intent intent = new Intent(getApplicationContext(), FollowingActivity.class);
                intent.putExtra("id", userId);
                startActivity(intent);
                return true;
            } else if (id == R.id.bottom_search) {
                //current activity, do nothing
                return true;
            } else if (id == R.id.bottom_saved) {
                Intent intent = new Intent(getApplicationContext(), SavedActivity.class);
                intent.putExtra("id", userId);
                startActivity(intent);
                return true;
            } else if (id == R.id.bottom_profile) {
                Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                intent.putExtra("id", userId);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void makeRecipeListReq(String searchURL) {
        // Clear existing data in adapter to ensure fresh results are displayed
        adapter.clear();

        JsonArrayRequest recipeListReq = new JsonArrayRequest(
                Request.Method.GET,
                searchURL,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        if (response.length() == 0) {
                            // No results found, notify user
                            Toast.makeText(SearchActivity.this, "No recipes found. Try a different search.", Toast.LENGTH_LONG).show();
                        }

                        // Parse the JSON array and add data to the adapter using makeItemRequestAndAdd
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                makeItemRequestAndAdd(response.getJSONObject(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Notify user that recipes have been loaded
                        if (response.length() > 0) {
                            Toast.makeText(SearchActivity.this, "Recipes loaded successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        // Notify user of the error
                        Toast.makeText(SearchActivity.this, "Failed to load recipes. Check your internet connection.", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // Add your headers here if needed
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // Add your parameters here if needed
                return params;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(recipeListReq);
    }


    /**
     * Making image request for the current item and adding the finished item to the adapter
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