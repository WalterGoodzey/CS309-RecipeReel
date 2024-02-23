package com.example.recipeapp;

import org.json.JSONObject;

public class ListItemObject {
    private String title;

    private String author;

    private String description;

    private JSONObject fullRecipe;

    public ListItemObject(String title, String author, String description, JSONObject fullRecipe) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.fullRecipe = fullRecipe;
    }

    public String getTitle() { return title; }

    public String getAuthor() { return author; }

    public String getDescription() {
        return description;
    }

    public JSONObject getFullRecipe() { return fullRecipe; }
}
