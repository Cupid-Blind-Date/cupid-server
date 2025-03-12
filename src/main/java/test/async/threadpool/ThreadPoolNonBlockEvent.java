package test.async.threadpool;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.async.event.TestDomainEvent;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("ThreadPoolNonBlockEvent")
public class ThreadPoolNonBlockEvent extends TestDomainEvent {

    public ThreadPoolNonBlockEvent(Long requestId) {
        super(requestId);
    }
}
