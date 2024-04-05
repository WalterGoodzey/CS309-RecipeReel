package recipeapp.Chat;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter @Setter @Entity
@Table(name = "chat_rooms")
public class ChatRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NonNull @Column
	private String sender;
	@NonNull @Column
	private String receiver;
	@Column
	private String senderReceiver;
	public ChatRoom(String memberOne, String memberTwo) {
		sender = memberOne;
		receiver = memberTwo;
		senderReceiver = sender +"&"+receiver;
	}

	public ChatRoom() {

	}
}
