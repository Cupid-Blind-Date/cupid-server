package cupid.chat.presentation.websocket.exception;

import jakarta.annotation.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;

public interface StompExceptionHandler {

    boolean canHandle(Throwable ex);

    /**
     * <p>예외를 처리하는 메서드. WebSocket 프로토콜에 의해 ERROR 커맨드를 사용하면, client와의 연결을 반드시 끊어야 한다.</p>
     * <p>이를 원치 않는 경우, {@link StompCommand#ERROR}를 사용하여 Accessor를 설정해서는 안 된다. </p>
     */
    @Nullable
    Message<byte[]> handle(@Nullable Message<byte[]> clientMessage, Throwable ex);
}
