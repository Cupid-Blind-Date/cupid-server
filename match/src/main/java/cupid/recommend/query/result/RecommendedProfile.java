package cupid.recommend.query.result;

import cupid.member.domain.Gender;
import cupid.member.domain.Member;

public record RecommendedProfile(
        Long id,
        String nickname,
        int age,
        Gender gender,

        // 나와의 거리 (Km)
        int distanceFromMeKm
) {
    public static RecommendedProfile from(Member member) {
        return new RecommendedProfile(
                member.getId(),
                member.getNickname(),
                member.getAge(),
                member.getGender(),
                0 // TODO 거리
        );
    }
}
