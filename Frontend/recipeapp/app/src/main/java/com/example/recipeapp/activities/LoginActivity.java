package com.example.recipeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
 * Activity for client to login to their account
 * @author Ryan McFadden
 */
public class LoginActivity extends AppCompatActivity {
    /** EditText for client to type in their username */
    private EditText usernameEditText;
    /** EditText for client to type in their password */
    private EditText passwordEditText;
    /** Button for client to attempt to login */
    private Button loginButton;
    /** Button for client to return to EntryActivity */
    private Button entryButton;
    /** URL for login Volley POST request */
    private static final String URL_LOGIN = "http://coms-309-018.class.las.iastate.edu:8080/login";     //define server URL for login
    /** JSONObject to store data for POST request */
    private JSONObject user;

    /** constant key for shared preferences */
    public static final String SHARED_PREFS = "shared_prefs";
    /** constant key for storing user's userId */
    public static final String USERID_KEY = "userid_key";
    /** constant key for storing user's userId */
    public static final String USERNAME_KEY = "username_key";
    /** constant key for storing user's email */
    public static final String EMAIL_KEY = "email_key";
    /** constant key for storing user's password */
    public static final String PASSWORD_KEY = "password_key";

    /**
     * onCreate method for LoginActivity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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
                try {
                    user.put("username", username);
                    user.put("email", null);
                    user.put("password", password);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(getApplicationContext(), "Logging in", Toast.LENGTH_LONG).show();
                makeLoginRequest();
            }
        });

        /* click listener on entry button pressed */
        entryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when entry button is pressed, use intent to switch to Entry Activity */
                startActivity(new Intent(LoginActivity.this, EntryActivity.class));
            }
        });
    }

    /**
     * Volley POST request to attempt to login
     */
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
                                // getting the data which is stored in shared preferences.
                                SharedPreferences saved_values = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                                //make editor for sharedPreferences
                                SharedPreferences.Editor editor = saved_values.edit();
                                // put values into sharedPreferences
                                editor.putInt(getString(R.string.USERID_KEY), response.getInt("id"));
                                editor.putString(getString(R.string.USERNAME_KEY), response.getString("username"));
                                editor.putString(getString(R.string.EMAIL_KEY), response.getString("emailAddress"));
                                editor.putString(getString(R.string.PASSWORD_KEY), response.getString("password"));
                                // to save our new key-value data
                                editor.apply();
                                // go to ProfileActivity
                                startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Login unsuccessful (VolleyError or Nonexistent User)", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
