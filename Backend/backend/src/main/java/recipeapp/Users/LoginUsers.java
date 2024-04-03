package recipeapp.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;
import recipeapp.Recipes.Recipe;

import java.util.List;

/**
 * Represents a user for login purposes in the recipe sharing app.
 * This entity stores information about a user, including their username and password.
 *
 * @author David Borucki
 */
@Entity @RequiredArgsConstructor @NoArgsConstructor
@Getter @Setter @ToString
public class LoginUsers{

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** The username of the user. */
    @NonNull
    private String username;

    /** The password of the user. */
    @NonNull
    private String password;


}
