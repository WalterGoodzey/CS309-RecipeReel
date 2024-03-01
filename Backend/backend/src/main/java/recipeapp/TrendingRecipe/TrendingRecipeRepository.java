package recipeapp.TrendingRecipe;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Will Custis
 *
 */

public interface TrendingRecipeRepository extends JpaRepository<TrendingRecipe, Long>{
    TrendingRecipe findById(int id);

    void deleteById(int id);

}