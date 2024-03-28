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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
/**
 * WebSocket endpoint for handling direct messaging between users in the recipe sharing app.
 */
@Controller      // Endpoint for springboot
@ServerEndpoint(value = "/chat/{username}")  // Websocket URL
public class DMSocket {

    private static DMRepository dmRepository;
    @Autowired
    public void setDmRepository(DMRepository repository) {
        dmRepository = repository;
    }
    /** Map to store session and associated username. */
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
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
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        logger.info("User " + username + " entered onOpen()");
        sessionUsernameMap.put(session,username);
        usernameSessionMap.put(username,session);
        sendMessageToParticularUser(username,getChatHistory());
        String msg = "User: " + username + " has entered the chat.";
        broadcast(msg);
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
        String username = sessionUsernameMap.get(session);
        broadcast(username + ": " + msg);
        dmRepository.save(new DM(username, msg));
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
            usernameSessionMap.get(username).getBasicRemote().sendText(msg);
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
    private String getChatHistory() {
        List<DM> msgs = dmRepository.findAll();

        // convert the list to a string
        StringBuilder sb = new StringBuilder();
        if(msgs != null && msgs.size() != 0) {
            for (DM msg : msgs) {
                sb.append(msg.getUsername() + ": " + msg.getContent() + "\n");
            }
        }
        return sb.toString();
    }





}
