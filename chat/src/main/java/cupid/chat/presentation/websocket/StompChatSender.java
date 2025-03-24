package cupid.chat.presentation.websocket;

import cupid.chat.kafka.topic.ReadChatTopicMessage;
import cupid.chat.kafka.topic.SendChatTopicMessage;
import cupid.chat.presentation.websocket.channel.ChattingChannelConfig.ReadChatChannel;
import cupid.chat.presentation.websocket.channel.ChattingChannelConfig.SendChatChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StompChatSender {

    private final SimpMessagingTemplate messagingTemplate;

    public void send(SendChatTopicMessage message) {
        // /sub/chat/{roomId} 를 구독한 클라이언트에게 메세지를 보낸다.
        messagingTemplate.convertAndSend(SendChatChannel.SUB + message.roomId(), message);
    }

    public void send(ReadChatTopicMessage message) {
        // /sub/read-chat/{roomId} 를 구독한 클라이언트에게 메세지를 보낸다.
        messagingTemplate.convertAndSend(ReadChatChannel.SUB + message.roomId(), message);
    }
}
