package recipeapp.Trending;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Will Custis
 *
 */

public interface TrendingRepository extends JpaRepository<Trending, Long>{
    Trending findById(int trendingID);

    void deleteById(int trendingID);

    Trending findByRecipe_Id(int id);
}
