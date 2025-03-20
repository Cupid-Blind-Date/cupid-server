package cupid.kafka.consumer;

import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE;

import cupid.chat.presentation.websocket.ChatTopicMessage;
import cupid.common.kafka.consumer.KafkaConsumerProperty;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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

    private Map<String, Object> getChatConfig() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, property.bootstrapServers());

        String uuid = UUID.randomUUID().toString();
        // scale out 된 여러 chat server 에서 동일한 topic 의 메세지를 모두 받아오기 위해 consumer 그룹을 다르게 지정한다.
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, property.groupId() + uuid);

        // 수동 커밋
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        // earliest 로 설정하면 애플리케이션이 뜰 때마다 consumer 그룹이 새로 만들어져 지금까지 진행된 모든 대화 메세지 topic 을 읽어온다.
        // 이를 방지하기 위해 latest 로 설정한다.
        // 메세지 유실은 걱정하지 않아도 되는게, 메세지를 보내기 전 어차피 db 에 메세지를 저장한다. 따라서 채팅방 입장 시 읽어오게 된다.
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return configs;
    }

    @Bean
    public ConsumerFactory<String, ChatTopicMessage> chatTopicMessageConsumerFactory() {
        Map<String, Object> configs = getChatConfig();
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
