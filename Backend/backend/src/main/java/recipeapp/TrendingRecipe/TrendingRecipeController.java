package recipeapp.TrendingRecipe;

import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import recipeapp.Recipes.Recipe;
import recipeapp.Recipes.RecipeRepository;

import java.util.ArrayList;
import java.util.List;


/**
 * Controller class for managing trending recipes in the recipe sharing app.
 * This class handles HTTP requests related to trending recipes.
 *
 * @author Will Custis
 */
@RestController
public class TrendingRecipeController {

    @Autowired
    TrendingRecipeRepository trendingRecipeRepository;

    @Autowired
    RecipeRepository recipeRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    /**
     * Retrieves a list of all trending recipes.
     * @return List of Recipe objects representing all trending recipes.
     */
    @GetMapping(path = "/trending")
    List<Recipe> getAllTrending(){
        List<Recipe> out = new ArrayList<>();
        List<TrendingRecipe> trendingList = trendingRecipeRepository.findAll();
        for (TrendingRecipe trendingRecipe : trendingList) {
            out.add(trendingRecipe.getRecipe());
        }
        return out;
    }

    /**
     * Retrieves a trending recipe by its ID.
     * @param id The ID of the trending recipe to retrieve.
     * @return The Recipe object associated with the specified trending recipe ID.
     */
    @GetMapping(path = "/trending/{id}")
    Recipe getTrendingRecipeById(@PathVariable int id){
        return trendingRecipeRepository.findById(id).getRecipe();
    }

    /**
     * Adds a recipe to the list of trending recipes.
     * @param stringId The ID of the recipe to add (in string format).
     * @return A success message if the addition is successful, otherwise a failure message.
     */
    @PostMapping(path = "/trending")
    String addRecipeByRecipeId(@RequestBody String stringId){
        int id = Integer.parseInt(stringId);
        Recipe recipe = recipeRepository.findById(id);
        if(recipe == null){
            return failure;
        }
        TrendingRecipe trendingRecipe = new TrendingRecipe();
        trendingRecipe.setRecipe(recipe);
        trendingRecipeRepository.save(trendingRecipe);
        return success;
    }
    /**
     * Updates the recipe associated with a trending recipe.
     * @param id The ID of the trending recipe to update.
     * @param tempId The ID of the new recipe to associate with the trending recipe (in string format).
     * @return The updated Recipe object associated with the trending recipe.
     */
    @PutMapping(path = "/trending/{id}")
    Recipe updateTrendingRecipe(@PathVariable int id, @RequestBody String tempId){
        TrendingRecipe trendingRecipe = trendingRecipeRepository.findById(id);
        if(trendingRecipe == null){
            throw new RuntimeException("Trending Recipe ID not found");
        }
        int recipeId = Integer.parseInt(tempId);
        Recipe recipe = recipeRepository.findById(recipeId);
        trendingRecipe.setRecipe(recipe);
        trendingRecipeRepository.save(trendingRecipe);
        return  trendingRecipe.getRecipe();
    }

    /**
     * Deletes a trending recipe by its ID.
     * @param id The ID of the trending recipe to delete.
     * @return A success message if the deletion is successful, otherwise a failure message.
     */
    @DeleteMapping(path = "/trending/{id}")
    String deleteTrendingRecipe(@PathVariable int id){
        TrendingRecipe trending = trendingRecipeRepository.findById(id);
        trendingRecipeRepository.delete(trending);
        return success;
    }

}