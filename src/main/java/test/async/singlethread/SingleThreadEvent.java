package test.async.singlethread;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import test.async.event.TestDomainEvent;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("SingleThreadEvent")
public class SingleThreadEvent extends TestDomainEvent {

    public SingleThreadEvent(Long requestId) {
        super(requestId);
    }
}
