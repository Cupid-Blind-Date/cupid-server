package cupid.support;

import org.springframework.kafka.test.context.EmbeddedKafka;

@EmbeddedKafka(
        brokerProperties = {
                // 9092 포트로 실행중인 작업이 없어야 함
                "listeners=PLAINTEXT://localhost:9092"
        },
        ports = {9092}
)
public class ApplicationWithKafkaTest extends ApplicationTest {
}
