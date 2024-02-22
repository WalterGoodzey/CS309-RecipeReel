package com.example.recipeapp.ui.saved;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipeapp.databinding.FragmentSavedBinding;

//added
import com.example.recipeapp.ListAdapter;
import com.example.recipeapp.ListItemObject;
import com.example.recipeapp.R;
import android.content.Context;
import com.example.recipeapp.VolleySingleton;


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


public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;

    private ListAdapter adapter;
    private ListView listView;

    private static final String URL_JSON_ARRAY = "https://1ee86d94-b706-4d14-85a5-df75cbea2fcb.mock.pstmn.io/recipes";

    Context applicationContext;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SavedViewModel savedViewModel =
                new ViewModelProvider(this).get(SavedViewModel.class);

        binding = FragmentSavedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textSaved;
//        final TextView textViewTwo = binding.textSavedSecond;
//        savedViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        savedViewModel.getSubText().observe(getViewLifecycleOwner(), textViewTwo::setText);
//
        //list
        View contentView = inflater.inflate(R.layout.fragment_saved, container, false);
        listView = contentView.findViewById(R.id.listView);
//        Context fragmentContext = container.getContext();
        // Initialize the adapter with an empty list (data will be added later)
        updateApplicationContext();
        adapter = new ListAdapter(getApplicationContext(), new ArrayList<>());
        listView.setAdapter(adapter);

        makeRecipeListReq();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Making a recipe list request (json array request)
     * */
    private void makeRecipeListReq() {
        JsonArrayRequest recipeListReq = new JsonArrayRequest(
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
                                String author = jsonObject.getString("author");
                                String description = jsonObject.getString("description");

                                // Create a ListItemObject and add it to the adapter
                                ListItemObject item = new ListItemObject(title, author, description, jsonObject);
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

    public Context getApplicationContext() {
        return applicationContext;
    }

    public void updateApplicationContext() {
        this.applicationContext = getContext();
    }
}
