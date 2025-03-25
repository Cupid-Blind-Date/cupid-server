package cupid.recommend.application;

import static cupid.recommend.config.RecommendAsyncConfig.RECOMMEND_ASYNC_TASK_EXECUTOR;

import cupid.filter.event.FilterUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecommendEventListener {

    private final RecommendService recommendService;

    @Async(RECOMMEND_ASYNC_TASK_EXECUTOR)
    @EventListener(value = FilterUpdateEvent.class)
    public void reloadCache(FilterUpdateEvent event) {
        Long memberId = event.memberId();
        log.info("Try to consume filter update event. reload recommended cache. memberId: {}", memberId);
        recommendService.reloadCache(memberId);
        log.info("Successfully consumed filter update event. reload recommended cache. memberId: {}", memberId);
    }
}
