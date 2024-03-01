package recipeapp.Users;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author David Borucki
 *
 */
public interface LoginRepository extends JpaRepository<Users, Long> {
    Users findById(int id);
    Users findByUsername(String username);

    void deleteById(int id);
}