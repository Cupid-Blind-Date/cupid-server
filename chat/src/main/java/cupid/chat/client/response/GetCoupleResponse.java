package cupid.chat.client.response;

public record GetCoupleResponse(
        Long coupleId,
        Long higherId,
        Long lowerId
) {
}
