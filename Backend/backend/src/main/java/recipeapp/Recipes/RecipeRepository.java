package recipeapp.Recipes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe findById(int id);

    @Transactional
    void deleteById(int id);
}
