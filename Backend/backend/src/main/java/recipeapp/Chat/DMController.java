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
    @Autowired DMRepository dmRepository;
    @GetMapping(path = "/chatrooms/{username}")
    List<String> getUsersChatRooms(@PathVariable String username) {
        List<ChatRoom> chatRooms = chatRoomRepository.findBySender(username);
        List<ChatRoom> chatRooms2 = chatRoomRepository.findByReceiver(username);
        List<ChatRoom> chatRoomsBoth = new ArrayList<ChatRoom>();
        chatRoomsBoth.addAll(chatRooms);
        chatRoomsBoth.addAll(chatRooms2);

        List<String> ret = new ArrayList<>();
        for (ChatRoom cr : chatRoomsBoth) {
            if (cr.getReceiver().equals(username)) {
                ret.add(cr.getSender());
            } else if (cr.getSender().equals(username)) {
                ret.add(cr.getReceiver());
            }

        }
        return ret;
    }
    @GetMapping(path = "/chatrooms/{username}/{receiver}/history")
    List<String> getChatHistoryWithUsername(@PathVariable String username, @PathVariable String receiver) {
        List<String> ret = new ArrayList<>();
        List<String> pair = new ArrayList<>();
        pair.add(username);
        pair.add(receiver);
        pair.sort(null);
        String pairString = pair.get(0) + "&" + pair.get(1);

        ChatRoom chatRoomPossible = chatRoomRepository.findBySenderReceiver(pairString);
        int chatRoomID = chatRoomPossible.getId();

        List<DM>  msgs = dmRepository.findByChatRoomId(chatRoomID);

        if(msgs != null && msgs.size() != 0) {
            for (DM msg : msgs) {
                ret.add(msg.getSender() + ": " + msg.getContent());
            }
        }
        return ret;
    }
}
