package cupid.member.domain;

import static cupid.member.exception.MemberExceptionCode.INVALID_CREDENTIALS;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import cupid.common.exception.ApplicationException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Member {

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
