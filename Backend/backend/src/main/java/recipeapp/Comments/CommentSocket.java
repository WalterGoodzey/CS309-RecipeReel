package recipeapp.Comments;

import jakarta.websocket.*;
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
 * @author Will Custis
 */
@Controller
@ServerEndpoint(value = "/recipes/{recipeId}/{username}")
public class CommentSocket {

    private static CommentRepository commentRepo;

    @Autowired
    public void setCommentRepo(CommentRepository repo){
        commentRepo = repo;
    }

    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(CommentSocket.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("recipeId") int recipeId, @PathParam("username") String username) throws IOException {

        logger.info("Entered into Open");

        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);

        try {
            usernameSessionMap.get(username).getBasicRemote().sendText(getComments(recipeId));
        } catch(IOException e) {
            logger.info("Exception: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @OnMessage
    public void onMessage(Session session, String content, @PathParam("recipeId") int recipeId) throws IOException{

        logger.info("Entered into Message: Got Comment:" + content);
        String username = sessionUsernameMap.get(session);

        sendComment(username + ": " + content + "\nLikes: 0");

        commentRepo.save(new Comment(username, recipeId, content));
    }

    @OnClose
    public void onClose(Session session){
        logger.info("Entered into Close");

        // remove the user connection information
        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

    }

    @OnError
    public void onError(Session session, Throwable throwable){
        // Do error handling here
        logger.info("Entered into Error");
        throwable.printStackTrace();

    }

    private String getComments(int recipeId){
        List<Comment> comments = commentRepo.findByRecipeId(recipeId);
        StringBuilder sb = new StringBuilder();
        if(comments != null && !comments.isEmpty()) {
            for (Comment comment : comments) {
                // Comment in form
                // username: content
                // Likes: number of likes
                sb.append(comment.getUsername()).append(": ").append(comment.getContent()).append("\nLikes: ").append(comment.getLikes()).append("\n");
            }
        }
        return sb.toString();
    }

    private void sendComment(String comment){
        sessionUsernameMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(comment);
            } catch(IOException e) {
                logger.info("Exception: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

}
