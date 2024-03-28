package recipeapp.Chat;
import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
/**
 * This class represents a direct message (DM) between two users in the recipe sharing app.
 */
@Getter
@Setter
@Entity
@Table(name = "dms")
@Data
public class DM {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /** The username of the sender of the direct message. */
    @Column
    private String username;
    /** The content of the direct message. */
    @Lob
    private String content;
    /** The date and time when the direct message was sent. */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="sent")
    private Date sent = new Date();
    /** Default constructor */
    public DM(){};
    /**
     * Constructs a new direct message with the given username and content.
     * @param username the username of the sender
     * @param content the content of the direct message
     */
    public DM(String username, String content) {
        this.username = username;
        this.content = content;
    };

}
