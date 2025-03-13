package cupid.kafka.consumer;

import static cupid.common.SQLRestrictionClause.DELETED_AT_IS_NULL;

import cupid.common.domain.SoftDeletedDomain;
import cupid.kafka.KafkaDomainEventMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction(DELETED_AT_IS_NULL)
@SQLDelete(sql = "UPDATE kafka_message_consume_history SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(name = "kafka_message_consume_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class KafkaMessageConsumeHistory extends SoftDeletedDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(unique = true)
    private String uuid;

    public KafkaMessageConsumeHistory(String topic, String uuid) {
        this.topic = topic;
        this.uuid = uuid;
    }

    public static KafkaMessageConsumeHistory create(String topic, KafkaDomainEventMessage data) {
        return new KafkaMessageConsumeHistory(
                topic,
                data.uuid()
        );
    }
}
