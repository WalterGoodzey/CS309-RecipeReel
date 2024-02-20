package recipeapp.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import recipeapp.Recipes.Recipe;

/**
 * 
 * @author David Borucki
 * 
 */ 

@Entity @AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class User {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String emailAddress;
    private String password;

    //I still need to figure out one to one, one to many which this is needed for
//    public void setRecipe(Recipe recipe) {
//        this.recipe = recipe;
//    }
}
