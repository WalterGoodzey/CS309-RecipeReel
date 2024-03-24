package recipeapp.Comments;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author Will Custis
 */
@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String username;

    @NonNull
    private int recipeId;

    @NonNull
    private String content;

    private int likes = 0;

    public Comment(String username, int recipeId, String content){
        this.username = username;
        this.recipeId = recipeId;
        this.content = content;
    }
}
