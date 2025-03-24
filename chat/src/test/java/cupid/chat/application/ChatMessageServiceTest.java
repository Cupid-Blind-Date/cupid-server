package cupid.chat.application;

import static cupid.chat.domain.ChatMessageType.TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cupid.chat.application.command.SendChatMessageCommand;
import cupid.chat.application.result.ChatMessageDto;
import cupid.chat.domain.ChatMessage;
import cupid.chat.domain.ChatMessageRepository;
import cupid.chat.domain.ChatRoom;
import cupid.chat.domain.ChatRoomRepository;
import cupid.support.ApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

@DisplayName("ChatMessageService 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class ChatMessageServiceTest extends ApplicationTest {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private Long chatRoomId = 1L;
    private Long 동훈_ID = 1L;
    private Long 윈터_ID = 2L;

    @Test
    void 채팅_메시지를_저장한다() {
        // given
        SendChatMessageCommand command = new SendChatMessageCommand(
                1L,
                1L,
                2L,
                "hi",
                TEXT
        );

        // when
        ChatMessage chatMessage = chatMessageService.saveMessage(command);

        // then
        assertThat(chatMessage.getId()).isNotNull();
        assertThat(chatMessage.isRead()).isFalse();
    }

    @Nested
    class 메시지_읽음_처리_시 {

        @BeforeEach
        void setUp() {
            chatRoomId = chatRoomRepository.save(new ChatRoom(윈터_ID, 동훈_ID)).getId();
        }

        @Test
        void 채팅방에_대한_권한이_없으면_예외() {
            assertDoesNotThrow(() -> {
                chatMessageService.readAllMessage(윈터_ID, chatRoomId);
            });
            assertDoesNotThrow(() -> {
                chatMessageService.readAllMessage(동훈_ID, chatRoomId);
            });
            assertThatThrownBy(() -> {
                chatMessageService.readAllMessage(3L, chatRoomId);
            });
        }

        @Test
        void 내가_받은_모든_메시지가_읽음_처리된다() {
            // given
            chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 윈터_ID, 동훈_ID, "안녕하세요~", TEXT
            ));
            chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 동훈_ID, 윈터_ID, "안녕하세요!", TEXT
            ));
            chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 동훈_ID, 윈터_ID, "이쁘세요", TEXT
            ));
            chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 동훈_ID, 윈터_ID, "뭐하세요?", TEXT
            ));
            chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 동훈_ID, 윈터_ID, "바쁘신가요 ㅠㅠ", TEXT
            ));

            // when
            chatMessageService.readAllMessage(윈터_ID, chatRoomId);

            // then
            assertThat(chatMessageRepository.findAll())
                    .extracting(ChatMessage::isRead)
                    .containsExactly(false, true, true, true, true);
        }
    }

    @Nested
    class 이전_채팅기록_조회_시 {

        @BeforeEach
        void setUp() {
            chatRoomId = chatRoomRepository.save(new ChatRoom(윈터_ID, 동훈_ID)).getId();
        }

        @Test
        void 채팅방에_대한_권한이_없으면_예외() {
            assertDoesNotThrow(() -> {
                chatMessageService.findPreviousMessages(null, 윈터_ID, chatRoomId, 2);
            });
            assertDoesNotThrow(() -> {
                chatMessageService.findPreviousMessages(1L, 동훈_ID, chatRoomId, 2);
            });
            assertThatThrownBy(() -> {
                chatMessageService.findPreviousMessages(null, 3L, chatRoomId, 2);
            });
        }

        @Test
        void id_가_없으면_최근_메시지부터_읽는다() {
            // given
            chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 윈터_ID, 동훈_ID, "안녕하세요~", TEXT
            ));
            chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 동훈_ID, 윈터_ID, "안녕하세요!", TEXT
            ));
            chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 동훈_ID, 윈터_ID, "이쁘세요", TEXT
            ));
            chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 동훈_ID, 윈터_ID, "뭐하세요?", TEXT
            ));
            chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 동훈_ID, 윈터_ID, "바쁘신가요 ㅠㅠ", TEXT
            ));

            // when
            Page<ChatMessageDto> result = chatMessageService.findPreviousMessages(null, 윈터_ID, chatRoomId, 2);

            // then
            assertThat(result.hasNext()).isTrue();
            assertThat(result.getContent())
                    .extracting(ChatMessageDto::message)
                    .containsExactly("바쁘신가요 ㅠㅠ", "뭐하세요?");
        }

        @Test
        void 특정_메시지_이전_메시지를_읽는다() {
            // given
            chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 윈터_ID, 동훈_ID, "안녕하세요~", TEXT
            ));
            chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 동훈_ID, 윈터_ID, "안녕하세요!", TEXT
            ));
            chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 동훈_ID, 윈터_ID, "이쁘세요", TEXT
            ));
            ChatMessage chatMessage = chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 동훈_ID, 윈터_ID, "뭐하세요?", TEXT
            ));
            chatMessageService.saveMessage(new SendChatMessageCommand(
                    chatRoomId, 동훈_ID, 윈터_ID, "바쁘신가요 ㅠㅠ", TEXT
            ));

            // when
            Page<ChatMessageDto> result = chatMessageService.findPreviousMessages(
                    chatMessage.getId(),
                    동훈_ID,
                    chatRoomId,
                    2
            );

            // then
            assertThat(result.hasNext()).isTrue();
            assertThat(result.getContent())
                    .extracting(ChatMessageDto::message)
                    .containsExactly("이쁘세요", "안녕하세요!");
        }
    }
}
