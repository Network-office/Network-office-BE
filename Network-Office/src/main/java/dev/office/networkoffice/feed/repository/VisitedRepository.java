package dev.office.networkoffice.feed.repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VisitedRepository {

    private static final String VISITED_NAMESPACE = "feed:visited:";
    private static final String KEY_FORMAT = VISITED_NAMESPACE + "%d:%d";
    private static final long EXPIRATION_SECONDS = 24 * 60 * 60;

    private final RedisTemplate<String, Long> redisTemplate;

    /**
     * 특정 사용자의 특정 피드 조회 기록을 저장합니다.
     * @param userId 사용자 ID
     * @param feedId 피드 ID
     */
    public void save(Long userId, Long feedId) {
        String key = generateKey(userId, feedId);
        redisTemplate.opsForValue()
                .set(key, feedId, EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 특정 사용자가 특정 피드를 24시간 내에 조회했는지 확인합니다.
     * @param userId 사용자 ID
     * @param feedId 피드 ID
     * @return 24시간 내 조회 여부
     */
    public boolean isFeedVisited(Long userId, Long feedId) {
        String key = generateKey(userId, feedId);
        return Boolean.TRUE
                .equals(redisTemplate.hasKey(key));
    }

    private String generateKey(Long userId, Long feedId) {
        return String.format(KEY_FORMAT, userId, feedId);
    }
}
