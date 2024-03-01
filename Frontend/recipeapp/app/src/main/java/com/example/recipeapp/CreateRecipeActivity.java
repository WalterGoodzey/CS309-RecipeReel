package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/**
    @author Walter Goodzey
 */

public class CreateRecipeActivity extends AppCompatActivity {

    private int userId;

    private Button button_post;
//    private Button button_image_upload;

    private EditText input_title, input_description,
            input_ingredients, input_instructions, input_tags;

    private TextView server_response;
    private static final String URL_JSON_OBJ =
            "http://coms-309-018.class.las.iastate.edu:8080/recipes";
//            "https://jsonplaceholder.typicode.com/users/1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userId = extras.getInt("id");
        }

        button_post = findViewById(R.id.button_post);
//        button_image_upload = findViewById(R.id.button_image_upload);

        input_title = findViewById(R.id.input_title);
        input_description = findViewById(R.id.input_description);
        input_ingredients = findViewById(R.id.input_ingredients);
        input_instructions = findViewById(R.id.input_steps);
        input_tags = findViewById(R.id.input_tags);
        server_response = findViewById(R.id.server_response);

//        button_image_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//
//            ;
//        });

        button_post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                postRecipe();
            }
        });
    }

    private void postRecipe() {
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
                Request.Method.POST,
                URL_JSON_OBJ,
                postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        server_response.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        server_response.setText(error.getMessage());
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
}
