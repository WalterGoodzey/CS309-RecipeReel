package com.example.recipeapp.activities;

import android.content.Intent;
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


public class ProfileActivity extends AppCompatActivity {

    private TextView headerText;
    private TextView guestText;
    private TextView usernameText;
    private TextView descriptionText;
//    private Button logoutButton;
    private Button entryButton;
//    private Button editButton;
    private int userId;
    private String URL_USERS = "http://coms-309-018.class.las.iastate.edu:8080/users";
    private String URL_THIS_USER;

//     private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        headerText = findViewById(R.id.profile_header_txt);
        usernameText = findViewById(R.id.profile_username_txt);
        guestText = findViewById(R.id.profile_guest_txt);
        descriptionText = findViewById(R.id.profile_description_txt);
//        logoutButton = findViewById(R.id.profile_logout_btn);
        entryButton = findViewById(R.id.profile_entry_btn);
//        editButton = findViewById(R.id.profile_edit_btn);

        Intent createIntent = getIntent();
        /* if intent has id and it is not null or the default value of -1, show user's profile page. If not, user is not signed in*/
        if(createIntent.hasExtra("id") && createIntent.getIntExtra("id", -1) > 0){

            /* set userId and add to url for GET request, make GET request (fills in user's profile page) */
            userId = createIntent.getIntExtra("id", -1);
            URL_THIS_USER = URL_USERS + "/" + userId;
            getUserInfoReq();

            guestText.setVisibility(View.INVISIBLE);
            entryButton.setVisibility(View.INVISIBLE);

            /* options toolbar at top */
            Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("My Profile");
            }
            toolbar.inflateMenu(R.menu.profile_menu);
        } else {
            usernameText.setVisibility(View.INVISIBLE);
            descriptionText.setVisibility(View.INVISIBLE);
            guestText.setText("You're not signed in!");
//            logoutButton.setVisibility(View.INVISIBLE);
//            editButton.setVisibility(View.INVISIBLE);
        }



//        /* click listener on logout button pressed */
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /* when logout button is pressed, use intent to switch to Entry Activity without sending any extras (logging user out) */
//                Intent intent = new Intent(ProfileActivity.this, EntryActivity.class);
//                startActivity(intent);
//            }
//        });

        /* click listener on entry button pressed */
        entryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when entry button is pressed, use intent to switch to Entry Activity without sending any extras (logging user out) */
                Intent intent = new Intent(ProfileActivity.this, EntryActivity.class);
                startActivity(intent);
            }
        });

//        /* click listener on edit button pressed */
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /* when edit button is pressed, use intent to switch to Edit Profile Activity, sending id as an extra */
//                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
//                intent.putExtra("id", userId);
//                startActivity(intent);
//            }
//        });


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

    /* profile header bar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

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