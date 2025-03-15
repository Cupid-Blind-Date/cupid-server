package cupid.chat.presentation.websocket.config;

import cupid.chat.presentation.websocket.exception.WebsocketExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static final String CLIENT_DESTINATION_PREFIX = "/sub";

    private final WebSocketProperties webSocketProperties;
    private final WebsocketAuthInterceptor authInterceptor;
    private final WebsocketExceptionHandler webSocketExceptionHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // enableStompBrokerRelay 는 RabbitMQ 등에만 사용 가능. 카프카는 STOMP 를 지원하지 않음

        // Spring 에서 제공해주는 내장 STOMP 브로커 사용
        // 클라이언트는 /sub/~~ 를 구독한다. (stompClient.subscribe('/sub/~~~'))
        registry.enableSimpleBroker(CLIENT_DESTINATION_PREFIX);

        // 클라이언트가 메시지를 보낼 때 사용 (stompClient.send('/pub/~~~'))
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("WebSocketProperties: {}", webSocketProperties);

        if (webSocketProperties.allowOrigins() == null || webSocketProperties.allowOrigins().length == 0) {
            log.warn("Websocket AllowOrigins is empty. so using *");
            registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
            return;
        }
        registry.addEndpoint("/ws").setAllowedOrigins(webSocketProperties.allowOrigins()).withSockJS();

        registry.setErrorHandler(webSocketExceptionHandler);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authInterceptor);
    }
}
