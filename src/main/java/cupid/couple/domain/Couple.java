package cupid.couple.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_couple_higher_id_lower_id", columnNames = {"higherId", "lowerId"})
        }
)
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Couple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long higherId;
    private Long lowerId;

    public Couple(Long higherId, Long lowerId) {
        this.higherId = higherId;
        this.lowerId = lowerId;
    }
}
