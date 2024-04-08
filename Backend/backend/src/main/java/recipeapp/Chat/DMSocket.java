package recipeapp.Chat;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.*;

/**
 * WebSocket endpoint for handling direct messaging between users in the recipe sharing app.
 */
@Controller      // Endpoint for springboot
@ServerEndpoint(value = "/chat/{username}/{sendToUsername}")  // Websocket URL
public class DMSocket {

    private static DMRepository dmRepository;
    String senderUsername;
    String receiverUsername;
    @Autowired
    public void setDmRepository(DMRepository repository) {
        dmRepository = repository;
    }
    private static ChatRoomRepository chatRoomRepository;
    @Autowired
    public void setChatRoomRepository(ChatRoomRepository chatRoomRepo) {
        chatRoomRepository = chatRoomRepo;
    }
    int chatRoomID;
    /** Map to store session and associated username. */
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<Session, String> sessionSendToUsernameMap = new Hashtable<>();

    /** Map to store username and associated session. */
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    /** Logger instance for logging messages. */
    private final Logger logger = LoggerFactory.getLogger(DMSocket.class);

    /**
     * Invoked when a new user joins the chat session.
     * @param session The WebSocket session of the user.
     * @param username The username of the user joining the chat.
     * @throws IOException If an I/O error occurs.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("sendToUsername") String sendToUsername) throws IOException {
        senderUsername = username;
        receiverUsername = sendToUsername;
        List<String> pair = new ArrayList<>();
        pair.add(username);
        pair.add(sendToUsername);
        pair.sort(null);
        String pairString = pair.get(0) + "&" + pair.get(1);

        ChatRoom chatRoomPossible = chatRoomRepository.findBySenderReceiver(pairString);

        if (chatRoomPossible == null) {
            ChatRoom newRoom = new ChatRoom(username, sendToUsername);
            chatRoomRepository.save(newRoom);
            chatRoomID = chatRoomRepository.findBySenderReceiver(pairString).getId();
        } else {
            chatRoomID = chatRoomPossible.getId();
        }

        logger.info("User " + username + " entered onOpen()");
        sessionUsernameMap.put(session,username);
        sessionSendToUsernameMap.put(session,sendToUsername);
        usernameSessionMap.put(username,session);
        //sendMessageToParticularUser(username,getChatHistory(username, sendToUsername));
        String msg = "User: " + username + " is online in the chat room now.";
	sendMessageToParticularUser(sendToUsername, msg);
    }
    /**
     * Invoked when a user sends a message in the chat.
     * @param session The WebSocket session of the user.
     * @param msg The message sent by the user.
     * @throws IOException If an I/O error occurs.
     */
    @OnMessage
    public void onMessage(Session session, String msg) throws IOException {
        logger.info("Entered onMessage(), got message '" + "'");
        //String username = sessionUsernameMap.get(session);
        String username = senderUsername;
        //String sendToUsername = sessionSendToUsernameMap.get(session);
        String sendToUsername = receiverUsername;
        broadcast(username + ": " + msg);
        dmRepository.save(new DM(username, sendToUsername, chatRoomID, msg));
    }
    /**
     * Invoked when a user leaves the chat session.
     * @param session The WebSocket session of the user.
     */
    @OnClose
    public void onClose(Session session) {
        String username = sessionUsernameMap.get(session);
        logger.info("User " + username + " entered onClose()");
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
        String msg = "User " + username + " has disconnected";
        broadcast(msg);
    }
    /**
     * Invoked when an error occurs in the WebSocket communication.
     * @param session The WebSocket session where the error occurred.
     * @param throwable The error that occurred.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("Entered into onError()");
        throwable.printStackTrace();
    }
    /**
     * Sends a message to a particular user in the chat session.
     * @param username The username of the recipient user.
     * @param msg The message to be sent.
     */
    private void sendMessageToParticularUser(String username, String msg){
        try {
            if (username.equals(receiverUsername)) {
                if (isReceiverOnline()) {
                    usernameSessionMap.get(username).getBasicRemote().sendText(msg);
                }
            } else {
                usernameSessionMap.get(username).getBasicRemote().sendText(msg);
            }
        }
        catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }
    /**
     * Broadcasts a message to all users in the chat session.
     * @param message The message to be broadcasted.
     */
    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }
        });
    }
    /**
     * Retrieves the chat history consisting of all previous messages.
     * @return The chat history as a string.
     */
    private String getChatHistory(String username, String sendToUsername) {
        List<DM> msgs = dmRepository.findByChatRoomId(chatRoomID);
        if (msgs == null){
            return "No chat room found for provided pair of users.";
        }
        // convert the list to a string
        StringBuilder sb = new StringBuilder();
        if(msgs != null && msgs.size() != 0) {
            for (DM msg : msgs) {
                sb.append(msg.getSender() + ": " + msg.getContent() + "\n");
            }
        }
        return sb.toString();
    }
//    private List<String> getChatHistory(String username, String sendToUsername, int id) {
//        List<DM> msgs = dmRepository.findByChatRoomId(chatRoomID);
//        List<String> ret = new ArrayList<>();
//        if (msgs == null){
//            return null;
//        }
//        if(msgs != null && msgs.size() != 0) {
//            for (DM msg : msgs) {
//                ret.add(msg.getSender() + ": " + msg.getContent());
//            }
//        }
//        return ret;
//    }
    boolean isReceiverOnline() {
        if (usernameSessionMap.containsKey(receiverUsername)) {
            return true;
        }
        return false;
    }






}
