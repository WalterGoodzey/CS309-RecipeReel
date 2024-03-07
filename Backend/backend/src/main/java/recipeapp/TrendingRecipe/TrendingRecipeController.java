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
* @author Will Custis
 **/
@RestController
public class TrendingRecipeController {

    @Autowired
    TrendingRecipeRepository trendingRecipeRepository;

    @Autowired
    RecipeRepository recipeRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    //Returns a list of recipe objects from the trendingRecipes in TrendingRecipeRepository
    @GetMapping(path = "/trending")
    List<Recipe> getAllTrending(){
        List<Recipe> out = new ArrayList<>();
        List<TrendingRecipe> trendingList = trendingRecipeRepository.findAll();
        for (TrendingRecipe trendingRecipe : trendingList) {
            out.add(trendingRecipe.getRecipe());
        }
        return out;
    }

    //Returns a single recipe from a specified trending recipe object in the repository
    @GetMapping(path = "/trending/{id}")
    Recipe getTrendingRecipeById(@PathVariable int id){
        return trendingRecipeRepository.findById(id).getRecipe();
    }

    //Adds a recipe to a new trending recipe object where the body is the recipe id
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

    @PutMapping(path = "/trending/{id}")
    Recipe updateTrendingRecipe(@PathVariable int id, @RequestBody Recipe request){
        TrendingRecipe trendingRecipe = trendingRecipeRepository.findById(id);
        Recipe recipe = trendingRecipe.getRecipe();
        if(recipe == null){
            return null;
        }
        trendingRecipe.setRecipe(request);
        trendingRecipeRepository.save(trendingRecipe);
        return  trendingRecipe.getRecipe();
    }

    @DeleteMapping(path = "/trending/{id}")
    String deleteTrendingRecipe(@PathVariable int id){
        trendingRecipeRepository.deleteById(id);
        return success;
    }

}