package cupid.async.threadpool;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import cupid.async.event.TestDomainEvent;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("ThreadPoolBlockEvent")
public class ThreadPoolBlockEvent extends TestDomainEvent {

    public ThreadPoolBlockEvent(Long requestId) {
        super(requestId);
    }
}
