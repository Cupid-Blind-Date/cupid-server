package cupid.chat.presentation.websocket;

import static cupid.chat.presentation.websocket.config.WebSocketConfig.CLIENT_CHAT_SUBSCRIBE_PREFIX;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatSender {

    private final SimpMessagingTemplate messagingTemplate;

    public void send(ChatTopicMessage message) {
        // /sub/chat/{roomId} 를 구독한 클라이언트에게 메세지를 보낸다.
        messagingTemplate.convertAndSend(CLIENT_CHAT_SUBSCRIBE_PREFIX + message.roomId(), message);
    }
}
