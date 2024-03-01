package recipeapp.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;
import recipeapp.Recipes.Recipe;

import java.util.List;

/**
 * 
 * @author David Borucki
 * 
 */ 

@Entity @NoArgsConstructor
@Getter @Setter
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

    public Users(@NonNull String username, @NonNull String emailAddress, @NonNull String password) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public Users(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

}
