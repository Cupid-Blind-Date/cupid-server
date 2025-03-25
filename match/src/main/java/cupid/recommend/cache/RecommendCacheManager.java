package cupid.recommend.cache;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RecommendCacheManager {

    private static final String CACHE_KEY_PREFIX = "recommend:";
    private static final Duration CACHE_TTL = Duration.ofHours(24);

    private final RedisTemplate<String, MemberIdListWrapper> redisTemplate;

    public List<Long> get(Long memberId) {
        ValueOperations<String, MemberIdListWrapper> ops = redisTemplate.opsForValue();
        String key = getKey(memberId);
        MemberIdListWrapper wrapper = ops.get(key);
        if (wrapper == null) {
            log.info("Miss recommend cache. memberId: {}", memberId);
            return Collections.emptyList();
        }
        return wrapper.ids();
    }

    public void update(Long memberId, List<Long> ids) {
        log.info("Try to update recommend cache. memberId: {}", memberId);
        ValueOperations<String, MemberIdListWrapper> ops = redisTemplate.opsForValue();
        String key = getKey(memberId);
        MemberIdListWrapper wrapper = new MemberIdListWrapper(ids);
        ops.set(key, wrapper, CACHE_TTL);
        log.info("Successfully update recommend cache. memberId: {}", memberId);
    }

    public void deleteAll() {
        String pattern = CACHE_KEY_PREFIX + "*";
        try {
            log.info("Try to delete all recommend caches");
            // 모든 recommend:* 키 가져오기
            var keys = redisTemplate.keys(pattern);
            if (!keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("Successfully deleted {} recommend cache entries", keys.size());
            } else {
                log.info("No recommend cache entries found to delete.");
            }
        } catch (Exception e) {
            log.error("Failed to delete recommend caches", e);
        }
    }

    private String getKey(Long memberId) {
        return CACHE_KEY_PREFIX + memberId;
    }

    // redis 에서 List 사용 시 역직렬화 오류 해결
    record MemberIdListWrapper(
            List<Long> ids
    ) {
    }
}
