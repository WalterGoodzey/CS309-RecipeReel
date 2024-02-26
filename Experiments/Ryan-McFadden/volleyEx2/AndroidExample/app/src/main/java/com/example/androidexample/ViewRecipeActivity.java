package com.example.androidexample;

import android.os.Bundle;
import android.view.View;

//added
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ViewRecipeActivity extends AppCompatActivity {

    private TextView titleTxt;
    private TextView authorTxt;
    private TextView descriptionTxt;


    private JSONObject fullRecipeJSON;

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
            descriptionTxt.setText(fullRecipeJSON.getString("description"));
        } catch (JSONException e) {

            throw new RuntimeException(e);
        }
    }
}
