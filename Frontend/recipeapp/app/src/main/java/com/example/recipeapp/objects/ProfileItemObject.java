package com.example.recipeapp.objects;

/**
 * Class to model a single profile preview object and it's data
 *
 * @author Ryan McFadden
 */
public class ProfileItemObject  {
    /** profile's username */
    private String username;

    /**
     * Contructor for a ProfileItemObject
     * @param username profile's username
     */
    public ProfileItemObject(String username){
        this.username = username;
    }

    /**
     * Getter for the username of the ProfileItemObject
     * @return username
     */
    public String getUsername() { return username; }

}
