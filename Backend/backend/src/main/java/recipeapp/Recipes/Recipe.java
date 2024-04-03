package recipeapp.Recipes;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import recipeapp.Users.Users;

/**
 * Represents a recipe in the recipe sharing app.
 * This class stores information about a recipe including its title, description, ingredients, instructions, tags, saved status, and rating.
 * @author David Borucki
 * @author Will Custis
 */

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    
    /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /** The title of the recipe. */

    @NonNull
    private String title;
    /** The description of the recipe. */

    @NonNull
    private String description;
    /** The list of ingredients needed for the recipe. */

    @NonNull
    private String ingredients;
    /** The instructions to prepare the recipe. */

    @NonNull
    private String instructions;
    /** The tags associated with the recipe (e.g., vegetarian, vegan, gluten-free). */

    private String tags;
    /** The flag indicating whether the recipe is saved by the user. */

    private Boolean saved;
    /** The rating of the recipe given by users. */
    private double rating = 0.0;
}
