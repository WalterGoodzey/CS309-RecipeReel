package recipeapp.Users;

import java.util.ArrayList;
import java.util.Comparator;
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
 * Controller class for managing users in the recipe sharing app.
 * This class handles HTTP requests related to users.
 *
 * @author David Borucki
 */

@RestController
public class UserController {

   /*
    * In order of POST / GET / PUT / DELETE
    */

    @Autowired
    UserRepository userRepository;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    RecipeRepository recipeRepository;

    /*
     * Success and Failure return Strings
     */
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    /**
     * POST - creates dummy users for testing.
     * @return A success message if the creation is successful, otherwise a failure message.
     */
    @PostMapping(path = "/dummyusers")
    String createDummyUsers() {
        Users users1 = new Users("daveb", "dave@iastate.edu", "password1");
        LoginUsers usersOne = new LoginUsers("daveb", "password1");

        Users users2 = new Users("ryanm","ryan@iastate.edu", "password2");
        LoginUsers usersTwo = new LoginUsers("ryanm", "password2");

        Users users3 = new Users("willc", "will@iastate.edu", "password3");
        LoginUsers usersThree = new LoginUsers("willc", "password3");

        Users users4 = new Users("walterg", "walter@iastate.edu", "password4");
        LoginUsers usersFour = new LoginUsers("walterg", "password4");

        userRepository.save(users1);
        loginRepository.save(usersOne);
        userRepository.save(users2);
        loginRepository.save(usersTwo);
        userRepository.save(users3);
        loginRepository.save(usersThree);
        userRepository.save(users4);
        loginRepository.save(usersFour);

        return success;
    }


    /**
     * POST - create a new user.
     * @param nuser The new user to create.
     * @return The created user object if creation is successful, otherwise null.
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
            LoginUsers nuser1 = new LoginUsers(nuser.getUsername(),nuser.getPassword());
            loginRepository.save(nuser1);
            nuser.setLoginUsers(nuser1);
            userRepository.save(nuser);
            return nuser;
        }
        return new Users();
    }

    /**
     * POST - login user.
     * @param user The user to log in.
     * @return String of success or failure. If user is found and usernames/passwords match, success. Else failure
     */
    @PostMapping(path = "/login")
    Users login(@RequestBody Users user) {
        List<Users> existingUsers = getAllUsers();
        for (Users e : existingUsers) {
            if (e.getUsername().equals(user.getUsername()) && e.getPassword().equals(user.getPassword()))
                return e;
        }
        return new Users();
    }

    /**
     * POST - add a saved recipe to a user.
     * @param id The ID of the user.
     * @param r The recipe to add.
     * @return A success message if the addition is successful, otherwise a failure message.
     */
    @PostMapping(path="/users/{id}/savedrecipes/{recipeId}")
    String addSavedRecipe(@PathVariable int id, @PathVariable int recipeId) {
        Users u = userRepository.findById(id);
        Recipe r = recipeRepository.findById(recipeId);
        if(u == null || r == null){
            return failure;
        }
        u.addSavedRecipe(r);
        userRepository.save(u);
        return success;
    }

