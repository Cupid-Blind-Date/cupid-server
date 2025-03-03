package cupid.member.domain;

import static cupid.member.exception.MemberExceptionCode.INVALID_AGE;

import cupid.common.exception.ApplicationException;

public record Age(
        int age
) {
    public static Age from(int age) {
        validate(age);
        return new Age(age);
    }

    private static void validate(int age) {
        if (age < 0) {
            throw new ApplicationException(INVALID_AGE);
        }
    }
}
