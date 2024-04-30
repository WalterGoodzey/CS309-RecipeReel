package com.example.recipeapp.objects;

import android.graphics.Bitmap;

/**
 * Class to model a single profile preview object and it's data
 *
 * @author Ryan McFadden
 */
public class ProfileItemObject  {
    /** profile's username */
    private String username;

    /** photoID of profile's profile picture */
    private Long photoID;
    /** Bitmap of profile's profile picture */
    private Bitmap profilePictureBitmap;

    /**
     * Contructor for a ProfileItemObject
     * @param username profile's username
     */
    public ProfileItemObject(String username, Long photoID){
        this.username = username;
        this.photoID = photoID;
    }

    /**
     * Getter for the username of the ProfileItemObject
     * @return username
     */
    public String getUsername() { return username; }

    /**
     * Getter for the profileID of the ProfileItemObject
     * @return profileID
     */
    public Long getPhotoID() { return photoID; }


}
