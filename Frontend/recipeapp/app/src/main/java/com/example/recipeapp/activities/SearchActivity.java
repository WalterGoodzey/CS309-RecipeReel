package com.example.recipeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
     * Button to send Search request
     */
    private Button searchTagsButton;
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
    private static final String URL_SERVER = "http://coms-309-018.class.las.iastate.edu:8080";
//    private static final String URL_SERVER = ""; // for postman testing
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
        searchTagsButton = findViewById(R.id.tag_search_button);
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
                String searchUrl = URL_SERVER + "/recipes/search/string/" + searchTerm;
                makeRecipeListReq(searchUrl);
            }
        });


        /* click listener on search tags button pressed */
        searchTagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchTags.getText().toString();
                String searchUrl = URL_SERVER + "/recipes/search/tag/" + searchTerm;
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
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
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

                        // Parse the JSON array and add data to the adapter
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int recipeId = Integer.valueOf(jsonObject.getString("id"));
                                String title = jsonObject.getString("title");
                                String author = jsonObject.getString("username");
                                String description = jsonObject.getString("description");

                                // Create a RecipeItemObject and add it to the adapter
                                RecipeItemObject item = new RecipeItemObject(recipeId, title, author, description, jsonObject);
                                adapter.add(item);

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

}