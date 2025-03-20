package cupid.chat.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("client")
public record ClientProperties(
        String mathServerInternalUrl
) {
}
