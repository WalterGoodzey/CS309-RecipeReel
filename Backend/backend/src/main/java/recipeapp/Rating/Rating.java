package recipeapp.Rating;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import recipeapp.Recipes.Recipe;
import recipeapp.Users.Users;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @NonNull
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "user_id")

    private Users user;

    @NonNull
    private int rating;

}
