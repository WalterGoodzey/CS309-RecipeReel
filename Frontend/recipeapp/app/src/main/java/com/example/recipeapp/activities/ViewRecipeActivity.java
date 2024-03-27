package com.example.recipeapp.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeapp.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity to view a single recipe in its entirety
 *
 * TODO - Update to display the rest of the recipe's data
 */
public class ViewRecipeActivity extends AppCompatActivity {
    /** TextView to display the title of the recipe */
    private TextView titleTxt;
    /** TextView to display the author of the recipe */
    private TextView authorTxt;
    /** TextView to display the description of the recipe */
    private TextView descriptionTxt;
    /** JSONObject to store the full recipe */
    private JSONObject fullRecipeJSON;

    /**
     * onCreate for ViewRecipeActivity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        titleTxt = findViewById(R.id.titleText);
        authorTxt = findViewById(R.id.authorText);
        descriptionTxt = findViewById(R.id.descriptionText);
        try {
            //get full JSON from intent
            fullRecipeJSON = new JSONObject(getIntent().getStringExtra("RecipeJsonAsString"));
            //fill text with data from JSON recipe
            titleTxt.setText(fullRecipeJSON.getString("title"));
            authorTxt.setText(fullRecipeJSON.getString("author"));
            descriptionTxt.setText(fullRecipeJSON.getString("description"));
        } catch (JSONException e) {

            throw new RuntimeException(e);
        }
    }
}
