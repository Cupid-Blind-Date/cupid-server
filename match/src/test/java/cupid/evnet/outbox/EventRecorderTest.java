package cupid.evnet.outbox;

import static org.assertj.core.api.Assertions.assertThat;

import cupid.common.evnet.DomainEvent;
import cupid.common.evnet.DomainEventRepository;
import cupid.common.evnet.EventState;
import cupid.common.evnet.outbox.EventRecorder;
import cupid.evnet.mock.TestDomainEvent;
import cupid.support.ApplicationTest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DisplayName("EventRecorder 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class EventRecorderTest extends ApplicationTest {

    @Autowired
    private EventRecorder eventRecorder;

    @Autowired
    private DomainEventRepository domainEventRepository;

    @Test
    void 이벤트를_저장한다() {
        // given
        DomainEvent testDomainEvent = new TestDomainEvent(1L);

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
        TestDomainEvent event = new TestDomainEvent(1L);
        String first = event.getUuid();
        domainEventRepository.save(event);

        TestDomainEvent topic = new TestDomainEvent(first, EventState.INIT, 1L, "topic");

        // when
        DomainEvent recorded = eventRecorder.record(topic);

        // then
        assertThat(recorded.getUuid()).isNotEqualTo(first);
    }
}
