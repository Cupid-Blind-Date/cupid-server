package cupid.couple.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("화살 (Arrow) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ArrowTest {

    @ParameterizedTest(name = "매치 성공시 회원의 아이디가 각각 {0}과 {1} 이라면, higherId={2}, lowerId={3} 으로 Couple 생성")
    @CsvSource(value = {
            "1,10,10,1",
            "10,1,10,1",
            "5,123,123,5",
            "123,5,123,5",
    }, delimiterString = ",")
    void createCoupleWithHigherAndLowerId(
            Long member1Id,
            Long member2Id,
            Long expectedHigherId,
            Long expectedLowerId
    ) {
        // given
        Arrow arrow = new Arrow(member1Id, member2Id, LikeType.LIKE);

        // when
        Couple couple = arrow.match();

        // then
        assertThat(couple.getHigherId()).isEqualTo(expectedHigherId);
        assertThat(couple.getLowerId()).isEqualTo(expectedLowerId);
    }
}
