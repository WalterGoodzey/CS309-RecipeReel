package recipeapp.TrendingRecipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;
import recipeapp.Recipes.Recipe;

@Entity
public class TrendingRecipe {
    // This is the primary key for the Trending table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Who added the recipe to the trending page
    // Temporary until trending page is generated automatically
    @NonNull
    private String addedBy;

    // Foreign key to a recipe in the recipes table, any data about the recipe
    // in the trending tab will be aquired using this ID.
    @OneToOne
    @Getter
    @JsonIgnore
    private Recipe recipe;

}