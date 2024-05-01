package com.example.recipeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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
 * Activity for a user to create a recipe
 *
 * @author Walter Goodzey
 */

public class EditRecipeActivity extends AppCompatActivity {
    /** Local user's userId */
    private int userId;
    /** Button to update recipe (initiates Volley request) */
    private Button button_update;
    /** Button to cancel the update of a recipe */
    private Button button_cancel;
//    private Button button_image_upload;
    /** EditText to input recipe's title */
    private EditText input_title;
    /** EditText to input recipe's description */
    private EditText input_description;
    /** EditText to input recipe's ingredients */
    private EditText input_ingredients;
    /** EditText to input recipe's instructions */
    private EditText input_instructions;
    /** EditText to input recipe's tags */
    private EditText input_tags;
    /** Base URL for making recipe JSONObject POST requests to the server */
    private static final String URL_SERVER =
            "http://coms-309-018.class.las.iastate.edu:8080/";
//            "https://jsonplaceholder.typicode.com/users/1";
    /**
     * recipe's id
     */
    private int recipeId;
    /**
     * JSONObject to store the full recipe
     */
    private JSONObject fullRecipeJSON;

    /**
     * onCreate method for CreateRecipeActivity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recipeId = extras.getInt("id");
        }

//        Bundle extras = getIntent().getExtras();
//        if(extras != null){
//            userId = extras.getInt("id");
//        }
        //get userId from shared preferences
        SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);
        userId = saved_values.getInt(getString(R.string.USERID_KEY), -1);


        button_update = findViewById(R.id.button_update);
//        button_cancel = findViewById(R.id.button_edit_back);
//        button_image_upload = findViewById(R.id.button_image_upload);

        input_title = findViewById(R.id.input_edit_title);
        input_description = findViewById(R.id.input_edit_description);
        input_ingredients = findViewById(R.id.input_edit_ingredients);
        input_instructions = findViewById(R.id.input_edit_steps);
        input_tags = findViewById(R.id.input_edit_tags);

//        button_image_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//
//            ;
//        });

//        getRecipe();
        button_update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateRecipe();
            }
        });

//        button_cancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(EditRecipeActivity.this, MyProfileActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    /**
     * Volley request to update a recipe to the server
     */
    private void updateRecipe() {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("title", input_title.getText().toString());
            postBody.put("description", input_description.getText().toString());
            postBody.put("ingredients", input_ingredients.getText().toString());
            postBody.put("instructions", input_instructions.getText().toString());
            postBody.put("tags", input_tags.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                URL_SERVER + "recipes/",
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Recipe Updated", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Recipe Update Unsuccessful (VolleyError)", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //                params.put("param1", "value1");
                //                params.put("param2", "value2");
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void getRecipe() {
        String url = URL_SERVER + "recipes/" + recipeId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d("GetRecipe", "Response: " + response.toString());
                    fullRecipeJSON = response;
                    updateUI(response);
                }, error -> Log.e("GetRecipe", "Error Response", error));

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void updateUI(JSONObject response) {
        try {
            input_title.setText(response.optString("title", "Title not found"));
            input_description.setText(response.optString("description", "Description not found"));
            input_ingredients.setText(response.optString("ingredients", "Ingredients not found"));
            input_instructions.setText(response.optString("instructions", "Instructions not found"));
            input_tags.setText(response.optString("tags", "Tags not found"));

        } catch (Exception e) {
            Log.e("UpdateUI", "Error parsing response", e);
        }
    }
}

