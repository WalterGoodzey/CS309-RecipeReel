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
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.recipeapp.R;
import com.example.recipeapp.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity to view a single recipe in its entirety
 * <p>
 */
public class ViewRecipeActivity extends AppCompatActivity {
    /**
     * TextView to display the author of the recipe
     */
    private TextView authorTxt;
    /**
     * TextView to display the description of the recipe
     */
    private TextView descriptionTxt;
    /**
     * TextView to display the rating of the recipe
     */
    private TextView ratingTxt;
    /**
     * TextView to display the instructions of the recipe
     */
    private TextView instructionsTxt;
    /**
     * TextView to display the ingredients of the recipe
     */
    private TextView ingredientsTxt;
    /**
     * JSONObject to store the full recipe
     */
    private JSONObject fullRecipeJSON;
    /**
     * recipe's id
     */
    private int recipeId;
    /**
     * Buttons for giving recipe a ratings as well as cancel rating process
     */
    private Button rate1, rate2, rate3, rate4, rate5, cancelRate;

    private String URL_SERVER = "http://coms-309-018.class.las.iastate.edu:8080/";
//    private String URL_SERVER = "https://ae827564-7ce7-4ae9-bb71-dd282e411c72.mock.pstmn.io/recipes";
    /**
     * Specific URL for recipe rating requests
     */
    private String SPECIFIC_URL_RATING;

    /**
     * Local user's userId
     */
    private int userId;
    /**
     * CardView to display popupCard
     */
    private CardView popupCard;
    /**
     * TextView to display rating prompt
     */
    private TextView popupRatingMessage;

    /**
     * onCreate for ViewRecipeActivity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        authorTxt = findViewById(R.id.authorText);
        ratingTxt = findViewById(R.id.ratingText);
        descriptionTxt = findViewById(R.id.descriptionText);
        ingredientsTxt = findViewById(R.id.ingredientsText);
        instructionsTxt = findViewById(R.id.instructionsText);
        popupCard = findViewById(R.id.rate_recipe_popup_card);
        popupRatingMessage = findViewById(R.id.view_rate_popup);
        //get recipeId from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recipeId = extras.getInt("id");
        }

        popupCard.setVisibility(View.INVISIBLE);
        popupRatingMessage.setVisibility(View.INVISIBLE);

        rate1 = findViewById(R.id.rate1_button);
        rate2 = findViewById(R.id.rate2_button);
        rate3 = findViewById(R.id.rate3_button);
        rate4 = findViewById(R.id.rate4_button);
        rate5 = findViewById(R.id.rate5_button);
        cancelRate = findViewById(R.id.cancel_rate);

        getRecipe();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_menu, menu);
        return true;
    }

    /**
     * Handler for when an option from the toolbar is selected
     *
     * @param item The menu item that was selected.
     * @return true if applicable option is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        int itemId = item.getItemId();
        if (itemId == R.id.view_options_save_recipe) {
            saveRecipe();
            return true;
        } else if (itemId == R.id.view_options_comments) {
            // TODO - implement new activity to view comments
            return true;
        } else if (itemId == R.id.view_options_rate) {
            rateRecipe();
            return true;
        } else if (itemId == R.id.view_options_view_author) {
            // TODO - implement view author
            return true;
        } else if (itemId == R.id.view_options_chat_with_author) {
            // TODO - implement chat with author
            return true;
            // need this for later viewing and making comments, updating recipe
//        } else if (itemId == R.id.profile_options_logout) {
//            /* when logout button is pressed, clear sharedPreferences (logging user out) and use intent to switch to Entry Activity */
//            // getting the data which is stored in shared preferences.
//            SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);
//            //make editor for shared preferences
//            SharedPreferences.Editor editor = saved_values.edit();
//            //clear and save shared preferences
//            editor.clear();
//            editor.apply();
//            //go to EntryActivity
////            startActivity(new Intent(ProfileActivity.this, EntryActivity.class));
//            return true;
//
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * TODO - add documentation for saveRecipe
     */
    private void saveRecipe() {
        String url = URL_SERVER + "users/";
        // TODO - implement save recipe
    }

