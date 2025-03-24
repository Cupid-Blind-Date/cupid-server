package cupid.chat.kafka.consumer;

import static cupid.chat.kafka.consumer.ChatKafkaConsumerConfig.READ_CHAT_CONTAINER_FACTORY;
import static cupid.chat.kafka.consumer.ChatKafkaConsumerConfig.SEND_CHAT_CONTAINER_FACTORY;
import static cupid.common.kafka.consumer.KafkaConsumerConfig.DOMAIN_EVENT_CONTAINER_FACTORY;

import cupid.chat.application.ChatRoomService;
import cupid.chat.client.CoupleClient;
import cupid.chat.client.response.GetCoupleResponse;
import cupid.chat.kafka.producer.ReadChatProducer;
import cupid.chat.kafka.producer.SendChatProducer;
import cupid.chat.kafka.topic.ReadChatTopicMessage;
import cupid.chat.kafka.topic.SendChatTopicMessage;
import cupid.chat.presentation.websocket.StompChatSender;
import cupid.common.event.CoupleMatchEvent;
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
public class KafkaConsumer {

    private final CoupleClient client;
    private final ChatRoomService chatRoomService;
    private final StompChatSender stompChatSender;

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

    @KafkaListener(topics = SendChatProducer.SEND_CHAT_TOPIC, containerFactory = SEND_CHAT_CONTAINER_FACTORY)
    public void consumeSendChatTopic(
            SendChatTopicMessage message,
            Acknowledgment ack,
            @Header(KafkaHeaders.OFFSET) int offset
    ) {
        Long messageId = message.chatMessageId();
        log.info("Try to consume send chat message topic. messageId:{}, offset: {}", messageId, offset);
        stompChatSender.send(message);
        ack.acknowledge();
        log.info("Successfully consume send chat message topic. messageId:{}, offset: {}", messageId, offset);
    }

    @KafkaListener(topics = ReadChatProducer.READ_CHAT_TOPIC, containerFactory = READ_CHAT_CONTAINER_FACTORY)
    public void consumeReadChatTopic(
            ReadChatTopicMessage message,
            Acknowledgment ack,
            @Header(KafkaHeaders.OFFSET) int offset
    ) {
        Long messageId = message.chatMessageId();
        log.info("Try to consume read chat message topic. messageId:{}, offset: {}", messageId, offset);
        stompChatSender.send(message);
        ack.acknowledge();
        log.info("Successfully consume read chat message topic. messageId:{}, offset: {}", messageId, offset);
    }
}
