package cupid.infra.kafka.producer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cupid.common.exception.ApplicationException;
import cupid.common.kafka.producer.KafkaProducer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.kafka.core.KafkaTemplate;
import cupid.support.UnitTest;

@DisplayName("KafkaProducer 단위 테스트")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class KafkaProducerTest extends UnitTest {

    @InjectMocks
    private KafkaProducer<String> kafkaProducer;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void 예외가_발생하면_ApplicationException_으로_래핑() {
        // given
        given(kafkaTemplate.send(any(), any()))
                .willReturn(CompletableFuture.failedFuture(new RuntimeException()));

        // when & then
        assertThatThrownBy(() -> {
            kafkaProducer.produce("t", "m");
        }).isInstanceOf(ApplicationException.class);
    }

    @Test
    void 동기적으로_동작한다() throws ExecutionException, InterruptedException {
        // given
        CompletableFuture mock = mock(CompletableFuture.class);
        given(kafkaTemplate.send(any(), any()))
                .willReturn(mock);

        // when & then
        Assertions.assertDoesNotThrow(() -> {
            kafkaProducer.produce("t", "m");
        });
        verify(mock, times(1)).get();
    }
}