    /**
     * TODO - add documentation for rate Recipe
     */
    private void rateRecipe() {
        popupCard.setVisibility(View.VISIBLE);
        popupRatingMessage.setVisibility(View.VISIBLE);
        /* click listener on rate1 button pressed */
        rate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when rate button is pressed, use Volley request to rate the recipe at 1 star */
                SPECIFIC_URL_RATING = URL_SERVER + "recipes/" + recipeId + "/rate/" + 1;
                popupCard.setVisibility(View.INVISIBLE);
                popupRatingMessage.setVisibility(View.INVISIBLE);
                putRecipeRating();
            }
        });
        /* click listener on rate2 button pressed */
        rate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when rate button is pressed, use Volley request to rate the recipe at 2 stars */
                SPECIFIC_URL_RATING = URL_SERVER + "recipes/" + recipeId + "/rate/" + 2;
                popupCard.setVisibility(View.INVISIBLE);
                popupRatingMessage.setVisibility(View.INVISIBLE);
                putRecipeRating();
            }
        });
        /* click listener on rate3 button pressed */
        rate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when rate button is pressed, use Volley request to rate the recipe at 3 stars */
                SPECIFIC_URL_RATING = URL_SERVER + "recipes/" + recipeId + "/rate/" + 3;
                popupCard.setVisibility(View.INVISIBLE);
                popupRatingMessage.setVisibility(View.INVISIBLE);
                putRecipeRating();
            }
        });
        /* click listener on rate4 button pressed */
        rate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when rate button is pressed, use Volley request to rate the recipe at 4 stars */
                SPECIFIC_URL_RATING = URL_SERVER + "recipes/" + recipeId + "/rate/" + 4;
                popupCard.setVisibility(View.INVISIBLE);
                popupRatingMessage.setVisibility(View.INVISIBLE);
                putRecipeRating();
            }
        });
        /* click listener on rate5 button pressed */
        rate5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when rate button is pressed, use Volley request to rate the recipe at 5 stars */
                SPECIFIC_URL_RATING = URL_SERVER + "recipes/" + recipeId + "/rate/" + 5;
                popupCard.setVisibility(View.INVISIBLE);
                popupRatingMessage.setVisibility(View.INVISIBLE);
                putRecipeRating();
            }
        });
        /* click listener on cancelRate button pressed */
        cancelRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupCard.setVisibility(View.INVISIBLE);
                popupRatingMessage.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Volley PUT request to PUT user's new recipe rating to the server
     */
    private void putRecipeRating() {
        JsonObjectRequest userReq = new JsonObjectRequest(Request.Method.PUT,
                SPECIFIC_URL_RATING,
                null, // request body for PUT request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        Toast.makeText(getApplicationContext(), "Rating sent!", Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Rating Unsuccessful (Volley Error)", Toast.LENGTH_LONG).show();
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
                return params;
            }
        };
//        Toast.makeText(getApplicationContext(), "Adding request to Volley Queue", Toast.LENGTH_LONG).show();
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(userReq);
    }

    /**
     * TODO - add documentation get Recipe
     */
    private void getRecipe() {
        String url = URL_SERVER + "recipes/" + recipeId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d("GetRecipe", "Response: " + response.toString());
                    updateUI(response);
                }, error -> Log.e("GetRecipe", "Error Response", error));

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * TODO - add documentation updateUI
     *
     * @param response JSONObject containing the information for a recipe
     */
    private void updateUI(JSONObject response) {
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.view_toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(response.optString("title",
                        "Title not found"));
            }
            toolbar.inflateMenu(R.menu.view_menu);


            authorTxt.setText("Author:\n" + response.optString("username", "Author not found"));
            ratingTxt.setText("Rating: " + response.optString("rating", "Rating not found"));
            instructionsTxt.setText("Instructions:\n" + response.optString("instructions", "Instructions not found"));
            ingredientsTxt.setText("Ingredients:\n" + response.optString("ingredients", "Ingredients not found"));
            descriptionTxt.setText("Description:\n" + response.optString("description", "Description not found"));
        } catch (Exception e) {
            Log.e("UpdateUI", "Error parsing response", e);
        }
    }
}