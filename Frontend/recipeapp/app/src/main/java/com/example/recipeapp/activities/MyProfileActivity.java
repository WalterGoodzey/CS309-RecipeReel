package com.example.recipeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
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
public class MyProfileActivity extends AppCompatActivity {
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
    /** ImageView for local user's profile picture */
    private ImageView profilePictureView;

    /** Local user's userId */
    private int userId;
    /** Local user's profile picture photoID */
    private long photoID;
    /** Local user's username */
    private String username;
    /** Local user's emailAddress */
    private String emailAddress;

    /** Base URL for Volley requests with server */
    private String URL_SERVER = "http://coms-309-018.class.las.iastate.edu:8080/";
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
        setContentView(R.layout.activity_my_profile);

        usernameText = findViewById(R.id.profile_username_txt);
        guestText = findViewById(R.id.profile_guest_txt);
        descriptionText = findViewById(R.id.profile_description_txt);
        entryButton = findViewById(R.id.profile_entry_btn);
        profilePictureView = findViewById(R.id.profile_image);

        //initializing our shared preferences
        SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);

        userId = saved_values.getInt(getString(R.string.USERID_KEY), -1);
        photoID = saved_values.getLong(getString(R.string.PHOTOID_KEY), -1L);
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
                Intent intent = new Intent(MyProfileActivity.this, ViewRecipeActivity.class);
                intent.putExtra("id", adapter.getItem(i).getRecipeId());
                //start EditRecipeActivity
                startActivity(intent);
            }
        });

        if(userId > 0) { //user is logged in
            guestText.setVisibility(View.INVISIBLE);
            entryButton.setVisibility(View.INVISIBLE);

            usernameText.setText(username);
            descriptionText.setText(emailAddress);

            //get their profile picture from the server and set it if they have one
            makeImageRequest();


            /* options toolbar at top */
            Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("My Profile");
            }
            toolbar.inflateMenu(R.menu.my_profile_menu);

            //update URL and get user's created recipes
            URL_GET_CREATED_ARRAY = URL_USERS + "/" + userId + "/recipes";

            makeRecipeListReq();



        } else { //user is not signed in
            usernameText.setVisibility(View.INVISIBLE);
            descriptionText.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);

            guestText.setText(R.string.profile_guest_text);
        }

        /* click listener on entry button pressed */
        entryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when entry button is pressed, use intent to switch to Entry Activity without sending any extras (logging user out) */
                Intent intent = new Intent(MyProfileActivity.this, EntryActivity.class);
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
        inflater.inflate(R.menu.my_profile_menu, menu);
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
            startActivity(new Intent(MyProfileActivity.this, EntryActivity.class));
            return true;
        } else if (itemId == R.id.profile_options_edit) { //Note: EditProfileActivity now includes the option to delete profile
            //go to password check
            startActivity(new Intent(MyProfileActivity.this, PasswordCheckActivity.class));
            return true;
        } else if (itemId == R.id.profile_options_myChats){
            //go to myChats activity
            startActivity(new Intent(MyProfileActivity.this, MyChatsActivity.class));
            return true;
        } else if (itemId == R.id.profile_options_helperchat) { //Chat with helper "admin" user (possibly temporary to test chat websocket, possible new feature)
            //go to chat, sending helper "admin" account username as an extra in intent
            Intent intent = new Intent(MyProfileActivity.this, ChatActivity.class);
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
                URL_SERVER + "users/" + userId + "/recipes",
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
     * Making image request
     * */
    private void makeImageRequest() {

        ImageRequest imageRequest = new ImageRequest(
                URL_SERVER + "image/" + photoID,
                //"http://sharding.org/outgoing/temp/testimg3.jpg", //for testing only!

                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView
                        profilePictureView.setImageBitmap(response);
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
                        //Display default image
                        profilePictureView.setImageDrawable(getDrawable(R.drawable.ic_launcher_foreground));
                    }
                }
        );

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
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