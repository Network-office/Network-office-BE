package dev.office.networkoffice.chat.controller;

import dev.office.networkoffice.chat.controller.docs.ChatRoomApiDocs;
import dev.office.networkoffice.chat.dto.response.ChatRoomListResponse;
import dev.office.networkoffice.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomController implements ChatRoomApiDocs {

    private final ChatRoomService chatRoomService;

    @GetMapping("/{role}")
    public ChatRoomListResponse getMyChatRooms(Principal principal, @PathVariable String role) {
        Long userId = getUserId(principal);
        return chatRoomService.getChatRoomsByUserId(userId);
    }

    private Long getUserId(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
