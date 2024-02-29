package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity {

    private TextView headerText;
    private TextView guestText;
    private TextView usernameText;

    private TextView descriptionText;

    private Button logoutButton;
    private Button entryButton;
    private String username;
    private int userId;
    private String URL_GET_USER_BASE = "http://coms-309-018.class.las.iastate.edu:8080/users";
    private String URL_GET_USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        headerText = findViewById(R.id.profile_header_txt);
        usernameText = findViewById(R.id.profile_username_txt);
        guestText = findViewById(R.id.profile_guest_txt);
        descriptionText = findViewById(R.id.profile_description_txt);
        logoutButton = findViewById(R.id.profile_logout_btn);
        entryButton = findViewById(R.id.profile_entry_btn);

        Intent createIntent = getIntent();
        /* if intent has id and it is not null or the default value of -1, show user's profile page. If not, user is not signed in*/
        if(createIntent.hasExtra("id") && createIntent.getIntExtra("id", -1) > 0){

            /* set userId and add to url for GET request, make GET request (fills in user's profile page) */
            userId = createIntent.getIntExtra("id", -1);
            URL_GET_USER = URL_GET_USER_BASE + "/" + userId;
            getUserInfoReq();

            guestText.setVisibility(View.INVISIBLE);
            entryButton.setVisibility(View.INVISIBLE);
        } else {
            usernameText.setText("No user signed in");
            guestText.setText("You're not signed in!");
            logoutButton.setVisibility(View.INVISIBLE);
        }

        /* click listener on logout button pressed */
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when logout button is pressed, use intent to switch to Entry Activity without sending any extras (logging user out) */
                Intent intent = new Intent(ProfileActivity.this, EntryActivity.class);
                startActivity(intent);
            }
        });

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

    private void getUserInfoReq() {
//        user = new JSONObject();
        JsonObjectRequest userReq = new JsonObjectRequest(Request.Method.GET,
                URL_GET_USER,
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
}