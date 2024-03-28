package recipeapp.Chat;
import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dms")
@Data
public class DM {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String username;
    @Lob
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="sent")
    private Date sent = new Date();

    public DM(){};

    public DM(String username, String content) {
        this.username = username;
        this.content = content;
    };

}
