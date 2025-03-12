//package cupid.chat.application;
//
//
//import static cupid.evnet.Topic.TOPIC_COUPLE_MATCH;
//import static cupid.infra.kafka.consumer.KafkaConsumerConfig.DOMAIN_EVENT_CONTAINER_FACTORY;
//
//import cupid.infra.kafka.KafkaDomainEventMessage;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class ChatKafkaConsumer {
//
//    private final ChatService chatService;
//
//    @KafkaListener(topics = , containerFactory = DOMAIN_EVENT_CONTAINER_FACTORY)
//    public void consumeCoupleMatchMessage(
//            KafkaDomainEventMessage domainEventData,
//            Acknowledgment ack,
//            @Header(KafkaHeaders.OFFSET) int offset
//    ) {
//        log.info("Hi!! KafkaDomainEventMessage:{}, offset:{}", domainEventData, offset);
//        throw new RuntimeException("Exception!");
//    }
//}
