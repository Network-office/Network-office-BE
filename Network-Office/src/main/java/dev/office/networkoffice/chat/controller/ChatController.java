package dev.office.networkoffice.chat.controller;

import dev.office.networkoffice.chat.dto.request.ChatMessageRequest;
import dev.office.networkoffice.chat.dto.response.ChatMessageResponse;
import dev.office.networkoffice.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/rooms/{roomId}/send")
    @SendTo("/topic/public/rooms/{roomId}")
    public ChatMessageResponse sendMessage(
            Principal principal,
            @DestinationVariable Long roomId,
            @Payload ChatMessageRequest chatMessageRequest) {

         // 인증 없이 사용하는 버전
         return chatMessageService.createAnonymousChatMessage(chatMessageRequest, roomId);

        // 인증 후 사용하는 버전(오류)
        // Long userId = getUserId(principal);
        // return chatMessageService.createChatMessage(chatMessageRequest, userId, roomId);
    }

    private Long getUserId(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
