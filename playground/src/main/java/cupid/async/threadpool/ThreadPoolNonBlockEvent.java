package cupid.async.threadpool;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import cupid.async.event.TestDomainEvent;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("ThreadPoolNonBlockEvent")
public class ThreadPoolNonBlockEvent extends TestDomainEvent {

    public ThreadPoolNonBlockEvent(Long requestId) {
        super(requestId);
    }
}
