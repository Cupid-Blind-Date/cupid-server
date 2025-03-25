package cupid.filter.application;

import cupid.filter.application.command.FilterCreateCommand;
import cupid.filter.application.command.FilterUpdateCommand;
import cupid.filter.domain.Filter;
import cupid.filter.domain.FilterRepository;
import cupid.filter.event.FilterUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilterService {

    private final ApplicationEventPublisher publisher;
    private final FilterRepository filterRepository;

    public Long createFilter(FilterCreateCommand command) {
        log.info("Try to create filter. memberId: {}", command.memberId());
        Filter filter = command.toFilter();
        Filter save = filterRepository.save(filter);
        publisher.publishEvent(new FilterUpdateEvent(filter.getMemberId()));
        return save.getId();
    }

    public void updateFilter(FilterUpdateCommand command) {
        log.info("Try to update filter. memberId: {}", command.memberId());
        Filter filter = filterRepository.getByMemberId(command.memberId());
        filter.update(command.toFilter());
        filterRepository.save(filter);
        publisher.publishEvent(new FilterUpdateEvent(filter.getMemberId()));
    }

    public boolean hasFilter(Long memberId) {
        return filterRepository.existsByMemberId(memberId);
    }
}
