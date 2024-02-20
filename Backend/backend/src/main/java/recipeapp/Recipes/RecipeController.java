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

import recipeapp.Users.User;
import recipeapp.Users.UserRepository;

/**
 * 
 * @author Vivek Bengre
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

    @GetMapping(path = "/laptops")
    List<Recipe> getAllLaptops(){
        return recipeRepository.findAll();
    }

    @GetMapping(path = "/laptops/{id}")
    Recipe getLaptopById(@PathVariable int id){
        return recipeRepository.findById(id);
    }

    @PostMapping(path = "/laptops")
    String createLaptop(@RequestBody Recipe Recipe){
        if (Recipe == null)
            return failure;
        recipeRepository.save(Recipe);
        return success;
    }

    @PutMapping(path = "/laptops/{id}")
    Recipe updateLaptop(@PathVariable int id, @RequestBody Recipe request){
        Recipe recipe = recipeRepository.findById(id);
        if(recipe == null)
            return null;
        recipeRepository.save(request);
        return recipeRepository.findById(id);
    }

    @DeleteMapping(path = "/recipes/{id}")
    String deleteRecipe(@PathVariable int id){

        // Check if there is an object depending on user and then remove the dependency
        User user = userRepository.findByRecipe_Id(id);
        user.setRecipe(null);
        userRepository.save(user);

        // delete the laptop if the changes have not been reflected by the above statement
        recipeRepository.deleteById(id);
        return success;
    }
}
