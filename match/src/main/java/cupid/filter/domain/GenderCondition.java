package cupid.filter.domain;

import cupid.member.domain.Gender;

public enum GenderCondition {

    ONLY_MALE(Gender.MALE),
    ONLY_FEMALE(Gender.FEMALE),
    ;

    private final Gender targetGender;

    GenderCondition(Gender targetGender) {
        this.targetGender = targetGender;
    }

    public Gender getTargetGender() {
        return targetGender;
    }
}
