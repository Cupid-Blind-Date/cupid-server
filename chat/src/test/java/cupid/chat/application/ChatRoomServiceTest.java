package cupid.chat.application;

import static org.assertj.core.api.Assertions.assertThat;

import cupid.chat.domain.ChatRoomRepository;
import cupid.support.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("ChatRoomService 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ChatRoomServiceTest extends ApplicationTest {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    void 채팅방을_생성한다() {
        // when
        Long id = chatRoomService.createChatRoom(2L, 1L);

        // then
        assertThat(chatRoomRepository.findById(id)).isPresent();
    }
}
