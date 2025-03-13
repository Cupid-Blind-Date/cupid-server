package cupid.evnet.outbox;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cupid.evnet.DomainEvent;
import cupid.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("EventRecordListener 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class EventRecordListenerTest extends UnitTest {

    @InjectMocks
    private EventRecordListener eventRecordListener;

    @Mock
    private EventRecorder eventRecorder;

    @Test
    void 이벤트를_저장한다() {
        // given
        DomainEvent event = mock(DomainEvent.class);

        // when
        eventRecordListener.recordEvent(event);

        // then
        verify(eventRecorder, times(1)).record(any());
    }
}
