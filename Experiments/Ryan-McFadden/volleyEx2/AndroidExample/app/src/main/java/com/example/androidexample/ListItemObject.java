package com.example.androidexample;

import org.json.JSONObject;

public class ListItemObject {
    private String title;
    private String description;

    private JSONObject fullRecipe;

    public ListItemObject(String title, String description, JSONObject fullRecipe) {
        this.title = title;
        this.description = description;
        this.fullRecipe = fullRecipe;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public JSONObject getFullRecipe() { return fullRecipe; }
}
