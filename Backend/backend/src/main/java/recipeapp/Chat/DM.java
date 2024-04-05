package recipeapp.Chat;
import java.util.Date;
import java.util.List;

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
    private String sender;
    @Column
    private String receiver;
    @Column
    private int chatRoomId;
    /** The content of the direct message. */
    @Lob
    private String content;
    /** The date and time when the direct message was sent. */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="sent")
    private Date sent = new Date();
    /** Default constructor */
    public DM(){};

    public DM(String sender, String receiver, int chatRoomId, String content) {
	this.sender = sender;
    this.receiver = receiver;
	this.chatRoomId = chatRoomId;
        this.content = content;
    };

}
