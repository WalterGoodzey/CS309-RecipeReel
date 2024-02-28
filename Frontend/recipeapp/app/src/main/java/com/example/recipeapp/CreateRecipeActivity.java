package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CreateRecipeActivity extends AppCompatActivity {

    private Button button_post, button_image_upload;

    private static final String URL_JSON_OBJ = "TEMP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        button_post = findViewById(R.id.button_post);
        button_image_upload = findViewById(R.id.button_image_upload);

        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){};
        });

        button_image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){};
        });


    }
}
