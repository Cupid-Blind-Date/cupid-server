package cupid.fixture;

import cupid.member.domain.Password;

public class PasswordFixture {

    public Password password() {
        return Password.hashPassword("Password1@");
    }
}
