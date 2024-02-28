package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ryan McFadden
 */
public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable
    private static final String URL_LOGIN = "http://coms-309-018.class.las.iastate.edu:8080/login"; //define server URL for login

    private JSONObject user;            //define user JSONObject to POST
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);            // link to Login activity XML

        /* initialize UI elements */
        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);    // link to login button in the Login activity XML
        signupButton = findViewById(R.id.login_signup_btn);  // link to signup button in the Login activity XML

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                /* send a JSON object to server/backend to check if login info matches known user */
                user = new JSONObject();
                try {
                    user.put("username", username);
                    user.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Logging in", Toast.LENGTH_LONG).show();
                makeLoginRequest();


//                /* when login button is pressed, use intent to switch to Login Activity */
//                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
//                intent.putExtra("USERNAME", username);  // key-value to pass to the MainActivity
//                intent.putExtra("PASSWORD", password);  // key-value to pass to the MainActivity
//                startActivity(intent);  // go to MainActivity with the key-value data
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);  // go to SignupActivity
            }
        });
    }



    private void makeLoginRequest() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL_LOGIN,
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Login received Volley response", Toast.LENGTH_LONG).show();

                        /* if backend returns null, it means the user does not exist in the DB */
                        if(response == null){
                            Toast.makeText(getApplicationContext(), "User does not exist. Try Signup!", Toast.LENGTH_LONG).show();
                        }
                        /* user exist in DB and is returned by backend, start profile activity with user info */
                        else {
                            try {
                                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                intent.putExtra("USERNAME", response.getString("username"));  // key-value to pass to the ProfilActivity
                                startActivity(intent);  // go to ProfileActivity with the key-value data (the user's username)
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Login unsuccessful (VolleyError)", Toast.LENGTH_LONG).show();
                    }
                }
        ){
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

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
