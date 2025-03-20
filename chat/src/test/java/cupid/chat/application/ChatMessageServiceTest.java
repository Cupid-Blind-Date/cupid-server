package cupid.chat.application;

import static org.assertj.core.api.Assertions.assertThat;

import cupid.chat.application.command.SendChatMessageCommand;
import cupid.chat.domain.ChatMessage;
import cupid.chat.domain.ChatMessageType;
import cupid.support.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("ChatMessageService 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ChatMessageServiceTest extends ApplicationTest {

    @Autowired
    private ChatMessageService chatMessageService;

    @Test
    void 채팅_메세지를_저장한다() {
        // given
        SendChatMessageCommand command = new SendChatMessageCommand(
                1L,
                1L,
                2L,
                "hi",
                ChatMessageType.TEXT
        );

        // when
        ChatMessage chatMessage = chatMessageService.saveMessage(command);

        // then
        assertThat(chatMessage.getId()).isNotNull();
        assertThat(chatMessage.isRead()).isFalse();
    }
}
