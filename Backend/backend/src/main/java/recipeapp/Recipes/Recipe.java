package recipeapp.Recipes;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import recipeapp.Rating.Rating;
import recipeapp.Tags.*;
import recipeapp.Users.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Represents a recipe in the recipe sharing app.
 * This class stores information about a recipe including its title, creator, description, ingredients, instructions, tags, saved status, and rating.
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
    private int creatorUserId;

    /** The id of the user that created the recipe*/
    private String username;

    /** The username of the user that created the recipe*/
    @NonNull
    private String description;

    /** The list of ingredients needed for the recipe. */
    @NonNull
    private String ingredients;

    /** The instructions to prepare the recipe. */
    @NonNull
    private String instructions;

    private Long photoID = (long) -2;


    private String tags;
    private int rating = 0;

    /** The tags associated with the recipe (e.g., vegetarian, vegan, gluten-free). */
    @ManyToMany
    private List<Tag> actualTags = new ArrayList<>();

    public void addTag(Tag tag){
        this.actualTags.add(tag);
    }

    public void removeTag(Tag tag) {this.actualTags.remove(tag);}
    /** The rating of the recipe given by users. */
    @JsonIgnore
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Rating> ratings;
    private double recipeRating = 0.0;
    private int ratingCount = 0;
    private int totalRating = 0;
}
