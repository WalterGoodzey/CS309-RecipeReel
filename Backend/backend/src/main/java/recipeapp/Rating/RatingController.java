package recipeapp.Rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import recipeapp.Recipes.Recipe;
import recipeapp.Recipes.RecipeRepository;

@RestController
public class RatingController {
    @Autowired
    RecipeRepository recipeRepository;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @PutMapping(path = "/recipes/{id}/rate/{rating}")
    String addRating (@PathVariable int id, @PathVariable int rating) {
        Recipe recipeToRate = recipeRepository.findById(id);
        Rating ratingObj = new Rating(recipeToRate, rating);

        /* Updates Values in Recipe */
        recipeToRate.setRatingCount(recipeToRate.getRatingCount() + 1);
        int newCount = recipeToRate.getRatingCount() + 1;
        recipeToRate.setTotalRating(recipeToRate.getTotalRating() + rating);
        int newTotalRating = recipeToRate.getTotalRating() + rating;
        double newRatingDouble = (double) newTotalRating / newCount;
        recipeToRate.setRecipeRating(newRatingDouble);

        /* Adds Rating Object to List in Recipe */
        recipeToRate.getRatings().add(ratingObj);
        recipeRepository.save(recipeToRate);

        return success;
    }
}
