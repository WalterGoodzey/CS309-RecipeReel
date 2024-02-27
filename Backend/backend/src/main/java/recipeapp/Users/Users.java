package recipeapp.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;
import recipeapp.Recipes.Recipe;

/**
 * 
 * @author David Borucki
 * 
 */ 

@Entity @RequiredArgsConstructor @NoArgsConstructor
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

    //Change to OneToMany

    @OneToOne
    @JsonIgnore
    @Getter
    @Setter
    private Recipe recipe;
}
