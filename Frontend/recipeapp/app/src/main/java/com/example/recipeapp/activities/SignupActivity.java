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
 * Activity for client to create an account
 *
 * @author Ryan McFadden
 */
public class SignupActivity extends AppCompatActivity {
    /** EditText for client to set their username */
    private EditText usernameEditText;
    /** EditText for client to set their email address */
    private EditText emailEditText;
    /** EditText for client to set their password */
    private EditText passwordEditText;
    /** EditText for client to confirm their password */
    private EditText confirmEditText;
    /** Button to bring client back to EntryActivity */
    private Button entryButton;
    /** Button to make Volley request to attempt to create account */
    private Button signupButton;
    /** URL for signup Volley POST request */
    private static final String URL_SIGNUP = "http://coms-309-018.class.las.iastate.edu:8080/newuser";
    /** JSONObject to store signup info for POST request */
    private JSONObject user;

    /**
     * onCreate for SignupActivity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /* initialize UI elements */
        usernameEditText = findViewById(R.id.signup_username_edt);  // link to username edittext in the Signup activity XML
        passwordEditText = findViewById(R.id.signup_password_edt);  // link to password edittext in the Signup activity XML
        emailEditText = findViewById(R.id.signup_email_edt);        // link to password edittext in the Signup activity XML
        confirmEditText = findViewById(R.id.signup_confirm_edt);    // link to confirm edittext in the Signup activity XML
        entryButton = findViewById(R.id.signup_entry_btn);          // link to return to login button in the Signup activity XML
        signupButton = findViewById(R.id.signup_signup_btn);        // link to signup button in the Signup activity XML

        /* click listener on login button pressed */
        entryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* when entry button is pressed, use intent to switch to EntryActivity */
                Intent intent = new Intent(SignupActivity.this, EntryActivity.class);
                startActivity(intent);  // go to EntryActivity
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm = confirmEditText.getText().toString();

                /* if passwords match and meet requirements, make volley signup request */
                if (password.equals(confirm)){
                    if(isValidPassword(password)) {
                        Toast.makeText(getApplicationContext(), "Signing up", Toast.LENGTH_LONG).show();
                        user = new JSONObject();
                        try {
                            user.put("username", username);
                            user.put("emailAddress", email);
                            user.put("password", password);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        makeSignupRequest();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Password must be 8-15 characters long & contain a number or special character", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Helper function that checks whether a string contains at least one number or special character
     * Used in isValidPassword()
     * @param s - string to be checked
     * @return true if string s contains a number or special character, false otherwise
     */
    private Boolean containsNumberOrSpecial(String s){
        char c;
        for(int i = 0; i < s.length(); ++i){
            c = s.charAt(i);
            if(Character.isDigit(c)){
                return true;
            } else if (!Character.isDigit(c) &&  !Character.isLetter(c) && !Character.isWhitespace(c)){
                return true;
            }
        }
        return false;
    }

    /**
     * Helper function that checks whether a string is a valid password
     * Requirements for a valid password include being 8-15 characters in length and
     * containing at least one special character or number
     * @param s - string to be checked
     * @return true if string s meets password requirements, false otherwise
     */
    private Boolean isValidPassword(String s){
        if(s.length() > 7 && s.length() < 16 && containsNumberOrSpecial(s)){
            return true;
        } else {
            return false;
        }
    }


    /**
     * Volley POST request to attempt to create a new account with the user information
     * given by the client
     */
    private void makeSignupRequest() {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL_SIGNUP,
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Signup received Volley response", Toast.LENGTH_LONG).show();

                        /* if backend returns null, it means the user already exists in the DB */
                        if(response == null){
                            Toast.makeText(getApplicationContext(), "User already exists. Try Login!", Toast.LENGTH_LONG).show();
                        }
                        /* user does not yet exist in DB the new user is returned by backend, start profile activity with user info */
                        else {
                            // getting the data which is stored in shared preferences.
                            SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);
                            //make editor for sharedPreferences
                            SharedPreferences.Editor editor = saved_values.edit();

                            // put values into sharedPreferences
                            try {
                                editor.putInt(getString(R.string.USERID_KEY), response.getInt("id"));
                                editor.putString(getString(R.string.USERNAME_KEY), response.getString("username"));
                                editor.putString(getString(R.string.EMAIL_KEY), response.getString("emailAddress"));
                                editor.putString(getString(R.string.PASSWORD_KEY), response.getString("password"));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            // to save our new key-value data
                            editor.apply();
                            // go to ProfileActivity
                            startActivity(new Intent(SignupActivity.this, ProfileActivity.class));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Signup unsuccessful (VolleyError)", Toast.LENGTH_LONG).show();
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
                return params;
            }
        };

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}