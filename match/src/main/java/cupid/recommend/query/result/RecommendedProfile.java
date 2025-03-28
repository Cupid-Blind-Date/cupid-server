package cupid.recommend.query.result;

import cupid.common.value.Point;
import cupid.member.domain.Gender;
import cupid.member.domain.Member;
import jakarta.annotation.Nullable;

public record RecommendedProfile(
        Long id,
        String nickname,
        int age,
        Gender gender,

        // 나와의 거리 (Km)
        @Nullable Integer distanceFromMeKm
) {
    public static RecommendedProfile of(Member member, Point point) {
        return new RecommendedProfile(
                member.getId(),
                member.getNickname(),
                member.getAge(),
                member.getGender(),
                point.distance(member.getRecentActiveInfo().getPoint())
        );
    }
}
