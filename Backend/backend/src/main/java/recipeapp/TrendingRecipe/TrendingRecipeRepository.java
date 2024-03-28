package recipeapp.TrendingRecipe;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing trending recipes in the recipe sharing app.
 * This interface provides methods for CRUD operations on TrendingRecipe entities.
 *
 * @author Will Custis
 */

public interface TrendingRecipeRepository extends JpaRepository<TrendingRecipe, Long>{
    /**
     * Finds a trending recipe by its ID.
     * @param id The ID of the trending recipe to find.
     * @return The TrendingRecipe object with the specified ID, or null if not found.
     */
    TrendingRecipe findById(int id);

    /**
     * Deletes a trending recipe by its ID.
     * @param id The ID of the trending recipe to delete.
     */
    void deleteById(int id);

}