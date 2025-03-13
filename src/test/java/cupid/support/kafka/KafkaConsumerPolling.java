package cupid.support.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumerPolling {

    public static <T> ConsumerRecords<String, T> waitingSync(String topic) {
        try {
            return KafkaConsumerPolling.<T>waitingAsync(topic).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> CompletableFuture<ConsumerRecords<String, T>> waitingAsync(String topic) {
        // Consumer 설정
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        CompletableFuture<ConsumerRecords<String, T>> future = CompletableFuture.supplyAsync(() -> {
            try (KafkaConsumer<String, T> consumer = new KafkaConsumer<>(props)) {
                consumer.subscribe(Collections.singletonList(topic));
                ConsumerRecords<String, T> records;
                long start = System.currentTimeMillis();
                do {
                    records = consumer.poll(Duration.ofMillis(300));
                } while (records.isEmpty() && System.currentTimeMillis() - start < 10_000); // 최대 10초 대기
                if (!records.isEmpty()) {
                    consumer.commitSync();
                }
                return records;
            }
        });
        return future;
    }
}
