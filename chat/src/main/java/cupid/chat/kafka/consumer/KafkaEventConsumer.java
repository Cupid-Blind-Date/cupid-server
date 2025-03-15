package cupid.chat.kafka.consumer;

import static cupid.common.kafka.consumer.KafkaConsumerConfig.DOMAIN_EVENT_CONTAINER_FACTORY;

import cupid.chat.application.ChatRoomService;
import cupid.chat.client.CoupleClient;
import cupid.chat.client.response.GetCoupleResponse;
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
}
