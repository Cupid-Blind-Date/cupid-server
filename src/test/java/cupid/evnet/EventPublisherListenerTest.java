package cupid.evnet;

import static org.assertj.core.api.Assertions.assertThat;

import cupid.support.ApplicationTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("EventPublisherListener 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class EventPublisherListenerTest extends ApplicationTest {

    @Autowired
    private RecordableEventPublisher eventPublisher;

    @Autowired
    private TestEventService testEventService;

    @Autowired
    private DomainEventRepository domainEventRepository;

    @AfterEach
    void tearDown() {
        eventPublisher.clear();
    }

    @Test
    void 이벤트_발행_트랜잭션_커밋_후_이벤트가_발행된다() {
        // when
        testEventService.eventOccur(1L);

        // then
        assertThat(eventPublisher.getDomainEvents()).hasSize(1);
        assertThat(domainEventRepository.findAll().get(0).getState()).isEqualTo(EventState.PUBLISH_SUCCESS);
    }

    @Test
    void 이벤트는_싱글스레드_내에서_순차적으로_발송된다() {
        // given
        // 임의의 시간 딜레이 (퍼블리시 하는 네트워크 io의 딜레이 가정)
        eventPublisher.setIsRandomDelay(true);

        // when
        for (long i = 0; i < 100000; i++) {
            testEventService.eventOccur(i);
        }

        // then
        List<DomainEvent> domainEvents = new ArrayList<>(eventPublisher.getDomainEvents());
        domainEvents.sort((o1, o2) -> Math.toIntExact(o1.id - o2.id));
        List<DomainEvent> domainEvents1 = eventPublisher.getDomainEvents();
        assertThat(domainEvents).containsSequence(domainEvents1);
    }
}
