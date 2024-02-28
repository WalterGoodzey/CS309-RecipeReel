package com.example.androidexample;

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



public class PostActivity extends AppCompatActivity{

    private Button btnPostRequest;
    private TextView msgToSend, msgFromServer;

    private static final String URL_JSON_OBJECT = "https://1ee86d94-b706-4d14-85a5-df75cbea2fcb.mock.pstmn.io/post_test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        btnPostRequest = findViewById(R.id.btnPostRequest);
        msgToSend = findViewById(R.id.msgToSend);
        msgFromServer = findViewById(R.id.serverMsgRecieved);


        btnPostRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePostReq();
            }
        });
    }

    /**
     * Making json object request
     */
    private void makePostReq(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST,
                URL_JSON_OBJECT,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());

                        try {
                            msgFromServer.setText(response.getString("message"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
//                headers.put("Content-Type", "application/json");
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
//                 Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
    }
    }

//    private void makeJsonObjReq() {
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
//                Request.Method.GET,
//                URL_JSON_OBJECT,
//                null, // Pass null as the request body since it's a GET request
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("Volley Response", response.toString());
//                        try {
//                            // Parse JSON object data
//                            String name = response.getString("title");
//                            String email = response.getString("author");
//                            String phone = response.getString("description");
//
//                            // Populate text views with the parsed data
//                            tvName.setText(name);
//                            tvEmail.setText(email);
//                            tvPhone.setText(phone);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Volley Error", error.toString());
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
////                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
////                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("param1", "value1");
////                params.put("param2", "value2");
//                return params;
//            }
//        };
//
//        // Adding request to request queue
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjReq);
//    }
//}
