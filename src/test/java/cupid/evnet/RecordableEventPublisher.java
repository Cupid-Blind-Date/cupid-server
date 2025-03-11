package cupid.evnet;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class RecordableEventPublisher implements EventPublisher {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private boolean isRandomDelay = false;

    @Override
    public void publish(DomainEvent domainEvent) {
        if (isRandomDelay) {
            try {
                // 임의의 시간 딜레이 (퍼블리시 하는 네트워크 io의 딜레이 가정)
                Thread.sleep((long) (Math.random() * 1000 + 1));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        domainEvents.add(domainEvent);
    }

    public void setIsRandomDelay(boolean isRandomDelay) {
        this.isRandomDelay = isRandomDelay;
    }

    public List<DomainEvent> getDomainEvents() {
        return domainEvents;
    }

    public void clear() {
        this.domainEvents.clear();
        this.isRandomDelay = false;
    }
}
