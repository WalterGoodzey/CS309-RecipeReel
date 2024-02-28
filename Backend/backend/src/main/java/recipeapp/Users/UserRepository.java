package recipeapp.Users;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * @author David Borucki
 * 
 */ 

public interface UserRepository extends JpaRepository<Users, Long> {
    
    Users findById(int id);

    void deleteById(int id);

    Users findByRecipe_Id(int id);
}