    @DeleteMapping(path="users/{id}/savedrecipes/{recipeId}")
    String removeSavedRecipe(@PathVariable int id, @PathVariable int recipeId){
        Users u = userRepository.findById(id);
        Recipe r = recipeRepository.findById(recipeId);
        if(u == null){
            return failure;
        }
        u.deleteSavedRecipe(r);
        userRepository.save(u);
        return success;
    }
    /**
     * GET - get all users.
     * @return List of Users.
     */
    @GetMapping(path = "/users")
    List<Users> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}/recipes")
    List<Recipe> getUsersRecipes(@PathVariable int id) {
        Users u = getUserById(id);
        return u.getRecipes();
    }

    /**
     * GET - get saved recipes of a user.
     * @param id The ID of the user.
     * @return List of saved recipes.
     */
    @GetMapping(path ="/users/{id}/savedrecipes")
    List<Recipe> getSavedRecipes(@PathVariable int id) {
        Users u = userRepository.findById(id);
        return u.getSavedRecipes();
    }
    /**
     * GET - get all logins.
     * @return List of LoginUsers.
     */
    @GetMapping(path = "/userslogins")
    List<LoginUsers> getAllLogins(){
        return loginRepository.findAll();
    }

    /**
     * GET - get a user by ID.
     * @param id The ID of the user.
     * @return The user object.
     */
    @GetMapping(path = "/users/{id}/image")
    Long getUserPhotoID(@PathVariable int id) { return userRepository.findById(id).getPhotoID();}
    @GetMapping(path = "/users/{id}")
    Users getUserById(@PathVariable int id){
        return userRepository.findById(id);
    }
    /**
     * PUT - update a user by ID.
     * @param id The ID of the user.
     * @param updatedUser The updated user object.
     * @return A success message if the update is successful, otherwise a failure message.
     */
    @PutMapping("/users/{id}")
    Users updateUser(@PathVariable int id, @RequestBody Users updatedUser){
        //System.out.println(updatedUser.toString());
        Users user = userRepository.findById(id);
        //System.out.println(user.toString());
        LoginUsers userOne = user.getLoginUsers();
       // System.out.println(userOne.toString());

        if(user == null) {
            throw new RuntimeException("user id does not exist");
        }
        if (!updatedUser.getUsername().equals(user.getUsername())) {
            user.setUsername(updatedUser.getUsername());
            userOne.setUsername(updatedUser.getUsername());
        }
        if (!updatedUser.getEmailAddress().equals(user.getEmailAddress())) {
            user.setEmailAddress(updatedUser.getEmailAddress());
        }
        if (!updatedUser.getPassword().equals(user.getPassword())) {
            user.setPassword(updatedUser.getPassword());
            userOne.setPassword(updatedUser.getPassword());
        }
        userRepository.save(user);
        //loginRepository.save(userOne);
        return user;
    }
    @PutMapping(path="/users/{id}/image/{photoID}")
    Users updateUsersPhotoID(@PathVariable int id, @PathVariable Long photoID) {
        Users u = userRepository.findById(id);
        if (u == null)
            return null;
        u.setPhotoID(photoID);
        return userRepository.save(u);
    }

    @GetMapping(path = "/users/{id}/following")
    List<Users> getFriends(@PathVariable int id){
        Users u = userRepository.findById(id);
        if (u == null){
            return null;
        }
        //Option to only output usernames
//        List<String> out = new ArrayList<>();
//        List<Users> friends = u.getFriendedUsers();
//        for(Users friend : friends){
//            out.add(friend.getUsername());
//        }
//        return out;

        return u.getFollowedUsers();
    }

    @GetMapping(path = "/users/{id}/following-recipes")
    List<Recipe> getFollowingUsersRecipes(@PathVariable int id){
        List<Users> friends = userRepository.findById(id).getFollowedUsers();
        List<Recipe> allFriendsRecipes = new ArrayList<>();
        for (Users u : friends) {
            allFriendsRecipes.addAll(u.getRecipes());
        }
        allFriendsRecipes.sort(Comparator.comparingInt(Recipe::getId).reversed());
        return allFriendsRecipes;
    }

    @PostMapping(path = "/users/{id}/following/{followId}")
    Users addFriend(@PathVariable int id, @PathVariable int followId){
        Users u = userRepository.findById(id);
        Users friend = userRepository.findById(followId);
        if(u == null || friend == null){
            return null;
        }
        u.follow(friend);
        userRepository.save(u);
        return friend;
    }

    @DeleteMapping(path = "/users/{id}/following/{followId}")
    String deleteFriend(@PathVariable int id, @PathVariable int followId){
        Users u = userRepository.findById(id);
        Users friend = userRepository.findById(followId);
        if(u == null || friend == null){
            return failure;
        }
        u.unfollow(friend);
        userRepository.save(u);
        return success;
    }

    /**
     * DELETE - delete a user by ID.
     * @param id The ID of the user.
     * @return A success message if the deletion is successful, otherwise a failure message.
     */
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        Users u = userRepository.findById(id);
        LoginUsers lU = u.getLoginUsers();
        lU.setUser(null);
        userRepository.delete(u);
        return success;
    }


    /**
     * DELETE - delete a user.
     * @param users The user to delete.
     * @return A success message if the deletion is successful, otherwise a failure message.
     */
    @DeleteMapping(path = "/users")
    String deleteUser(@RequestBody Users users) {
        if (users == null)
            return failure;
        userRepository.delete(users);
        return success;
    }
}
