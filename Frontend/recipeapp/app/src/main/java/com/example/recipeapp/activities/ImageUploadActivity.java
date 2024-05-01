package com.example.recipeapp.activities;

import static java.lang.Long.parseLong;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.recipeapp.MultipartRequest;
import com.example.recipeapp.R;
import com.example.recipeapp.VolleySingleton;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class ImageUploadActivity extends AppCompatActivity {

    Button selectBtn, uploadBtn, cancelBtn;
    ImageView mImageView;
    Uri selectedUri;

    // replace this with the actual address
    // 10.0.2.2 to be used for localhost if running springboot on the same host
    private static String SERVER_URL = "http://coms-309-018.class.las.iastate.edu:8080";

    /** UserId of profile if we came from edit profile, will be -1 if we came from edit recipe*/
    private int userId;
    /** RecipeId of recipe if we came from edit recipe, will be -1 if we came from edit profile*/
    private int recipeId;
    /** photoID of the image on the server (after uploading it) */
    private long photoID;
    private ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        mImageView = findViewById(R.id.imageSelView);
        selectBtn = findViewById(R.id.selectBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        photoID = 0;


        //get userId or recipeId from intent - set whichever we don't get to -1, get previous photoID
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userId = extras.getInt("userId", -1);
            recipeId = extras.getInt("recipeId", -1);
            photoID = extras.getLong("photoID", -1);
        }
        //fill image view with current photo
        makeImageRequest();

        // select image from gallery
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                // Handle the returned Uri
                if (uri != null) {
                    selectedUri = uri;
                    ImageView imageView = findViewById(R.id.imageSelView);
                    imageView.setImageURI(uri);
                }
        });

        selectBtn.setOnClickListener(v -> mGetContent.launch("image/*"));
        uploadBtn.setOnClickListener(v -> {
            if(selectedUri != null) {
                uploadImage();
            } else {
                Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_LONG).show();
            }
        });
        cancelBtn.setOnClickListener(v -> {
            if(recipeId > 0){
                //TODO
            } else if (userId > 0){
                Intent intent = new Intent(ImageUploadActivity.this, EditProfileActivity.class);
                //if(photoID > 0) {
                intent.putExtra("photoID", photoID);
                //}
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Odd cancel case", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    /**
     * Uploads an image to a remote server using a multipart Volley request.
     *
     * This method creates and executes a multipart request using the Volley library to upload
     * an image to a predefined server endpoint. The image data is sent as a byte array and the
     * request is configured to handle multipart/form-data content type. The server is expected
     * to accept the image with a specific key ("image") in the request.
     *
     */
    private void uploadImage(){

        byte[] imageData = convertImageUriToBytes(selectedUri);
        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST,
                SERVER_URL + "/image",
                imageData,
                response -> {
                    // Handle response
                    photoID = parseLong(response);
                    //Toast.makeText(getApplicationContext(), response,Toast.LENGTH_LONG).show();

                    Log.d("Upload", "Response: " + response);
                    putPhotoID();
                    //return to previous activity
                    if(recipeId > 0){
                        //TODO
                    } else if (userId > 0){
                        Intent intent = new Intent(ImageUploadActivity.this, EditProfileActivity.class);
                        //if(photoID > 0) {
                        intent.putExtra("photoID", photoID);
                        //}
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Odd cancel case", Toast.LENGTH_LONG).show();
                        finish();
                    }
                },
                error -> {
                    // Handle error
                    Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                    Log.e("Upload", "Error: " + error.getMessage());
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
    }

    /**
     * Converts the given image URI to a byte array.
     *
     * This method takes a URI pointing to an image and converts it into a byte array. The conversion
     * involves opening an InputStream from the content resolver using the provided URI, and then
     * reading the content into a byte array. This byte array represents the binary data of the image,
     * which can be used for various purposes such as uploading the image to a server.
     *
     * @param imageUri The URI of the image to be converted. This should be a content URI that points
     *                 to an image resource accessible through the content resolver.
     * @return A byte array representing the image data, or null if the conversion fails.
     * @throws IOException If an I/O error occurs while reading from the InputStream.
     */
    private byte[] convertImageUriToBytes(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            return byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void putPhotoID(){
        String PUT_PHOTOID_URL = "";
        if(userId > 0){
            PUT_PHOTOID_URL += SERVER_URL + "/users/" + userId + "/image/" + photoID;
        } else if (recipeId > 0) {
            PUT_PHOTOID_URL += SERVER_URL + "/recipes/" + recipeId + "/image/" + photoID;
        }
        JsonObjectRequest photoIDPUTReq = new JsonObjectRequest(Request.Method.PUT,
                PUT_PHOTOID_URL,
                null, // request body for PUT request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley Response", response.toString());
//                        Toast.makeText(getApplicationContext(), "Volley Received Response", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Update Unsuccessful (Volley Error)", Toast.LENGTH_LONG).show();
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(photoIDPUTReq);
    }

    /**
     * Making image request
     * */
    private void makeImageRequest() {

        ImageRequest imageRequest = new ImageRequest(
                SERVER_URL + "/image/" + photoID,
                //"http://sharding.org/outgoing/temp/testimg3.jpg", //for testing only!

                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Display the image in the ImageView
                        mImageView.setImageBitmap(response);
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Volley Error", error.toString());
                        //Display default image
                        //mImageView.setImageDrawable(getDrawable(R.drawable.ic_launcher_foreground));
                    }
                }
        );

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }
}


