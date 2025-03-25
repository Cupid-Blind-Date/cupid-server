package cupid.recommend.application;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cupid.filter.event.FilterUpdateEvent;
import cupid.support.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DisplayName("RecommendEventListener 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class RecommendEventListenerTest extends ApplicationTest {

    @Autowired
    private RecommendEventListener recommendEventListener;

    @Autowired
    private ApplicationEventPublisher publisher;

    @MockitoBean
    private RecommendService recommendService;

    @Test
    void 필터_업데이트_이벤트를_받아_회원_추천_캐시를_업데이트한다() {
        // when
        publisher.publishEvent(new FilterUpdateEvent(1L));

        // then
        verify(recommendService, times(1))
                .reloadCache(1L);
    }
}
