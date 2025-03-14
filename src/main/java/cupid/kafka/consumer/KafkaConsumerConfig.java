package cupid.kafka.consumer;

import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE;

import cupid.kafka.KafkaDomainEventMessage;
import cupid.kafka.deadletter.DeadLetterRecorder;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Configuration
public class KafkaConsumerConfig {

    public static final String DOMAIN_EVENT_CONTAINER_FACTORY = "domainEventContainerFactory";

    private final DeadLetterRecorder deadLetterRecorder;
    private final KafkaIdempotencyFilter kafkaIdempotencyFilter;
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
    public ConsumerFactory<String, KafkaDomainEventMessage> defaultDomainEventConsumerFactory() {
        Map<String, Object> configs = getDefaultConfig();
        return new DefaultKafkaConsumerFactory<>(
                configs,
                new StringDeserializer(),
                new JsonDeserializer<>(KafkaDomainEventMessage.class, false)
        );
    }

    @Bean(DOMAIN_EVENT_CONTAINER_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<String, KafkaDomainEventMessage> domainEventContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaDomainEventMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // 수동 커밋
        factory.getContainerProperties().setAckMode(MANUAL_IMMEDIATE);
        factory.setConsumerFactory(defaultDomainEventConsumerFactory());

        // 파티션 수와 동일하게 맞춰야 성능이 좋음. 파티션에는 최대 1개의 스레드만 할당됨.
        factory.setConcurrency(1);

        // record 필터. (interceptor 이후 실행된다.)
        // retry 되는 경우에도 동일하게 호출된다.
        factory.setRecordFilterStrategy(kafkaIdempotencyFilter);

        // setRecordFilterStrategy 로 인해 필터링(중복 처리)된 record 에 대해서도 ack 처리하여 중복으로 처리하지 않도록 함.
        factory.setAckDiscarded(true);

        // 예외처리 핸들리
        // 재처리하지 않음.
        FixedBackOff fixedBackOff = new FixedBackOff(0, 0);
        DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler(deadLetterRecorder, fixedBackOff);
        factory.setCommonErrorHandler(defaultErrorHandler);
        return factory;
    }
}
