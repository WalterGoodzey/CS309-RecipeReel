package recipeapp.Users;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author David Borucki
 * 
 */ 

public interface UserRepository extends JpaRepository<User, Long> {
    
    User findById(int id);

    void deleteById(int id);

    User findByRecipe_Id(int id);
}
