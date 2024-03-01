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


    // Foreign key to a recipe in the recipes table, any data about the recipe
    // in the trending tab will be acquired using this ID.
    @OneToOne
    @Getter
    @Setter
    @JsonIgnore
    private Recipe recipe;

}