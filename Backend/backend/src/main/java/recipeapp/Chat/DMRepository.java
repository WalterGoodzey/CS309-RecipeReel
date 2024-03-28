package recipeapp.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * This interface defines a repository for managing direct messages (DMs) in the recipe sharing app.
 */
@Repository
public interface DMRepository extends JpaRepository<DM, Long> {

}
