package recipeapp.Recipes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import recipeapp.Tags.Tag;

import java.util.List;

/**
 * Repository interface for managing recipes in the recipe sharing app.
 * This interface provides methods for CRUD operations on Recipe entities.
 *
 * @author David Borucki
 */

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    /**
     * Finds a recipe by its ID.
     * @param id The ID of the recipe to find.
     * @return The Recipe object with the specified ID, or null if not found.
     */
    Recipe findById(int id);

    /**
     * Finds a List of recipes created by a user
     * @param id
     * @return A List of recipes with the given creatorUserId
     */
    List<Recipe> findByCreatorUserId(int id);

    /**
     * Finds a List of recipes by title
     * @param searchString
     * @return A List object of recipes whose titles contain the searchString
     */
    List<Recipe> findByTitleContainingIgnoreCase(String searchString);

    List<Recipe> findByTagsContaining(String tags);


    /**
     * Deletes a recipe by its ID.
     * @param id The ID of the recipe to delete.
     */
    void deleteById(int id);
}
