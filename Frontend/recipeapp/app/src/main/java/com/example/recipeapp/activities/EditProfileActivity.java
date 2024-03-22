package com.example.recipeapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class EditProfileActivity extends AppCompatActivity {



    private TextView usernameText;
    private TextView emailText;
    private TextView passwordText;
    private EditText usernameEditText;  // define username edittext variable
    private EditText emailEditText;     // define email edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private EditText confirmEditText;   // define confirm password edittext variable
    private Button saveButton;
    private Button exitButton;
    private int userId;
    private String URL_EDIT_USER_BASE = "http://coms-309-018.class.las.iastate.edu:8080/users";
    private String URL_EDIT_USER;
    private JSONObject user;

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

        Intent createIntent = getIntent();
        /* set userId and add to url for GET request, make GET request (fills in user's profile page) */
        userId = createIntent.getIntExtra("id", -1);
        URL_EDIT_USER = URL_EDIT_USER_BASE + "/" + userId;
        getUserInfoReq();

        /*  click listener on save button   */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm = confirmEditText.getText().toString();

                /* if passwords match, make volley signup request */
                if (password.equals(confirm)){
                    Toast.makeText(getApplicationContext(), "Saving info", Toast.LENGTH_LONG).show();
                    user = new JSONObject();
                    try {
                        user.put("id", userId);
                        user.put("username", username);
                        user.put("emailAddress", email);
                        user.put("password", password);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    //make PUT request
                    putUserInfoReq();
                    //go to profile activity (with updated info)
                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    intent.putExtra("id", userId);  // key-value to pass to the ProfileActivity
                    startActivity(intent);  // go to ProfileActivity with the key-value data (the user's id)
                }
                else {
                    Toast.makeText(getApplicationContext(), "Password don't match", Toast.LENGTH_LONG).show();
                }
            }
        });



        /*  click listener on exit button   */
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intent.putExtra("id", userId);  // key-value to pass to the ProfileActivity
                startActivity(intent);  // go to ProfileActivity with the key-value data (the user's id)
            }
        });
    }


    private void putUserInfoReq() {
        JsonObjectRequest userReq = new JsonObjectRequest(Request.Method.PUT,
                URL_EDIT_USER,
                user, // request body for PUT request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
                        Toast.makeText(getApplicationContext(), "Volley Received Response", Toast.LENGTH_LONG).show();


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
                try{
                    params.put("username", user.getString("username"));
                    params.put("emailAddress", user.getString("email"));
                    params.put("password", user.getString("password"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

//        Toast.makeText(getApplicationContext(), "Adding request to Volley Queue", Toast.LENGTH_LONG).show();
        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(userReq);
    }

    private void getUserInfoReq() {
        JsonObjectRequest userReq = new JsonObjectRequest(Request.Method.GET,
                URL_EDIT_USER,
                null, // Pass null as the request body since it's a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
//                        Toast.makeText(getApplicationContext(), "Volley Received Response", Toast.LENGTH_LONG).show();

                        String usernameResponse = "testusername";
                        String emailResponse = "testemail";
                        String passwordResponse = "testpassword";
                        try{
                            usernameResponse = response.getString("username");
                            emailResponse = response.getString("emailAddress");
                            passwordResponse = response.getString("password");
                        }catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        usernameEditText.setText(usernameResponse);
                        emailEditText.setText(emailResponse);
                        passwordEditText.setText(passwordResponse);
                        confirmEditText.setText(passwordResponse);
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