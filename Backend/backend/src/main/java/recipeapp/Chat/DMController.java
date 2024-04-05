package recipeapp.Chat;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DMController {
    @Autowired ChatRoomRepository chatRoomRepository;
    @GetMapping(path = "/chatrooms/{username}")
    List<String> getUsersChatRooms(@PathVariable String username) {
        List<ChatRoom> chatRooms = chatRoomRepository.findBySender(username);
        List<String> ret = new ArrayList<>();
        for (ChatRoom cr : chatRooms) {
            ret.add(cr.getReceiver());
        }
        return ret;
    }
}
