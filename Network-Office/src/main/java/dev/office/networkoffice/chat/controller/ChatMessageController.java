package dev.office.networkoffice.chat.controller;

import dev.office.networkoffice.chat.controller.docs.ChatMessageApiDocs;
import dev.office.networkoffice.chat.dto.response.ChatMessageResponse;
import dev.office.networkoffice.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/chat-rooms")
@RequiredArgsConstructor
public class ChatMessageController implements ChatMessageApiDocs {

    private final ChatMessageService chatMessageService;

    @GetMapping("{roomId}/messages")
    public List<ChatMessageResponse> getMessageList(@PathVariable Long roomId) {
        return chatMessageService.loadChatMessageList(roomId);
    }
}
