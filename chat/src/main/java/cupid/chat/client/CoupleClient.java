package cupid.chat.client;

import cupid.chat.client.response.GetCoupleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class CoupleClient {

    private final RestClient matchServerClient;

    public GetCoupleResponse getCoupleById(
            Long coupleId
    ) {
        log.info("Try to get couple from match server. coupleId: {}", coupleId);
        GetCoupleResponse body = matchServerClient.get()
                .uri("/couples/" + coupleId)
                .retrieve()
                .body(GetCoupleResponse.class);
        log.info("Successfully get couple from match server. GetCoupleResponse: {}", body);
        return body;
    }
}
