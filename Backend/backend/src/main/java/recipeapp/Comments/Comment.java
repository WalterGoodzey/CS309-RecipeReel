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
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String username;

    @NonNull
    private String content;

    private int likes = 0;
}
