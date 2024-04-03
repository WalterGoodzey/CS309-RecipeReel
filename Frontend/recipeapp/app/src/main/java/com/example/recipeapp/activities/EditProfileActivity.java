package com.example.recipeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
 * Activity for user to edit their profile information
 *
 * @author Ryan McFadden
 */
public class EditProfileActivity extends AppCompatActivity {
    /** TextView to display local user's username */
    private TextView usernameText;
    /** TextView to display local user's email address */
    private TextView emailText;
    /** TextView to display local user's password */
    private TextView passwordText;
    /** EditText for local user to edit their username */
    private EditText usernameEditText;
    /** EditText for local user to edit their email address */
    private EditText emailEditText;
    /** EditText for local user to edit their password */
    private EditText passwordEditText;
    /** EditText for local user to confirm their new password */
    private EditText confirmEditText;
    /** Button for local user to save their changes */
    private Button saveButton;
    /** Button for local user to exit EditProfileActivity and go to their ProfileActivity */
    private Button exitButton;
    /** Button for local user to delete their account and bring up confirmation CardView*/
    private Button deleteAccountButton;

    /** Cardview for confirm "popup window" */
    private CardView popupCard;
    /** Message on "popup" */
    private TextView popupMessage;
    /** Button for local user to confirm deleting their account and go to EntryActivity */
    private Button confirmDeleteAccountButton;
    /** Button for local user to cancel deleting their account and remove confirmation CardView */
    private Button cancelDeleteAccountButton;


    /** Local user's userId */
    private int userId;
    /** Local user's username */
    private String username;
    /** Local user's emailAddress */
    private String emailAddress;
    /** Local user's password */
    private String password;

    /** Base URL for Volley GET and PUT requests */
    private String URL_EDIT_BASE = "http://coms-309-018.class.las.iastate.edu:8080/users";
    /** URL for Volley GET and PUT requests for this specific local user */
    private String URL_EDIT_THIS_USER;
    /** JSONObject to store and send local user's profile information */
    private JSONObject user;
    /** Boolean to flag whether PUT request was successful*/
    private Boolean successfulPut;

    /**
     * onCreate method for EditProfileActivity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        /* initialize UI elements */
        usernameEditText = findViewById(R.id.editprofile_username_edt);     // link to username editor in the EditProfile activity XML
        emailEditText = findViewById(R.id.editprofile_email_edt);           // link to email editor in the EditProfile activity XML
        passwordEditText = findViewById(R.id.editprofile_password_edt);     // link to password editor in the EditProfile activity XML
        confirmEditText = findViewById(R.id.editprofile_confirm_edt);       // link to confirm password editor in the EditProfile activity XML
        saveButton = findViewById(R.id.editprofile_save_btn);               // link to save button in the EditProfile activity XML
        exitButton = findViewById(R.id.editprofile_exit_btn);               // link to exit button in the EditProfile activity XML
        deleteAccountButton = findViewById(R.id.editprofile_delete_btn);    // link to delete button in the EditProfile activity XML
        confirmDeleteAccountButton = findViewById(R.id.editprofile_popup_confirm_btn);      // link to confirm delete button in the EditProfile activity XML
        cancelDeleteAccountButton = findViewById(R.id.editprofile_popup_cancel_btn);        // link to cancel delete button in the EditProfile activity XML
        popupCard = findViewById(R.id.editprofile_popup_card);
        popupMessage = findViewById(R.id.editprofile_popup_message);

        //make popup window invisible
        popupCard.setVisibility(View.INVISIBLE);
        popupMessage.setVisibility(View.INVISIBLE);
        confirmDeleteAccountButton.setVisibility(View.INVISIBLE);
        cancelDeleteAccountButton.setVisibility(View.INVISIBLE);

