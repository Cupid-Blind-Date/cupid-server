package cupid.chat.presentation.rest;

import cupid.chat.application.ChatMessageService;
import cupid.chat.presentation.rest.response.ChatMessageResponse;
import cupid.common.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/chat/{chatroomId}/messages")
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @PostMapping("/read")
    public void readAllMessages(
            @PathVariable("chatRoomId") Long chatroomId,
            @Auth Long memberId
    ) {
        chatMessageService.readAllMessages(memberId, chatroomId);
    }

    @GetMapping
    public Page<ChatMessageResponse> findMessages(
            @PathVariable("chatRoomId") Long chatroomId,
            @Auth Long memberId,
            @RequestParam(name = "lastReadId") Long lastReadId,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return chatMessageService.findPreviousMessages(lastReadId, memberId, chatroomId, size)
                .map(ChatMessageResponse::from);
    }
}
