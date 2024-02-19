package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

//added
import android.content.Intent;


public class JsonArrReqActivity extends AppCompatActivity {

    private Button btnJsonArrReq;
    private ListAdapter adapter;
    private ListView listView;

//    private static final String URL_JSON_ARRAY = "https://jsonplaceholder.typicode.com/users";
//    private static final String URL_JSON_ARRAY = "https://76996fed-8c69-4b5a-99a7-9b2ce56847d0.mock.pstmn.io/recipestest";
    private static final String URL_JSON_ARRAY = "https://1ee86d94-b706-4d14-85a5-df75cbea2fcb.mock.pstmn.io/recipes";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_arr_req);

        btnJsonArrReq = findViewById(R.id.btnJsonArr);
        listView = findViewById(R.id.listView);

        // Initialize the adapter with an empty list (data will be added later)
        adapter = new ListAdapter(this, new ArrayList<>());
        listView.setAdapter(adapter);

        btnJsonArrReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJsonArrayReq();
            }
        });

        //Added to go to activity_view_recipe when an item in listview is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //intent to view single recipe
                Intent intent = new Intent(JsonArrReqActivity.this, ViewRecipeActivity.class);
                //send full JSON recipe as a string to be used in recipe view Activity
                intent.putExtra("RecipeJsonAsString", adapter.getItem(i).getFullRecipe().toString());
                //start ViewRecipeActivity
                startActivity(intent);
            }
        });
    }

    /**
     * Making json array request
     * */
    private void makeJsonArrayReq() {
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(
                Request.Method.GET,
                URL_JSON_ARRAY,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());

                        // Parse the JSON array and add data to the adapter
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String title = jsonObject.getString("title");
//                                String email = jsonObject.getString("email");
                                String description = jsonObject.getString("description");

                                // Create a ListItemObject and add it to the adapter
                                ListItemObject item = new ListItemObject(title, description, jsonObject);
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrReq);
    }
}