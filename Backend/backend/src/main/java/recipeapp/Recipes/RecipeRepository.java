package recipeapp.Recipes;

import org.springframework.data.jpa.repository.JpaRepository;

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
     * Deletes a recipe by its ID.
     * @param id The ID of the recipe to delete.
     */
    void deleteById(int id);

}
