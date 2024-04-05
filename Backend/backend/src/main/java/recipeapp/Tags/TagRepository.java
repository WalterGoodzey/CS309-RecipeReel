package recipeapp.Tags;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Interface
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findById(int id);

    Tag findByTagName(String tagName);
}
