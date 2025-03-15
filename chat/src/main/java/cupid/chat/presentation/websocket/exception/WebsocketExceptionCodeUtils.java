package cupid.chat.presentation.websocket.exception;

import org.springframework.http.HttpStatus;

public class WebsocketExceptionCodeUtils {

    /**
     * 웹소켓 예외코드 4000~4999: 개인용으로 사용하면 되며, WebSocket 애플리케이션 간의 사전 계약에 의해 사용 가능.
     */
    public static String createCode(HttpStatus status) {
        return String.valueOf(4000 + status.value());
    }
}
