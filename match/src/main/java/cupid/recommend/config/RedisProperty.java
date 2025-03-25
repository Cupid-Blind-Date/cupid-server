package cupid.recommend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("redis")
public record RedisProperty(
        String host,
        int port
) {
}
