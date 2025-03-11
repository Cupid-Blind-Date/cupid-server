package test.async.threadpool;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.async.event.TestDomainEvent;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("ThreadPoolEvent")
public class ThreadPoolEvent extends TestDomainEvent {

    public ThreadPoolEvent(Long requestId) {
        super(requestId);
    }
}
