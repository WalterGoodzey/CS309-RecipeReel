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

@Controller      // Endpoint for springboot
@ServerEndpoint(value = "/chat/{username}")  // Websocket URL
public class DMSocket {

    private static DMRepository dmRepository;
    @Autowired
    public void setDmRepository(DMRepository repository) {
        dmRepository = repository;
    }
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();
    private final Logger logger = LoggerFactory.getLogger(DMSocket.class);

    /*
     * User has joined the session
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
    /*
     * User has sent a message
     */
    @OnMessage
    public void onMessage(Session session, String msg) throws IOException {
        logger.info("Entered onMessage(), got message '" + "'");
        String username = sessionUsernameMap.get(session);
        broadcast(username + ": " + msg);
        dmRepository.save(new DM(username, msg));
    }
    /*
     * User has left the session
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
    /*
     * Error handling
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("Entered into onError()");
        throwable.printStackTrace();
    }
    /*
     * Send a message to a particular user in session
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
    /*
     * Sends message to everyone in session
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
    /*
     * Receives history of all messages
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
