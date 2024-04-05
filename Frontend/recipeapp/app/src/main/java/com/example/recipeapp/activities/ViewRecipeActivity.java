package com.example.recipeapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.recipeapp.R;
import com.example.recipeapp.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity to view a single recipe in its entirety
 *
 * TODO - Update to display the rest of the recipe's data
 */
public class ViewRecipeActivity extends AppCompatActivity {
    /** TextView to display the title of the recipe */
    private TextView titleTxt;
    /** TextView to display the author of the recipe */
    private TextView authorTxt;
    /** TextView to display the description of the recipe */
    private TextView descriptionTxt;
    /** TextView to display the rating of the recipe*/
    private TextView ratingTxt;
    /** TextView to display the instructions of the recipe*/
    private TextView instructionsTxt;
    /** TextView to display the ingredients of the recipe*/
    private TextView ingredientsTxt;
    /** JSONObject to store the full recipe */
    private JSONObject fullRecipeJSON;
    /** recipe's id */
    private int recipeId;

    /** Button for giving recipe a 1 star rating - TEMPORARY */
    private Button rate1;
    /** Button for giving recipe a 2 star rating - TEMPORARY */
    private Button rate2;
    /** Button for giving recipe a 3 star rating - TEMPORARY */
    private Button rate3;
    /** Button for giving recipe a 4 star rating - TEMPORARY */
    private Button rate4;
    /** Button for giving recipe a 5 star rating - TEMPORARY */
    private Button rate5;
    /** BASE URL for recipe rating requests */
    private String BASE_URL_RECIPES = "http://coms-309-018.class.las.iastate.edu:8080/recipes";
    /** Specific URL for recipe rating requests */
    private String SPECIFIC_URL_RATING;

    /**
     * onCreate for ViewRecipeActivity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        titleTxt = findViewById(R.id.titleText);
        authorTxt = findViewById(R.id.authorText);
        descriptionTxt = findViewById(R.id.descriptionText);
        try {
            //get full JSON from intent
            fullRecipeJSON = new JSONObject(getIntent().getStringExtra("RecipeJsonAsString"));
            //fill text with data from JSON recipe
            titleTxt.setText(fullRecipeJSON.getString("title"));
            authorTxt.setText(fullRecipeJSON.getString("author"));
//            ratingTxt.setText(fullRecipeJSON.getString("rating"));
            instructionsTxt.setText(fullRecipeJSON.getString("instructions"));
            ingredientsTxt.setText(fullRecipeJSON.getString("ingredients"));
        } catch (JSONException e) {
//        try {
//            //get full JSON from intent
//            fullRecipeJSON = new JSONObject(getIntent().getStringExtra("RecipeJsonAsString"));
//            //fill text with data from JSON recipe
//            titleTxt.setText(fullRecipeJSON.getString("title"));
//            authorTxt.setText(fullRecipeJSON.getString("author"));
//            descriptionTxt.setText(fullRecipeJSON.getString("description"));
//        } catch (JSONException e) {
//
//            throw new RuntimeException(e);
//        }

        rate1 = findViewById(R.id.rate1_button);
        rate2 = findViewById(R.id.rate2_button);
        rate3 = findViewById(R.id.rate3_button);
        rate4 = findViewById(R.id.rate4_button);
        rate5 = findViewById(R.id.rate5_button);

        //get recipeId from previous activity
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            recipeId = extras.getInt("recipeId");
        }


        /* click listener on rate1 button pressed */
        rate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when rate button is pressed, use Volley request to rate the recipe at 1 star */
                SPECIFIC_URL_RATING = BASE_URL_RECIPES + "/" + recipeId + "/rate/" + 1;
                putRecipeRating();
            }
        });
        /* click listener on rate2 button pressed */
        rate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when rate button is pressed, use Volley request to rate the recipe at 2 stars */
                SPECIFIC_URL_RATING = BASE_URL_RECIPES + "/" + recipeId + "/rate/" + 2;
                putRecipeRating();
            }
        });
        /* click listener on rate3 button pressed */
        rate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when rate button is pressed, use Volley request to rate the recipe at 3 stars */
                SPECIFIC_URL_RATING = BASE_URL_RECIPES + "/" + recipeId + "/rate/" + 3;
                putRecipeRating();
            }
        });
        /* click listener on rate4 button pressed */
        rate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when rate button is pressed, use Volley request to rate the recipe at 4 stars */
                SPECIFIC_URL_RATING = BASE_URL_RECIPES + "/" + recipeId + "/rate/" + 4;
                putRecipeRating();
            }
        });
        /* click listener on rate5 button pressed */
        rate5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when rate button is pressed, use Volley request to rate the recipe at 5 stars */
                SPECIFIC_URL_RATING = BASE_URL_RECIPES + "/" + recipeId + "/rate/" + 5;
                putRecipeRating();
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
}
