package cupid.recommend.cache;

import static org.assertj.core.api.Assertions.assertThat;

import cupid.support.ApplicationTest;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("RecommendCacheManager 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class RecommendCacheManagerTest extends ApplicationTest {

    @Autowired
    private RecommendCacheManager recommendCacheManager;

    @BeforeEach
    void setUp() {
        recommendCacheManager.deleteAll();
    }

    @AfterEach
    void tearDown() {
        recommendCacheManager.deleteAll();
    }

    @Test
    void 회원_아이디와_추천_회원_아이디_목록을_캐싱() {
        // when
        recommendCacheManager.update(1L, List.of(2L, 3L, 4L));

        // then
        List<Long> ids = recommendCacheManager.get(1L);
        assertThat(ids).containsExactly(2L, 3L, 4L);
    }

    @Test
    void 추천_회원_아이디_목록을_업데이트() {
        // given
        recommendCacheManager.update(1L, List.of(1L, 2L));

        // when
        recommendCacheManager.update(1L, List.of(2L, 3L, 4L));

        // then
        List<Long> ids = recommendCacheManager.get(1L);
        assertThat(ids).containsExactly(2L, 3L, 4L);
    }

    @Test
    void 없는_값_조회시_EMPTY() {
        // when
        List<Long> result = recommendCacheManager.get(10L);

        // then
        assertThat(result).isEmpty();
    }
}
