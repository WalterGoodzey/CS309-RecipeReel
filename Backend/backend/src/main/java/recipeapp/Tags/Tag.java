package recipeapp.Tags;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.NonNull;
import recipeapp.Recipes.*;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Tag {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String tagName;

    @JsonIgnore
    @ManyToMany
    private List<Recipe> recipes = new ArrayList<>();

    public void addRecipe(Recipe recipe){
        this.recipes.add(recipe);
    }
}
