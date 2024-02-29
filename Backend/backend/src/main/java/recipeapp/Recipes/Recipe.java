package recipeapp.Recipes;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import recipeapp.Users.Users;

/**
 * 
 * @author Vivek Bengre
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

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private String ingredients;

    @NonNull
    private String instructions;

    private String tags;

    private Boolean saved;

}
