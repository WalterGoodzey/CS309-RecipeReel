package recipeapp.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;
import recipeapp.Recipes.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author David Borucki
 * 
 */ 

@Entity @RequiredArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
public class Users {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String username;

    @NonNull
    private String emailAddress;

    @NonNull
    private String password;
    @OneToMany
    private List<Recipe> savedRecipes = new ArrayList<>();

    public List<Recipe> getSavedRecipes() {
        return savedRecipes;
    }
    public void addSavedRecipe(Recipe r) {
        this.savedRecipes.add(r);
    }
    public void deleteSavedRecipe(Recipe r) {
        this.savedRecipes.remove(r);
    }


}
