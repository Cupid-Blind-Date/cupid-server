package cupid.filter.domain;

import static cupid.common.SQLRestrictionClause.DELETED_DATE_IS_NULL;

import cupid.common.domain.SoftDeletedDomain;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction(DELETED_DATE_IS_NULL)
@SQLDelete(sql = "UPDATE filter SET deleted_date = CURRENT_TIMESTAMP WHERE id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Filter extends SoftDeletedDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long memberId;

    @Embedded
    private AgeCondition ageCondition;

    @Embedded
    private DistanceCondition distanceCondition;

    @Enumerated(EnumType.STRING)
    private GenderCondition genderCondition;

    public Filter(
            Long memberId,
            AgeCondition ageCondition,
            DistanceCondition distanceCondition,
            GenderCondition genderCondition
    ) {
        this.memberId = memberId;
        this.ageCondition = ageCondition;
        this.distanceCondition = distanceCondition;
        this.genderCondition = genderCondition;
    }

    public void update(Filter filter) {
        this.ageCondition = filter.ageCondition;
        this.distanceCondition = filter.distanceCondition;
        this.genderCondition = filter.genderCondition;
    }
}
