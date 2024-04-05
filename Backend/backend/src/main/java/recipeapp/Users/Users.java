package recipeapp.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;
import recipeapp.Recipes.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the recipe sharing app.
 * This entity stores information about a user, including their username, email address, and password.
 * It also manages the list of recipes saved by the user.
 *
 * @author David Borucki
 */

@Entity @RequiredArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
public class Users {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by Spring Boot.
     * The @GeneratedValue generates a value if not already present. The strategy in this case is to start from 1 and increment for each table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** The username of the user. */
    @NonNull
    private String username;

    /** The email address of the user. */
    @NonNull
    private String emailAddress;

    /** The password of the user. */
    @NonNull
    private String password;

    /** The list of recipes saved by the user. */
    @OneToMany
    private List<Recipe> savedRecipes = new ArrayList<>();
    @OneToMany
    private List<Recipe> recipes = new ArrayList<>();


    /**
     * Adds a recipe to the list of saved recipes.
     * @param r The recipe to add.
     */
    public void addSavedRecipe(Recipe r) {
        this.savedRecipes.add(r);
    }

    public void addRecipe (Recipe r) { this.recipes.add(r);}

    /**
     * Deletes a recipe from the list of saved recipes.
     * @param r The recipe to delete.
     */
    public void deleteSavedRecipe(Recipe r) {
        this.savedRecipes.remove(r);
    }
}
