package recipeapp.Users;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author David Borucki
 *
 */
public interface LoginRepository extends JpaRepository<LoginUsers, Long> {
    LoginUsers findById(int id);
    LoginUsers findByUsername(String username);

    void deleteById(int id);
}
