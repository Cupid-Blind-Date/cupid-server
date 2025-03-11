package cupid.evnet;

import static org.assertj.core.api.Assertions.assertThat;

import cupid.support.ApplicationTest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DisplayName("TestEventRecorder 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class TestEventRecorderTest extends ApplicationTest {

    @Autowired
    private EventRecorder eventRecorder;

    @Autowired
    private DomainEventRepository domainEventRepository;

    @Test
    void 이벤트를_저장한다() {
        // given
        TestDomainEvent testDomainEvent = new TestDomainEvent(1L, "uuid", EventState.INIT);

        // when
        eventRecorder.record(testDomainEvent);

        // then
        Optional<DomainEvent> byId = domainEventRepository.findById(testDomainEvent.getId());
        assertThat(byId).isPresent();
        assertThat(byId.get().getState()).isEqualTo(EventState.INIT);
    }

    @Test
    void 이벤트의_uuid가_겹치는_경우_재시도() {
        // given
        domainEventRepository.save(new TestDomainEvent(1L, "uuid", EventState.INIT));
        TestDomainEvent testDomainEvent = new TestDomainEvent(2L, "uuid", EventState.INIT);

        // when
        DomainEvent recorded = eventRecorder.record(testDomainEvent);

        // then
        assertThat(recorded.getUuid()).isNotEqualTo("uuid");
    }
}
