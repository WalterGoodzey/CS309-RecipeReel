package recipeapp.Chat;


import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
/**
 * Configuration class for WebSocket server endpoint.
 */
@Configuration
public class DMSocketConfig {
    /**
     * Bean definition for the ServerEndpointExporter.
     * This bean registers annotated endpoints with the WebSocket container.
     * @return ServerEndpointExporter instance.
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
