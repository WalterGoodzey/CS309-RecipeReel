package recipeapp.Tags;

import recipeapp.Recipes.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRecipeConnecterRepository  extends JpaRepository<TagRecipeConnecter, Long> {

    TagRecipeConnecter findById(int id);

    List<TagRecipeConnecter> findByRecipeId(int id);

    List<TagRecipeConnecter> findByTagId(int id);
}
