package cupid.chat.presentation.websocket.config;

import cupid.chat.presentation.websocket.channel.ChattingChannelConfig.ErrorChannel;
import cupid.chat.presentation.websocket.channel.ChattingChannelConfig.ReadChatChannel;
import cupid.chat.presentation.websocket.channel.ChattingChannelConfig.SendChatChannel;
import cupid.chat.presentation.websocket.exception.WebsocketExceptionHandler;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.support.TaskExecutorAdapter;
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

    private final WebSocketProperties webSocketProperties;
    private final WebsocketExceptionHandler webSocketExceptionHandler;
    private final WebsocketAuthenticationInterceptor websocketAuthenticationInterceptor;
    private final WebsocketAuthorizationInterceptor websocketAuthorizationInterceptor;
    private final WebsocketValidateRoomInterceptor websocketValidateRoomInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // enableStompBrokerRelay 는 RabbitMQ 등에만 사용 가능. 카프카는 STOMP 를 지원하지 않음

        // Spring 에서 제공해주는 내장 STOMP 브로커 사용
        // 클라이언트는 /sub/~~ 를 구독한다. (stompClient.subscribe('/sub/~~~'))
        registry.enableSimpleBroker(
                ErrorChannel.SUB,
                SendChatChannel.SUB,
                ReadChatChannel.SUB
        );

        // 클라이언트가 메시지를 보낼 때 사용 (stompClient.send('/pub/~~~'))
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("WebSocketProperties: {}", webSocketProperties);
        registry.setErrorHandler(webSocketExceptionHandler);

        if (webSocketProperties.allowedOrigins() == null || webSocketProperties.allowedOrigins().length == 0) {
            log.warn("Websocket AllowOrigins is empty. so using *");
            registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
            return;
        }
        registry.addEndpoint("/ws").setAllowedOrigins(webSocketProperties.allowedOrigins()).withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(
                websocketAuthenticationInterceptor,
                websocketAuthorizationInterceptor,
                websocketValidateRoomInterceptor
        );
        registration.executor(websocketVirtualThreadTaskExecutor());
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.executor(websocketVirtualThreadTaskExecutor());
    }

    @Bean
    public Executor websocketVirtualThreadTaskExecutor() {
        ExecutorService concurrentExecutor = Executors.newVirtualThreadPerTaskExecutor();
        return new TaskExecutorAdapter(concurrentExecutor);
    }
}
