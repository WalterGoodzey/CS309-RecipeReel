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
    List<Users> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    Users getUserById(@PathVariable int id){
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    String createUser(@RequestBody Users users){
        if (users == null)
            return failure;
        userRepository.save(users);
        return success;
    }
    //ONLY USE THIS IF NO OTHER DATAS ARE IN DB. JUST FOR BACKEND TESTING
    @PostMapping(path = "/dummyusers")
    String createDummyUsers() {
        Users users1 = new Users("daveb", "dave@iastate.edu", "password1");
        Users users2 = new Users("ryanm","ryan@iastate.edu", "password2");
        Users users3 = new Users("willc", "will@iastate.edu", "password3");
        Users users4 = new Users("walterg", "walter@iastate.edu", "password4");
        userRepository.save(users1);
        userRepository.save(users2);
        userRepository.save(users3);
        userRepository.save(users4);

        return success;
    }

    @DeleteMapping(path = "/users")
    String deleteUser(@RequestBody Users users) {
        if (users == null)
            return failure;
        userRepository.delete(users);
        return success;
    }

    @PutMapping("/users/{id}")
    Users updateUser(@PathVariable int id, @RequestBody Users request){
        Users users = userRepository.findById(id);

        if(users == null) {
            throw new RuntimeException("user id does not exist");
        }
        else if (users.getId() != id){
            throw new RuntimeException("path variable id does not match User request id");
        }

        userRepository.save(request);
        return userRepository.findById(id);
    }

    @PutMapping("/users/{userId}/recipes/{recipeId}")
    String assignRecipeToUser(@PathVariable int userId,@PathVariable int recipeId){
        Users users = userRepository.findById(userId);
        Recipe recipe = recipeRepository.findById(recipeId);
        if(users == null || recipe == null)
            return failure;
        recipe.setUsers(users);
        users.setRecipe(recipe);
        userRepository.save(users);
        return success;
    }

    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }
}
