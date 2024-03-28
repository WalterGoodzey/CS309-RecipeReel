package com.example.recipeapp;

import org.json.JSONObject;
/**
 *  Class to model a single recipe preview object and it's data
 *
 *  @author Ryan McFadden
 */
public class RecipeItemObject {
    /** title of the recipe */
    private String title;
    /** author of the recipe */
    private String author;
    /** description of the recipe */
    private String description;
    /** JSONObject containing full recipe */
    private JSONObject fullRecipe;

    /**
     * Constructor for a RecipeItemObject
     * @param title title of recipe
     * @param author author of recipe
     * @param description description of recipe
     * @param fullRecipe JSONObject which stores the full recipe
     */
    public RecipeItemObject(String title, String author, String description, JSONObject fullRecipe) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.fullRecipe = fullRecipe;
    }

    /**
     * Getter for the title of the RecipeItemObject
     * @return title
     */
    public String getTitle() { return title; }
    /**
     * Getter for the author of the RecipeItemObject
     * @return author
     */
    public String getAuthor() { return author; }
    /**
     * Getter for the description of the RecipeItemObject
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Getter for the full recipe of the RecipeItemObject
     * @return fullRecipe
     */
    public JSONObject getFullRecipe() { return fullRecipe; }
}
