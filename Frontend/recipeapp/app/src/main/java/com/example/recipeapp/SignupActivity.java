package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/**
 * @author Ryan McFadden
 */
public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText emailEditText;     // define email edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private EditText confirmEditText;   // define confirm edittext variable
    private Button entryButton;         // define back to entry button variable
    private Button signupButton;        // define signup button variable

//    private static final String URL_SIGNUP = "https://1ee86d94-b706-4d14-85a5-df75cbea2fcb.mock.pstmn.io/post_test";
    private static final String URL_SIGNUP = "http://coms-309-018.class.las.iastate.edu:8080/newuser"; //define server URL for signup
    private JSONObject user;            //define user JSONObject to POST
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

                /* if passwords match, make volley signup request */
                if (password.equals(confirm)){
                    Toast.makeText(getApplicationContext(), "Signing up", Toast.LENGTH_LONG).show();
                    user = new JSONObject();
                    try {
                        user.put("username", username);
                        user.put("email", email);
                        user.put("password", password);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    makeSignupRequest();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Password don't match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

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
                            try {
                                Intent intent = new Intent(SignupActivity.this, ProfileActivity.class);
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
                try{
                    params.put("username", user.getString("username"));
                    params.put("email", user.getString("email"));
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