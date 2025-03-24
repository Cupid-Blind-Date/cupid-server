package cupid.chat.presentation.rest;

import cupid.chat.application.ChatMessageService;
import cupid.chat.presentation.rest.response.ChatMessageResponse;
import cupid.common.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @PostMapping("/chat/{chatRoomId}/messages/read")
    public void readAllMessages(
            @PathVariable("chatRoomId") Long chatroomId,
            @Auth Long memberId
    ) {
        chatMessageService.readAllMessages(memberId, chatroomId);
    }

    @GetMapping("/chat/{chatRoomId}/messages")
    public Page<ChatMessageResponse> findMessages(
            @PathVariable("chatRoomId") Long chatroomId,
            @Auth Long memberId,
            @RequestParam(name = "lastReadId", required = false) Long lastReadId,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return chatMessageService.findPreviousMessages(lastReadId, memberId, chatroomId, size)
                .map(ChatMessageResponse::from);
    }
}
