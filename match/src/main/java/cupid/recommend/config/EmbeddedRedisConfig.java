package cupid.recommend.config;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import redis.embedded.RedisServer;

@RequiredArgsConstructor
@Profile({"local", "test"})
@Configuration
public class EmbeddedRedisConfig {

    private final RedisProperty redisProperty;
    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() {
        int redisPort = redisProperty.port();
        if (isArmMac()) {
            redisServer = new RedisServer(Objects.requireNonNull(getRedisFileForArcMac()), redisPort);
        }
        if (!isArmMac()) {
            redisServer = new RedisServer(redisPort);
        }
        try {
            redisServer.start();
        } catch (Exception e) {
        }
    }

    private boolean isArmMac() {
        return Objects.equals(System.getProperty("os.arch"), "aarch64") &&
               Objects.equals(System.getProperty("os.name"), "Mac OS X");
    }

    private File getRedisFileForArcMac() {
        try {
            return new ClassPathResource("redis/redis-server-7.2.3-mac-arm64").getFile();
        } catch (Exception e) {
            throw new RuntimeException("embedded redis can not start");
        }
    }
}
