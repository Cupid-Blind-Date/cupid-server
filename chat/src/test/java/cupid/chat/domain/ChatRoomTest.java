package cupid.chat.domain;

import static cupid.chat.exception.ChatExceptionCode.NO_AUTHORITY_FOR_CHAT_ROOM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cupid.common.exception.ApplicationException;
import cupid.common.exception.ExceptionCode;
import cupid.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("채팅방 (ChatRoom) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ChatRoomTest extends UnitTest {

    @Test
    void 참여자_검증_성공() {
        // given
        ChatRoom chatRoom = new ChatRoom(2L, 1L);

        // when & then
        assertDoesNotThrow(() -> {
            chatRoom.validateParticipants(1L);
        });
        assertDoesNotThrow(() -> {
            chatRoom.validateParticipants(2L);
        });
    }

    @Test
    void 참여자_검증_실패() {
        // given
        ChatRoom chatRoom = new ChatRoom(2L, 1L);

        // when & then
        ExceptionCode code = assertThrows(ApplicationException.class, () -> {
            chatRoom.validateParticipants(3L);
        }).getCode();
        assertThat(code).isEqualTo(NO_AUTHORITY_FOR_CHAT_ROOM);
    }

    @Test
    void 상대방_id_조회() {
        // given
        ChatRoom chatRoom = new ChatRoom(2L, 1L);

        // when
        Long targetId1 = chatRoom.getTargetId(1L);
        Long targetId2 = chatRoom.getTargetId(2L);

        // then
        assertThat(targetId1).isEqualTo(2L);
        assertThat(targetId2).isEqualTo(1L);
    }
}
