package com.example.recipeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
 * Activity to display local user's profile
 *
 * @author Ryan McFadden
 */
public class ProfileActivity extends AppCompatActivity {
    /** TextView to display message if local user is not logged in */
    private TextView guestText;
    /** TextView to display local user's username */
    private TextView usernameText;
    /** TextView to display description message */
    private TextView descriptionText;
    /** Button to go to EntryActivity if local user is not logged in */
    private Button entryButton;
    /** RecipeAdapter for the ListView of RecipeItemObjects */
    private RecipeAdapter adapter;
    /** ListView to store list of RecipeItemObjects */
    private ListView listView;

    /** Local user's userId */
    private int userId;
    /** Local user's username */
    private String username;
    /** Local user's emailAddress */
    private String emailAddress;


    /** Base URL for user Volley requests with server */
    private String URL_USERS = "http://coms-309-018.class.las.iastate.edu:8080/users";
    /** Specific URL for local user's Volley requests with server */
    private String URL_GET_CREATED_ARRAY;




    /**
     * onCreate method for ProfileActivity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameText = findViewById(R.id.profile_username_txt);
        guestText = findViewById(R.id.profile_guest_txt);
        descriptionText = findViewById(R.id.profile_description_txt);
        entryButton = findViewById(R.id.profile_entry_btn);

        //initializing our shared preferences
        SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);

        userId = saved_values.getInt(getString(R.string.USERID_KEY), -1);
        username = saved_values.getString(getString(R.string.USERNAME_KEY), null);
        emailAddress = saved_values.getString(getString(R.string.EMAIL_KEY), null);

        //recipe list setup and operation
        listView = findViewById(R.id.profileListView);

        // Initialize the adapter with an empty list (data will be added later)
        adapter = new RecipeAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);

        //Added to go to ViewRecipeActivity when an item in listview is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //intent to view single recipe
                Intent intent = new Intent(ProfileActivity.this, ViewRecipeActivity.class);
                //send full JSON recipe as a string to be used in recipe view Activity
                intent.putExtra("recipeId", adapter.getItem(i).getRecipeId());
                //start ViewRecipeActivity
                startActivity(intent);
            }
        });

        if(userId > 0) { //user is logged in
            guestText.setVisibility(View.INVISIBLE);
            entryButton.setVisibility(View.INVISIBLE);

            usernameText.setText(username);
            descriptionText.setText(emailAddress);

            /* options toolbar at top */
            Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("My Profile");
            }
            toolbar.inflateMenu(R.menu.profile_menu);

            //update URL and get user's created recipes
            URL_GET_CREATED_ARRAY = URL_USERS + "/" + userId + "/recipes";
            makeRecipeListReq();

            //for example
            for(int i = 0; i < 5; i++){
                String title = "Example" + i;
                // Create a ListItemObject and add it to the adapter
                RecipeItemObject item = new RecipeItemObject(i, title, "author", "description", null);
                adapter.add(item);
            }

        } else { //user is not signed in
            usernameText.setVisibility(View.INVISIBLE);
            descriptionText.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);

            guestText.setText("You're not signed in!");
        }

        /* click listener on entry button pressed */
        entryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when entry button is pressed, use intent to switch to Entry Activity without sending any extras (logging user out) */
                Intent intent = new Intent(ProfileActivity.this, EntryActivity.class);
                startActivity(intent);
            }
        });

        //bottom navigation bar setup and functionality
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
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
                //current activity, do nothing
                return true;
            }
            return false;
        });
    }

    /**
     * onCreate for ProfileActivity's header toolbar
     *
     * @param menu The options menu in which you place your items.
     * @return true on successful create
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    /**
     * Handler for when an option from the toolbar is selected
     *
     * @param item The menu item that was selected.
     *
     * @return true if applicable option is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        int itemId = item.getItemId();
        if (itemId == R.id.profile_options_create) {
            //go to CreateRecipeActivity
            startActivity(new Intent(getApplicationContext(), CreateRecipeActivity.class));
            return true;
        } else if (itemId == R.id.profile_options_logout) {
            /* when logout button is pressed, clear sharedPreferences (logging user out) and use intent to switch to Entry Activity */
            // getting the data which is stored in shared preferences.
            SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);
            //make editor for shared preferences
            SharedPreferences.Editor editor = saved_values.edit();
            //clear and save shared preferences
            editor.clear();
            editor.apply();
            //go to EntryActivity
            startActivity(new Intent(ProfileActivity.this, EntryActivity.class));
            return true;
        } else if (itemId == R.id.profile_options_edit) { //Note: EditProfileActivity now includes the option to delete profile
            //go to password check
            startActivity(new Intent(ProfileActivity.this, PasswordCheckActivity.class));
            return true;
        } else if (itemId == R.id.profile_options_myChats){
            //go to myChats activity
            startActivity(new Intent(ProfileActivity.this, MyChatsActivity.class));
            return true;
        } else if (itemId == R.id.profile_options_helperchat) { //Chat with helper "admin" user (possibly temporary to test chat websocket, possible new feature)
            //go to chat, sending helper "admin" account username as an extra in intent
            Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
            intent.putExtra("otherChatUser", "HELPERCHAT"); //hard set to HELPERCHAT for now
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Volley GET request to get a list of recipes, in this case used to get local user's created recipes
     */
    private void makeRecipeListReq() {
        JsonArrayRequest recipeListReq = new JsonArrayRequest(
                Request.Method.GET,
                URL_GET_CREATED_ARRAY,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        // Parse the JSON array and add data to the adapter
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int recipeId = jsonObject.getInt("recipeId");
                                String title = jsonObject.getString("title");
                                String author = jsonObject.getString("author");
                                String description = jsonObject.getString("description");

                                // Create a ListItemObject and add it to the adapter
                                RecipeItemObject item = new RecipeItemObject(recipeId, title, author, description, jsonObject);
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(recipeListReq);
    }


//    /**
//     * Volley GET request to get the local user's profile information
//     */
//    private void getUserInfoReq() {
//        JsonObjectRequest userReq = new JsonObjectRequest(Request.Method.GET,
//                URL_THIS_USER,
//                null, // Pass null as the request body since it's a GET request
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("Volley Response", response.toString());
////                        Toast.makeText(getApplicationContext(), "Volley Received Response", Toast.LENGTH_LONG).show();
//
//                        String usernameResponse = "testusername";
//                        String emailResponse = "testemail";
//                        try{
//                            usernameResponse = response.getString("username");
//                            emailResponse = response.getString("emailAddress");
//                        }catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        usernameText.setText(usernameResponse);
//                        descriptionText.setText(emailResponse);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), "Volley Error Response", Toast.LENGTH_LONG).show();
//                        Log.e("Volley Error", error.toString());
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
////                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
////                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
////                params.put("username", "value1");
////                params.put("param2", "value2");
//                return params;
//            }
//        };
//
////        Toast.makeText(getApplicationContext(), "Adding request to Volley Queue", Toast.LENGTH_LONG).show();
//        // Adding request to request queue
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(userReq);
//    }


}