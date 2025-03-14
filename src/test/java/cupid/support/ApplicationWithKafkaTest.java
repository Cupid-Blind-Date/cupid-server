package cupid.support;

import cupid.support.db.DataClearExtension;
import cupid.support.kafka.KafkaConsumerPolling;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@EmbeddedKafka(
        brokerProperties = {
                // 9092 포트로 실행중인 작업이 없어야 함
                "listeners=PLAINTEXT://localhost:9092"
        },
        ports = {9092}
)
@ExtendWith(DataClearExtension.class)
@SpringBootTest
public class ApplicationWithKafkaTest extends CommonTest {

    protected <T> ConsumerRecords<String, T> waitingConsumeTopicSync(String topic) {
        return KafkaConsumerPolling.waitingSync(topic);
    }

    protected <T> CompletableFuture<ConsumerRecords<String, T>> waitingConsumeTopicAsync(String topic) {
        return KafkaConsumerPolling.waitingAsync(topic);
    }
}
