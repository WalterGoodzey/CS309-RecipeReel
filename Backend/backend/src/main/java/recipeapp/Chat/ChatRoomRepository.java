package recipeapp.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    ChatRoom findBySenderReceiver(String senderReceiver);
    List<ChatRoom> findBySender(String sender);
    List<ChatRoom> findByReceiver (String receiver);
}
