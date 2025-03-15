package cupid.chat.presentation.websocket.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "websocket")
public record WebSocketProperties(
        String[] allowOrigins
) {
}
