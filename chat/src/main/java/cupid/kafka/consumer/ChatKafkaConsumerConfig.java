package cupid.kafka.consumer;

import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE;

import cupid.chat.presentation.websocket.ChatTopicMessage;
import cupid.common.kafka.consumer.KafkaConsumerProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ChatKafkaConsumerConfig {

    public static final String CHAT_CONTAINER_FACTORY = "chatContainerFactory";

    private final KafkaConsumerProperty property;

    private Map<String, Object> getDefaultConfig() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, property.bootstrapServers());
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, property.groupId());

        // 수동 커밋
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        // 메세지 유실을 방지하기 위해 earliest 로 설정
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return configs;
    }

    @Bean
    public ConsumerFactory<String, ChatTopicMessage> chatTopicMessageConsumerFactory() {
        Map<String, Object> configs = getDefaultConfig();
        return new DefaultKafkaConsumerFactory<>(
                configs,
                new StringDeserializer(),
                new JsonDeserializer<>(ChatTopicMessage.class, false)
        );
    }

    @Bean(CHAT_CONTAINER_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<String, ChatTopicMessage> chatContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ChatTopicMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // 수동 커밋
        factory.getContainerProperties().setAckMode(MANUAL_IMMEDIATE);
        factory.setConsumerFactory(chatTopicMessageConsumerFactory());

        // 파티션 수와 동일하게 맞춰야 성능이 좋음. 파티션에는 최대 1개의 스레드만 할당됨.
        factory.setConcurrency(1);

        // 예외처리 핸들리
        // 1초 간격 2번 재시도
        FixedBackOff fixedBackOff = new FixedBackOff(1000, 2);
        DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler((consumerRecord, e) -> {
            log.error("Unexpected exception while consume chat message topic. offset: {}", consumerRecord.offset());
        }, fixedBackOff);
        factory.setCommonErrorHandler(defaultErrorHandler);
        return factory;
    }
}
