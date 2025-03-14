package cupid.async.event;

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
    protected LocalDateTime initializedDate; // 객체 생성 시점
    protected LocalDateTime processedDate; // 실제 처리된 시점

    // 객체 생성 시점 - DB 기록 시점
    protected long initializedDateToCreatedDateDuration;

    // DB 기록 시점 - 처리 완료 시점
    protected long createdDateToProcessedDateDuration;

    // 객체 생성 시점 - 처리 완료 시점
    protected long initializedDateToProcessedDateDuration;

    @Enumerated(EnumType.STRING)
    protected TestEventState state;

    public TestDomainEvent(Long requestId) {
        this.initializedDate = LocalDateTime.now();
        this.uuid = UUID.randomUUID().toString();
        this.state = TestEventState.INIT;
        this.requestId = requestId;
    }

    public void publishSuccess() {
        this.state = TestEventState.PUBLISH_SUCCESS;
        this.processedDate = LocalDateTime.now();
        log.info("이벤트 객체 생성 시점: {}, DB 기록 시점: {}, 처리 완료 시점: {}", initializedDate, getCreatedDate(), processedDate);
        initializedDateToCreatedDateDuration = Duration.between(initializedDate, getCreatedDate()).toMillis();
        createdDateToProcessedDateDuration = Duration.between(getCreatedDate(), processedDate).toMillis();
        initializedDateToProcessedDateDuration = Duration.between(initializedDate, processedDate).toMillis();
        log.info("객체 생성 시점 - DB 기록 시점: {}ms", initializedDateToCreatedDateDuration);
        log.info("DB 기록 시점 - 처리 완료 시점: {}ms", createdDateToProcessedDateDuration);
        log.info("객체 생성 시점 - 처리 완료 시점: {}ms", initializedDateToProcessedDateDuration);
    }

    public void regenerateUuid() {
        this.uuid = UUID.randomUUID().toString();
    }

    public void publishFail() {
        this.state = TestEventState.PUBLISH_FAIL;
    }
}
