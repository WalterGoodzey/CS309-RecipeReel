package recipeapp.Users;

import java.util.List;

import org.apache.catalina.User;
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

   /*
    * In order of POST / GET / PUT / DELETE
    */

    @Autowired
    UserRepository userRepository;
    @Autowired
    RecipeRepository recipeRepository;

    /*
     * Success and Failure return Strings
     */
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    /*
     * POST - creates dummy users
     * params : none
     * return: String (success/failure)
     *
     * you can use this if you need some users to test certain functionalities
     * kinda OP if database is nuked, and we need quick users
     */
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

    /*
     * POST - create new user
     * params: new user (nuser)
     * return: user
     *
     * if no other users in db, save new user and return user
     * if other users in db, check to see if user exists (by username or email)
     */
    @PostMapping(path = "/newuser")
    Users createUser(@RequestBody Users nuser) {
        boolean exists = false;
        if (userRepository.count() > 0) {
            List<Users> users = getAllUsers();
            for (Users e : users) {
                if (e.getUsername().equals(nuser.getUsername()) || e.getEmailAddress().equals(nuser.getEmailAddress())) {
                    exists = true;
                    break;
                }
            }
        }
        if (!exists) {
            userRepository.save(nuser);
            return nuser;
        }
        return null;
    }

    /*
     * POST - login user
     * params: user that Frontend sends
     * return: user from Backend's DB if username & password match
     *
     * if no user exists, nothing is returned
     */
    @PostMapping(path = "/login")
    Users login(@RequestBody Users user) {
        List<Users> existingUsers = getAllUsers();
        for (Users e : existingUsers) {
            if (e.getUsername().equals(user.getUsername()) && e.getPassword().equals(user.getPassword()))
                return e;
        }
        return null;
    }
    /*
     * GET - get all users
     * params: none
     * return: List of Users type
     */
    @GetMapping(path = "/users")
    List<Users> getAllUsers(){
        return userRepository.findAll();
    }

    /*
     * GET - get certain user by id
     * params: int of users id
     * return: User
     */
    @GetMapping(path = "/users/{id}")
    Users getUserById(@PathVariable int id){
        return userRepository.findById(id);
    }


    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        Users u = userRepository.findById(id);
        userRepository.delete(u);
        return success;
    }

    /*
     * DELETE - deletes a user using Users object
     * params: User object
     * return: String (success/failure)
     */
    @DeleteMapping(path = "/users")
    String deleteUser(@RequestBody Users users) {
        if (users == null)
            return failure;
        userRepository.delete(users);
        return success;
    }
}
