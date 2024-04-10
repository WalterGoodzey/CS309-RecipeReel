package recipeapp.Rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import recipeapp.Recipes.Recipe;
import recipeapp.Recipes.RecipeRepository;
import recipeapp.Users.Users;

@RestController
public class RatingController {
    @Autowired
    RecipeRepository recipeRepository;

    void addRating (int recipeId, int rating) {

        /* Gets Recipe and User */
        Recipe recipeToRate = recipeRepository.findById(recipeId);
        //TODO Need to find a way to discover the User
        // Users userWhoRated

        /* Updates Values in Recipe */
        recipeToRate.setRatingCount(recipeToRate.getRatingCount() + 1);
        recipeToRate.setTotalRating(recipeToRate.getTotalRating() + rating);
        double newRatingDouble = recipeToRate.getTotalRating() / recipeToRate.getRatingCount();
        recipeToRate.setRecipeRating(newRatingDouble);

        /*Adds Rating Object to List in Recipe*/
        Rating newRatingObj = new Rating(recipeToRate, rating);
        recipeToRate.getRatings().add(newRatingObj);

    }
}
