package recipeapp.Comments;

import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.OnClose;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author Will Custis
 */
@Controller
@ServerEndpoint(value = "/recipes/{recipeId}")
public class CommentSocket {

    private static CommentRepository commentRepo;

    @Autowired
    public void setCommentRepo(CommentRepository repo){
        commentRepo = repo;
    }

    @OnOpen
    public void onOpen(){

    }

    @OnMessage
    public void onMessage(){

    }

    @OnClose
    public void onClose(){

    }

    @OnError
    public void onError(){

    }

}
