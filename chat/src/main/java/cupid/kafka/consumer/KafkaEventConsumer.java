package cupid.kafka.consumer;

import static cupid.common.kafka.consumer.KafkaConsumerConfig.DOMAIN_EVENT_CONTAINER_FACTORY;
import static cupid.kafka.consumer.ChatKafkaConsumerConfig.CHAT_CONTAINER_FACTORY;

import cupid.chat.application.ChatProducer;
import cupid.chat.application.ChatRoomService;
import cupid.chat.client.CoupleClient;
import cupid.chat.client.response.GetCoupleResponse;
import cupid.chat.presentation.websocket.ChatSender;
import cupid.chat.presentation.websocket.ChatTopicMessage;
import cupid.common.evnet.CoupleMatchEvent;
import cupid.common.kafka.KafkaDomainEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaEventConsumer {

    private final CoupleClient client;
    private final ChatRoomService chatRoomService;
    private final ChatSender chatSender;

    @KafkaListener(topics = CoupleMatchEvent.COUPLE_MATCH_EVENT_TOPIC, containerFactory = DOMAIN_EVENT_CONTAINER_FACTORY)
    public void consumeCoupleMatchEvent(
            KafkaDomainEventMessage message,
            Acknowledgment ack,
            @Header(KafkaHeaders.OFFSET) int offset
    ) {
        Long coupleId = message.targetDomainId();
        log.info("Try to consume couple match event topic. coupleId:{}, uuid: {}, offset: {}",
                coupleId,
                message.uuid(),
                offset
        );
        GetCoupleResponse couple = client.getCoupleById(coupleId);
        chatRoomService.createChatRoom(couple.higherId(), couple.lowerId());
        ack.acknowledge();
        log.info("Successfully consume couple match event topic. coupleId:{}, uuid: {}, offset: {}",
                coupleId,
                message.uuid(),
                offset
        );
    }

    @KafkaListener(topics = ChatProducer.CHAT_TOPIC, containerFactory = CHAT_CONTAINER_FACTORY)
    public void consumeChatTopic(
            ChatTopicMessage message,
            Acknowledgment ack,
            @Header(KafkaHeaders.OFFSET) int offset
    ) {
        Long messageId = message.chatMessageId();
        log.info("Try to consume chat message topic. messageId:{}, offset: {}", messageId, offset);
        chatSender.send(message);
        ack.acknowledge();
        log.info("Successfully consume chat message topic. messageId:{}, offset: {}", messageId, offset);
    }
}
