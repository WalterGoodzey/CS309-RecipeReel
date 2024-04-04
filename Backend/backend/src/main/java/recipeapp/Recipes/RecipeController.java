package recipeapp.Recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import recipeapp.Tags.Tag;
import recipeapp.Users.Users;
import recipeapp.Users.UserRepository;

/**
 * Controller class for managing recipes in the recipe sharing app.
 * This class handles HTTP requests related to recipes.
 *
 * @author David Borucki
 * @author Will Custis
 */
@RestController
public class RecipeController {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    /**
     * Retrieves all recipes stored in the system.
     * @return List of Recipe objects representing all recipes.
     */
    @GetMapping(path = "/recipes")
    List<Recipe> getAllRecipes(){
        return recipeRepository.findAll();
    }

    /**
     * Retrieves a recipe by its ID.
     * @param id The ID of the recipe to retrieve.
     * @return The Recipe object with the specified ID.
     */
    @GetMapping(path = "/recipes/{id}")
    Recipe getRecipeById(@PathVariable int id){
        return recipeRepository.findById(id);
    }

    /**
     * Retrieves all recipes from the repo matching the search
     * @param searchString
     * @return A list of recipes with searchString as a substring of the title and matching the tags
     */
    @GetMapping(path = "/recipes/search/{searchString}")
    List<Recipe> searchRecipes(@PathVariable String searchString, @RequestBody List<Tag> tags){
        return recipeRepository.findByTitleContainingIgnoreCase(searchString);
    }



    /**
     * Creates a new recipe.
     * @param Recipe The Recipe object representing the new recipe.
     * @return A success message if the creation is successful, otherwise a failure message.
     */
    @PostMapping(path = "/recipes")
    String createRecipe(@RequestBody Recipe Recipe){
        if (Recipe == null)
            return failure;
        Recipe.setUsername(userRepository.findById(Recipe.getCreatorUserId()).getUsername());
        recipeRepository.save(Recipe);
        return success;
    }

    /**
     * Updates an existing recipe.
     * @param id The ID of the recipe to update.
     * @param request The updated Recipe object.
     * @return The updated Recipe object.
     */
    @PutMapping(path = "/recipes/{id}")
    Recipe updateRecipe(@PathVariable int id, @RequestBody Recipe request){
        Recipe recipe = recipeRepository.findById(id);
        if(recipe == null)
            return null;
        recipeRepository.save(request);
        return recipeRepository.findById(id);
    }

    /**
     * Deletes a recipe by its ID.
     * @param id The ID of the recipe to delete.
     * @return A success message if the deletion is successful, otherwise a failure message.
     */
    @DeleteMapping(path = "/recipes/{id}")
    String deleteRecipe(@PathVariable int id){
        Recipe recipe = recipeRepository.findById(id);
        if(recipe == null){
            return failure;
        }
        recipeRepository.delete(recipe);
        return success;
    }

}
