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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.recipeapp.R;
import com.example.recipeapp.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

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


    /** Local user's userId */
    private int userId;
    /** Local user's username */
    private String username;
    /** Local user's emailAddress */
    private String emailAddress;
    /** Local user's password */
    private String password;


    /** Base URL for user Volley requests with server */
    private String URL_USERS = "http://coms-309-018.class.las.iastate.edu:8080/users";
    /** Specific URL for local user's Volley requests with server */
    private String URL_THIS_USER;


    /** constant key for shared preferences */
    public static final String SHARED_PREFS = "shared_prefs";
    /** constant key for storing user's userId */
    public static final String USERID_KEY = "userid_key";
    /** constant key for storing user's userId */
    public static final String USERNAME_KEY = "username_key";
    /** constant key for storing user's email */
    public static final String EMAIL_KEY = "email_key";
    /** constant key for storing user's password */
    public static final String PASSWORD_KEY = "password_key";
    /** variable for SharedPreferences */
    SharedPreferences sharedPreferences;

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
        SharedPreferences saved_values = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        //get data from shared preferences and store it into variables
//        userId = sharedPreferences.getInt(USERID_KEY, -1);
//        username = sharedPreferences.getString(USERNAME_KEY, null);
//        emailAddress = sharedPreferences.getString(EMAIL_KEY, null);
//        password = sharedPreferences.getString(PASSWORD_KEY, null);

        userId = saved_values.getInt(USERID_KEY, -1);
        username = saved_values.getString(USERNAME_KEY, null);
        emailAddress = saved_values.getString(EMAIL_KEY, null);
        password = saved_values.getString(PASSWORD_KEY, null);


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
        } else { //user is not signed in
            usernameText.setVisibility(View.INVISIBLE);
            descriptionText.setVisibility(View.INVISIBLE);
            guestText.setText("You're not signed in!");
        }
//        }

//        Intent createIntent = getIntent();
//        /* if intent has id and it is not null or the default value of -1, show user's profile page. If not, user is not signed in*/
//        if(createIntent.hasExtra("id") && createIntent.getIntExtra("id", -1) > 0){
//
//            /* set userId and add to url for GET request, make GET request (fills in user's profile page) */
//            userId = createIntent.getIntExtra("id", -1);
//            URL_THIS_USER = URL_USERS + "/" + userId;
//            getUserInfoReq();
//
//            guestText.setVisibility(View.INVISIBLE);
//            entryButton.setVisibility(View.INVISIBLE);
//
//            /* options toolbar at top */
//            Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
//            setSupportActionBar(toolbar);
//            if (getSupportActionBar() != null) {
//                getSupportActionBar().setTitle("My Profile");
//            }
//            toolbar.inflateMenu(R.menu.profile_menu);
//        } else {
//            usernameText.setVisibility(View.INVISIBLE);
//            descriptionText.setVisibility(View.INVISIBLE);
//            guestText.setText("You're not signed in!");
//        }

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
        if(itemId == R.id.profile_options_create){
            //go to create recipe activity
            Intent intent = new Intent(getApplicationContext(), CreateRecipeActivity.class);
            intent.putExtra("id", userId);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.profile_options_logout){
            /* when logout button is pressed, use intent to switch to Entry Activity without sending any extras (logging user out) */
            Intent intent = new Intent(ProfileActivity.this, EntryActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.profile_options_edit){
            /* when edit profile button is pressed, use intent to switch to Edit Profile Activity without sending any extras (logging user out) */
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("id", userId);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.profile_options_delete) {
            //TODO - add password protection
            deleteUserReq();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Volley GET request to get the local user's profile information
     */
    private void getUserInfoReq() {
        JsonObjectRequest userReq = new JsonObjectRequest(Request.Method.GET,
                URL_THIS_USER,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
//                        Toast.makeText(getApplicationContext(), "Volley Received Response", Toast.LENGTH_LONG).show();

                        String usernameResponse = "testusername";
                        String emailResponse = "testemail";
                        try{
                            usernameResponse = response.getString("username");
                            emailResponse = response.getString("emailAddress");
                        }catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        usernameText.setText(usernameResponse);
                        descriptionText.setText(emailResponse);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Volley Error Response", Toast.LENGTH_LONG).show();
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
//                params.put("username", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

//        Toast.makeText(getApplicationContext(), "Adding request to Volley Queue", Toast.LENGTH_LONG).show();
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(userReq);
    }

    /**
     * Volley DELETE request to delete local user's account
     *
     */
    private void deleteUserReq() {
        JsonObjectRequest userReq = new JsonObjectRequest(Request.Method.DELETE,
                URL_THIS_USER,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        Toast.makeText(getApplicationContext(), "Volley Received Response, deleting account", Toast.LENGTH_LONG).show();

                        /* when account is deleted, use intent to switch to Entry Activity without sending any extras (logging user out) */
                        Intent intent = new Intent(ProfileActivity.this, EntryActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Volley Error Response", Toast.LENGTH_LONG).show();
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
//                params.put("username", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

//        Toast.makeText(getApplicationContext(), "Adding request to Volley Queue", Toast.LENGTH_LONG).show();
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(userReq);
    }
}