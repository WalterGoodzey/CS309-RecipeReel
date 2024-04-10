package recipeapp.Rating;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import recipeapp.Recipes.Recipe;
import recipeapp.Users.Users;

@Entity
@Getter
@Setter
@Table(name = "rating")
@RequiredArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @NonNull
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "user_id")

    private Users user;

    @NonNull
    private int rating;

    public void addRating (Recipe recipeToRate, int rating) {

        /* Updates Values in Recipe */
        recipeToRate.setRatingCount(recipeToRate.getRatingCount() + 1);
        recipeToRate.setTotalRating(recipeToRate.getTotalRating() + rating);
        double newRatingDouble = recipeToRate.getTotalRating() / recipeToRate.getRatingCount();
        recipeToRate.setRecipeRating(newRatingDouble);

        /*Adds Rating Object to List in Recipe*/
        Rating ratingObj = new Rating(recipeToRate, rating);
        recipeToRate.getRatings().add(ratingObj);

    }

}
