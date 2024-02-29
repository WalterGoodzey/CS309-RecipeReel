package recipeapp.Recipes;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe findById(int id);

    void deleteById(int id);

    Recipe findByUser_Id(int id);
}