        //get user info from shared preferences
        SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);
        userId = saved_values.getInt(getString(R.string.USERID_KEY), -1);
        username = saved_values.getString(getString(R.string.USERNAME_KEY), null);
        emailAddress = saved_values.getString(getString(R.string.EMAIL_KEY), null);
        password = saved_values.getString(getString(R.string.PASSWORD_KEY), null);


        usernameEditText.setText(username);
        emailEditText.setText(emailAddress);
        passwordEditText.setText(password);
        confirmEditText.setText(password);

        //setup URL for Volley requests
        URL_EDIT_THIS_USER = URL_EDIT_BASE + "/" + userId;

        /*  click listener on save button   */
        saveButton.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(getApplicationContext(), "Saving info", Toast.LENGTH_LONG).show();
                        user = new JSONObject();
                        try {
                            user.put("id", userId);
                            user.put("username", username);
                            user.put("emailAddress", email);
                            user.put("password", password);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //make PUT request
                        successfulPut = false;
                        putUserInfoReq();

                        //TODO - add check for successfulPUT that accounts for delayed Volley response
                        //On successful POST, update shared preferences to new values
                        SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);
                        //make editor for sharedPreferences
                        SharedPreferences.Editor editor = saved_values.edit();
                        // put values into sharedPreferences
                        editor.putString(getString(R.string.USERNAME_KEY), username);
                        editor.putString(getString(R.string.EMAIL_KEY), emailAddress);
                        editor.putString(getString(R.string.PASSWORD_KEY), password);
                        // save new key-value data
                        editor.apply();

                        //go to profile activity (with updated info)
                        startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Password must be 8-15 characters long and contain a number or special character", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
                }
            }
        });

        /* click listener on exit button */
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to ProfileActivity
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
            }
        });

        /* click listener on delete account button */
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make popup window visible
                popupCard.setVisibility(View.VISIBLE);
                popupMessage.setVisibility(View.VISIBLE);
                confirmDeleteAccountButton.setVisibility(View.VISIBLE);
                cancelDeleteAccountButton.setVisibility(View.VISIBLE);

                //disable other buttons
                saveButton.setEnabled(false);
                exitButton.setEnabled(false);
            }
        });

        /* click listener on confirm delete account button */
        confirmDeleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear and saved shared preferences
                SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = saved_values.edit();
                editor.clear();
                editor.apply();
                //send Volley delete request, will send user back to EntryActivity
                deleteUserReq();
            }
        });

        /* click listener on cancel delete account button */
        cancelDeleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make popup window invisible
                popupCard.setVisibility(View.INVISIBLE);
                popupMessage.setVisibility(View.INVISIBLE);
                confirmDeleteAccountButton.setVisibility(View.INVISIBLE);
                cancelDeleteAccountButton.setVisibility(View.INVISIBLE);

                //re-enable other buttons
                saveButton.setEnabled(false);
                exitButton.setEnabled(false);
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
     * Volley PUT request to PUT user's new profile information to the server
     */
    private void putUserInfoReq() {
        JsonObjectRequest userReq = new JsonObjectRequest(Request.Method.PUT,
                URL_EDIT_THIS_USER,
                user, // request body for PUT request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        successfulPut = true;
                        Log.d("Volley Response", response.toString());
//                        Toast.makeText(getApplicationContext(), "Volley Received Response", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        successfulPut = false;
                        Toast.makeText(getApplicationContext(), "Volley Error Response", Toast.LENGTH_LONG).show();
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
                return params;
            }
        };
//        Toast.makeText(getApplicationContext(), "Adding request to Volley Queue", Toast.LENGTH_LONG).show();
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(userReq);
    }

    /**
     * Volley DELETE request to delete local user's account
     *
     */
    private void deleteUserReq() {
        JsonObjectRequest userReq = new JsonObjectRequest(Request.Method.DELETE,
                URL_EDIT_THIS_USER,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
//                        Toast.makeText(getApplicationContext(), "Volley Received Response, deleting account", Toast.LENGTH_LONG).show();

                        /* when account is deleted, use intent to switch to Entry Activity */
                        Intent intent = new Intent(EditProfileActivity.this, EntryActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Volley Error Response", Toast.LENGTH_LONG).show();
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
//                params.put("username", "value1");
//                params.put("param2", "value2");
                return params;
            }
        };

//        Toast.makeText(getApplicationContext(), "Adding request to Volley Queue", Toast.LENGTH_LONG).show();
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(userReq);
    }
}