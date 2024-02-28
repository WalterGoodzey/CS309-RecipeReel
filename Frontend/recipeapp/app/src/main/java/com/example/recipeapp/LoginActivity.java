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
    private Button entryButton;        // define back to entry button variable
    private static final String URL_LOGIN = "http://coms-309-018.class.las.iastate.edu:8080/login";     //define server URL for login
//    private static final String URL_LOGIN = "https://1ee86d94-b706-4d14-85a5-df75cbea2fcb.mock.pstmn.io/post_test";
    private JSONObject user;            //define user JSONObject to POST

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);            // link to Login activity XML

        /* initialize UI elements */
        usernameEditText = findViewById(R.id.login_username_edt);   // link to username editor in the Login activity XML
        passwordEditText = findViewById(R.id.login_password_edt);   // link to password editor in the Login activity XML
        loginButton = findViewById(R.id.login_login_btn);           // link to login button in the Login activity XML
        entryButton = findViewById(R.id.login_entry_btn);           // link to entry button in the Login activity XML


        user = new JSONObject();
        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                /* send a JSON object to server/backend to check if login info matches known user */
//                user = new JSONObject();
                try {
                    user.put("username", username);
                    user.put("email", null);
                    user.put("password", password);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Logging in", Toast.LENGTH_LONG).show();
                makeLoginRequest();

//                /* Test code to push to profile page without using Volley and DB */
//                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
//                intent.putExtra("USERNAME", username);  // key-value to pass to the ProfileActivity
//                intent.putExtra("PASSWORD", password);  // key-value to pass to the ProfileActivity
//                startActivity(intent);  // go to ProfileActivity with the key-value data
            }
        });

        /* click listener on entry button pressed */
        entryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when entry button is pressed, use intent to switch to Entry Activity */
                Intent intent = new Intent(LoginActivity.this, EntryActivity.class);
                startActivity(intent);  // go to EntryActivity
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
                try{
                    params.put("username", user.getString("username"));
//                    params.put("email", null); //TODO - Implement user inputting email on signup
                    params.put("password", user.getString("password"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
