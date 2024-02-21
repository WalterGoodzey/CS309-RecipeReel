package recipeapp.Users;

import java.util.List;

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

/**
 * 
 * @author David Borucki
 * 
 */ 

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RecipeRepository recipeRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        if (user == null)
            return failure;
        userRepository.save(user);
        return success;
    }
    //ONLY USE THIS IF NO OTHER DATAS ARE IN DB. JUST FOR BACKEND TESTING
    @PostMapping(path = "/dummyusers")
    String createDummyUsers() {
        if (userRepository != null)
            return failure;
        User user1 = new User("daveb", "dave@iastate.edu", "password1");
        User user2 = new User("ryanm","ryan@iastate.edu", "password2");
        User user3 = new User("willc", "will@iastate.edu", "password3");
        User user4 = new User("walterg", "walter@iastate.edu", "password4");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        return success;
    }

    @DeleteMapping(path = "/users")
    String deleteUser(@RequestBody User user) {
        if (user == null)
            return failure;
        userRepository.delete(user);
        return success;
    }

    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);

        if(user == null) {
            throw new RuntimeException("user id does not exist");
        }
        else if (user.getId() != id){
            throw new RuntimeException("path variable id does not match User request id");
        }

        userRepository.save(request);
        return userRepository.findById(id);
    }

    @PutMapping("/users/{userId}/recipes/{recipeId}")
    String assignRecipeToUser(@PathVariable int userId,@PathVariable int recipeId){
        User user = userRepository.findById(userId);
        Recipe recipe = recipeRepository.findById(recipeId);
        if(user == null || recipe == null)
            return failure;
        recipe.setUser(user);
        user.setRecipe(recipe);
        userRepository.save(user);
        return success;
    }

    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }
}
