package cupid.member.domain;

import static cupid.member.exception.MemberExceptionCode.INVALID_CREDENTIALS;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import cupid.common.exception.ApplicationException;
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

    @Embedded
    private Username username;

    @Embedded
    private Password password;

    @Embedded
    private Nickname nickname;

    @Embedded
    private Age age;

    @Enumerated(STRING)
    private Gender gender;

    public Member(String username, String password, String nickname, int age, Gender gender) {
        this.username = Username.from(username);
        this.password = Password.hashPassword(password);
        this.nickname = Nickname.from(nickname);
        this.age = Age.from(age);
        this.gender = gender;
    }

    public void login(String plainPassword) {
        boolean same = this.password.checkPassword(plainPassword);
        if (!same) {
            throw new ApplicationException(INVALID_CREDENTIALS);
        }
    }
}
