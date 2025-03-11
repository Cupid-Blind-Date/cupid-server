package test.async.event;

import static jakarta.persistence.InheritanceType.SINGLE_TABLE;
import static lombok.AccessLevel.PROTECTED;

import cupid.common.domain.SoftDeletedDomain;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@DiscriminatorColumn(name = "event_type", length = 255)
@Inheritance(strategy = SINGLE_TABLE)
public abstract class TestDomainEvent extends SoftDeletedDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(unique = true)
    protected String uuid;

    protected Long requestId;
    protected LocalDateTime produceTime;  // ApplicationEventListener 에 의해 produce 된 시점
    protected LocalDateTime processedTime; // 실제 처리된 시점

    @Enumerated(EnumType.STRING)
    protected TestEventState state;


    public TestDomainEvent(Long requestId) {
        this.requestId = requestId;
        this.produceTime = LocalDateTime.now();
    }

    public void init() {
        this.uuid = UUID.randomUUID().toString();
        this.state = TestEventState.INIT;
    }

    public void publishSuccess() {
        this.state = TestEventState.PUBLISH_SUCCESS;
        this.processedTime = LocalDateTime.now();
        LocalDateTime produceTime = getProduceTime();
        LocalDateTime processedTime = getProcessedTime();
        // 시간 차이 계산
        Duration duration = Duration.between(produceTime, processedTime);
        log.info("총 초 차이: {} s", duration.getSeconds());
        log.info("총 밀리초 차이: {} ms", duration.toMillis());
    }

    public void regenerateUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

    public void publishFail() {
        this.state = TestEventState.PUBLISH_FAIL;
    }
}
