package cupid.member.domain;

import static cupid.common.SQLRestrictionClause.DELETED_AT_IS_NULL;
import static cupid.member.exception.MemberExceptionCode.INVALID_CREDENTIALS;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import cupid.common.domain.SoftDeletedDomain;
import cupid.common.exception.ApplicationException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction(DELETED_AT_IS_NULL)
@SQLDelete(sql = "UPDATE member SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(name = "member")
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Member extends SoftDeletedDomain {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @Embedded
    private Password password;

    @Column(nullable = false, length = 10, unique = false)
    private String nickname;

    private int age;

    @Enumerated(STRING)
    private Gender gender;

    public Member(String username, String password, String nickname, int age, Gender gender) {
        this.username = username;
        this.password = Password.hashPassword(password);
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
    }

    public void login(String plainPassword) {
        boolean same = this.password.checkPassword(plainPassword);
        if (!same) {
            throw new ApplicationException(INVALID_CREDENTIALS);
        }
    }
}
