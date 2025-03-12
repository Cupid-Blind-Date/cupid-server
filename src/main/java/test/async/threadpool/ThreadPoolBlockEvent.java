package test.async.threadpool;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.async.event.TestDomainEvent;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("ThreadPoolBlockEvent")
public class ThreadPoolBlockEvent extends TestDomainEvent {

    public ThreadPoolBlockEvent(Long requestId) {
        super(requestId);
    }
}
