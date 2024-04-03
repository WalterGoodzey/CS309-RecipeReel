package recipeapp.TrendingRecipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;
import recipeapp.Recipes.Recipe;
/**
 * Represents a trending recipe in the recipe sharing app.
 * This entity stores information about a recipe that is currently trending.
 * @author Will Custis
 */
@Entity
public class TrendingRecipe {
    /** The primary key for the TrendingRecipe table. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The recipe associated with this trending recipe.
     * This serves as a foreign key to a recipe in the recipes table.
     */
    @OneToOne
    @Getter
    @Setter
    @JsonIgnore
    private Recipe recipe;

}