package recipeapp.Recipes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import recipeapp.Users.Users;
import recipeapp.Users.UserRepository;

/**
 * 
 * @author David Borucki
 * @author Will Custis
 * 
 */ 

@RestController
public class RecipeController {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    UserRepository userRepository;
    
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/recipes")
    List<Recipe> getAllRecipes(){
        return recipeRepository.findAll();
    }

    @GetMapping(path = "/recipes/{id}")
    Recipe getRecipeById(@PathVariable int id){
        return recipeRepository.findById(id);
    }

    @PostMapping(path = "/recipes")
    String createRecipe(@RequestBody Recipe Recipe){
        if (Recipe == null)
            return failure;
        recipeRepository.save(Recipe);
        return success;
    }

    @PutMapping(path = "/recipes/{id}")
    Recipe updateRecipe(@PathVariable int id, @RequestBody Recipe request){
        Recipe recipe = recipeRepository.findById(id);
        if(recipe == null)
            return null;
        recipeRepository.save(request);
        return recipeRepository.findById(id);
    }

    @DeleteMapping(path = "/recipes/{id}")
    String deleteRecipe(@PathVariable int id){

        // Check if there is an object depending on user and then remove the dependency
        Users users = userRepository.findByRecipe_Id(id);
        users.setRecipe(null);
        userRepository.save(users);

        // delete the recipe if the changes have not been reflected by the above statement
        recipeRepository.deleteById(id);
        return success;
    }
}
