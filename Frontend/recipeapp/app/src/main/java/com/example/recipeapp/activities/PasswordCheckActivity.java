package com.example.recipeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeapp.R;

public class PasswordCheckActivity extends AppCompatActivity {

    /** TextView to display  message */
    private TextView messageText;
    /** EditText for user to input their password */
    private EditText inputPasswordEtx;
    /** Button to go to ProfileActivity */
    private Button backButton;
    /** Button to go to check if password is correct and if so pass user to EditProfileActivity */
    private Button confirmButton;

    /** Local user's password */
    private String correctPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_check);

        messageText = findViewById(R.id.password_check_message);
        inputPasswordEtx = findViewById(R.id.password_check_etx);
        backButton = findViewById(R.id.password_check_back_btn);
        confirmButton = findViewById(R.id.password_check_confirm_btn);

        //Handler for clicking on the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back to profile activity
                startActivity(new Intent(PasswordCheckActivity.this, ProfileActivity.class));

            }
        });

        //Handler for clicking on the confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get correct password from shared preferences
                SharedPreferences saved_values = getSharedPreferences(getString(R.string.PREF_KEY), Context.MODE_PRIVATE);
                correctPassword = saved_values.getString(getString(R.string.PASSWORD_KEY), null);

                if(inputPasswordEtx.getText().toString().equals(correctPassword)){
                    //go to edit profile activity
                    startActivity(new Intent(PasswordCheckActivity.this, EditProfileActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
