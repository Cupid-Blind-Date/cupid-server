package cupid.infra.kafka.consumer;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.kafka.consumer")
public record KafkaConsumerProperty(
        String bootstrapServers,
        String groupId
) {
}
