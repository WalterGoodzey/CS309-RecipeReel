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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.recipeapp.R;
import com.example.recipeapp.VolleySingleton;
import com.example.recipeapp.adapters.RecipeAdapter;
import com.example.recipeapp.objects.RecipeItemObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity to display another user's profile
 *
 * @author Ryan McFadden
 */
public class OtherProfileActivity extends AppCompatActivity {

    /** TextView to display local user's username */
    private TextView usernameText;
    /** TextView to display description message */
    private TextView descriptionText;
    /** RecipeAdapter for the ListView of RecipeItemObjects */
    private RecipeAdapter adapter;
    /** ListView to store list of RecipeItemObjects */
    private ListView listView;

    private int userId;
    /** User ID of the profile being view */
    private int viewingUserId;
    /** Profile's username */
    private String username;
    /** Profile's emailAddress */
    private String emailAddress;
    private String URL_SERVER = "http://coms-309-018.class.las.iastate.edu:8080/";

    private boolean isFollowing = false;

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
        setContentView(R.layout.activity_other_profile);

        SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);
        userId = saved_values.getInt(getString(R.string.USERID_KEY), -1);

        checkIfFollowing();

        usernameText = findViewById(R.id.profile_username_txt);
        descriptionText = findViewById(R.id.profile_description_txt);

        //get other username from intent
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            viewingUserId = extras.getInt("viewingUserId");
        }
        getUserInfoReq();

//        //get profile's info
//        //initializing our shared preferences
//        SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);
//
//        userId = saved_values.getInt(getString(R.string.USERID_KEY), -1);
//        username = saved_values.getString(getString(R.string.USERNAME_KEY), null);
//        emailAddress = saved_values.getString(getString(R.string.EMAIL_KEY), null);

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
                Intent intent = new Intent(OtherProfileActivity.this, ViewRecipeActivity.class);
                intent.putExtra("id", adapter.getItem(i).getRecipeId());
                //start ViewRecipeActivity
                startActivity(intent);
            }
        });

//        usernameText.setText(username);
//        descriptionText.setText(emailAddress);

//        /* options toolbar at top */
//        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(username + "'s Profile");
//        }
//        toolbar.inflateMenu(R.menu.other_profile_menu);

        /* GET the profile's recipes */
        makeRecipeListReq();
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
        inflater.inflate(R.menu.other_profile_menu, menu);
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
        if (itemId == R.id.profile_options_follow) {
            checkIfFollowing();
            if (isFollowing) {
                unfollow();
            } else {
                follow();
            }
            return true;
        } else if (itemId == R.id.profile_options_chat) {
            /* go to a new chat with the other user */
            //intent to selected chatroom
            Intent intent = new Intent(OtherProfileActivity.this, ChatActivity.class);
            //send other chat users username as an extra in intent
            intent.putExtra("otherChatUser", username);
            //start ChatActivity
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Volley GET request to get the user's profile information
     */
    private void getUserInfoReq() {
        JsonObjectRequest userReq = new JsonObjectRequest(Request.Method.GET,
                URL_SERVER + "users/" + viewingUserId,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
//                        Toast.makeText(getApplicationContext(), "Volley Received Response", Toast.LENGTH_LONG).show();

                        String emailResponse = "testemail";
                        try{
                            username = response.getString("username");
                            emailResponse = response.getString("emailAddress");
                        }catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        usernameText.setText(username);
                        descriptionText.setText(emailResponse);
                        /* options toolbar at top */
                        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
                        setSupportActionBar(toolbar);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle(username + "'s Profile");
                        }
                        toolbar.inflateMenu(R.menu.other_profile_menu);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), "Volley Error Response", Toast.LENGTH_LONG).show();
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
     * Volley GET request to get a list of recipes, in this case used to get local user's created recipes
     */
    private void makeRecipeListReq() {
        JsonArrayRequest recipeListReq = new JsonArrayRequest(
                Request.Method.GET,
                URL_SERVER + "users/" + viewingUserId + "/recipes",
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        // Parse the JSON array and add data to the adapter
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int recipeId = jsonObject.getInt("id");
                                String title = jsonObject.getString("title");
                                String author = jsonObject.getString("username");
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

    private void follow() {
            JsonObjectRequest userReq = new JsonObjectRequest(
                    Request.Method.POST,
                    URL_SERVER + "users/" + userId + "/following/" + viewingUserId,
                    null, // Pass null as the request body since it's a GET request
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Volley Response", response.toString());
                            Toast.makeText(getApplicationContext(), "Profile followed", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Following Unsuccessful (Volley Error)", Toast.LENGTH_LONG).show();
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

    private void unfollow() {
        JsonObjectRequest userReq = new JsonObjectRequest(
                Request.Method.DELETE,
                URL_SERVER + "users/" + userId + "/following/" + viewingUserId,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        Toast.makeText(getApplicationContext(), "Profile unfollowed", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Unfollowing Unsuccessful (Volley Error)", Toast.LENGTH_LONG).show();
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

    private void checkIfFollowing() {
        JsonArrayRequest followingReq = new JsonArrayRequest(
                Request.Method.GET,
                URL_SERVER + "users/" + userId + "/following",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject profile = response.getJSONObject(i);
                                int followedUserId = profile.getInt("id");
                                if (followedUserId == viewingUserId) {
                                    isFollowing = true;
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // User is not following the target profile
                        // Update your UI accordingly
                        // e.g., updateFollowButtonAppearance(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(followingReq);
    }


}