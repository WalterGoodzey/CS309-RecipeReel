package recipeapp.Tags;

import org.springframework.data.jpa.repository.JpaRepository;
import recipeapp.Recipes.Recipe;

import java.util.List;

public interface TagRecipeConnectorRepository extends JpaRepository<TagRecipeConnector, Long> {

    TagRecipeConnector findById(int id);

    List<TagRecipeConnector> findByRecipe(Recipe recipe);

    List<TagRecipeConnector> findByTag(Tag tag);
}
