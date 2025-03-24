package cupid.chat.presentation.websocket.channel;

public class ChattingChannelConfig {

    // 예외 메시지 읽는 채널
    public static class ErrorChannel {
        // SendToUser 시에는 반드시 /queue 로 시작해야 함.
        // https://stackoverflow.com/questions/22367223/sending-message-to-specific-user-on-spring-websocket
        public static final String SUB = "/queue/errors";
    }

    // 채팅 메시지 pub/sub
    public static class SendChatChannel {
        public static final String SUB = "/sub/chat/";
        public static final String PUB = "/pub/chat/";
        public static final String PUB_MESSAGE_MAPPING_URI = "/chat/{roomId}";

        public static boolean accept(String destination) {
            return destination.startsWith(SendChatChannel.PUB)
                   || destination.startsWith(SendChatChannel.SUB);
        }
    }

    // 채팅 메시지의 읽음 정보 pub/sub (실시간 읽음 처리에 활용)
    public static class ReadChatChannel {
        public static final String SUB = "/sub/read-chat/";
        public static final String PUB = "/pub/read-chat/";
        public static final String PUB_MESSAGE_MAPPING_URI = "/read-chat/{roomId}";

        public static boolean accept(String destination) {
            return destination.startsWith(ReadChatChannel.PUB)
                   || destination.startsWith(ReadChatChannel.SUB);
        }
    }
}
