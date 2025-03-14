package cupid.common.kafka.producer;

import static cupid.common.exception.InternalServerExceptionCode.UNKNOWN_EXCEPTION;

import cupid.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * KafkaTemplate 의 Wrapper class. 일반적인 경우 Topic 은 String, Message 는 Json 직렬화 가능한 객체를 사용하게 되는데, 이때 해당 클래스를 사용한다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducer<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;

    public void produce(String topic, T message) {
        try {
            log.info("Try to produce topic using kafkaTemplate. topic: {}, message: {}", topic, message);
            kafkaTemplate.send(topic, message).get();
            log.info("Successfully produced topic using kafkaTemplate. topic: {}, message: {}", topic, message);
        } catch (Exception e) {
            log.error("Unexpected exception while send topic using kafkaTemplate. topic: {}, message: {}",
                    topic, message, e);
            throw new ApplicationException(UNKNOWN_EXCEPTION);
        }
    }
}
