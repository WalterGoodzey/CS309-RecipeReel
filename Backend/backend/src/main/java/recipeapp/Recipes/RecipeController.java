package recipeapp.Recipes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import recipeapp.Tags.*;
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

    @Autowired
    TagRepository tagRepository;

    @Autowired
    TagRecipeConnectorRepository tagRecipeConnectorRepository;

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

    @GetMapping(path = "/recipes/{id}/image")
    Long getRecipePhotoID(@PathVariable int id) { return recipeRepository.findById(id).getPhotoID();}

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
    @GetMapping(path = "/recipes/search/string/{searchString}")
    List<Recipe> searchRecipesByTitle(@PathVariable String searchString){
        return recipeRepository.findByTitleContainingIgnoreCase(searchString);
    }

    @GetMapping(path = "recipes/search/tag/{tagName}")
    List<Recipe> searchRecipesByTag(@PathVariable String tagName){
        Tag tag = tagRepository.findByTagName(tagName);
        if(tag == null){
            return null;
        }
        return tag.getRecipes();
    }

    @GetMapping(path="recipes/search/both/{searchString}/{tagName}")
    List<Recipe> searchRecipesByBoth(@PathVariable String searchString, @PathVariable String tagName){
        List<Recipe> titleList = searchRecipesByTitle(searchString);
        List<Recipe> tagList = searchRecipesByTitle(searchString);
        titleList.retainAll(tagList);
        return titleList;
    }


    /**
     * Creates a new recipe.
     * @param recipe The Recipe object representing the new recipe.
     * @return A success message if the creation is successful, otherwise a failure message.
     */
    @PostMapping(path = "/recipes")
    String createRecipe(@RequestBody Recipe recipe){
        if (recipe == null)
            return failure;
        recipe.setUsername(userRepository.findById(recipe.getCreatorUserId()).getUsername());

        String[] userIn = recipe.getTags().split(" ");
        for(String tagName : userIn){
            Tag tag = tagRepository.findByTagName(tagName);
            if(tag != null){
                recipe.addTag(tag);
                tag.addRecipe(recipe);
            }
        }

        Users u = userRepository.findByUsername(recipe.getUsername());
        u.addRecipe(recipe);

        recipeRepository.save(recipe);
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
    @PutMapping(path="/recipes/{id}/image/{photoID}")
    Recipe updateRecipePhotoID(@PathVariable int id, @PathVariable Long photoID) {
        Recipe recipe = recipeRepository.findById(id);
        if (recipe == null)
            return null;
        recipe.setPhotoID(photoID);
        return recipeRepository.save(recipe);
    }

//    @PutMapping(path = "/recipes/{id}/rate/{rating}")
//    Method Found in RatingController

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
        Users u = userRepository.findById(recipe.getCreatorUserId());
        u.deleteSavedRecipe(recipe);
        u.deleteRecipe(recipe);
        for(Tag tag : recipe.getActualTags()){
            tag.removeRecipe((recipe));
        }
        recipeRepository.delete(recipe);
        return success;
    }

}
