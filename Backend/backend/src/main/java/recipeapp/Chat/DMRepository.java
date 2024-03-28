package recipeapp.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DMRepository extends JpaRepository<DM, Long> {

}
