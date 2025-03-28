package cupid.common.value;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("좌표 (Point) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PointTest {

    @Test
    void 거리_계산_정확성_테스트() {
        // 서울역 (37.5563, 126.9723)
        Point p1 = new Point(37.5563, 126.9723);
        // 강남역 (37.4979, 127.0276)
        Point p2 = new Point(37.4979, 127.0276);

        Integer distance = p1.distance(p2);

        // 대략 8 ~ 9km 정도, 소수점 반올림되었기 때문에 오차 범위 체크
        assertThat(distance).isBetween(8, 11);
    }

    @Test
    void 널_좌표_테스트() {
        Point valid = new Point(37.0, 127.0);
        Point invalid = new Point(null, null);

        assertThat(valid.distance(null)).isNull();
        assertThat(valid.distance(invalid)).isNull();
        assertThat(invalid.distance(valid)).isNull();
    }

    @Test
    void 동일_좌표_테스트() {
        Point p = new Point(37.1234, 127.5678);
        assertThat(p.distance(p)).isEqualTo(0);
    }
}
