package recipeapp.Users;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository interface for managing user login information in the recipe sharing app.
 * This interface provides methods for CRUD operations on LoginUsers entities.
 *
 * @author David Borucki
 */
public interface LoginRepository extends JpaRepository<LoginUsers, Long> {

    /**
     * Finds a user login by its ID.
     * @param id The ID of the user login to find.
     * @return The LoginUsers object with the specified ID, or null if not found.
     */
    LoginUsers findById(int id);

    /**
     * Finds a user login by its username.
     * @param username The username of the user login to find.
     * @return The LoginUsers object with the specified username, or null if not found.
     */
    LoginUsers findByUsername(String username);

    /**
     * Deletes a user login by its ID.
     * @param id The ID of the user login to delete.
     */
    void deleteById(int id);
}
