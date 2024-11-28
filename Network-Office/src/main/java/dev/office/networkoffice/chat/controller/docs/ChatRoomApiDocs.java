package dev.office.networkoffice.chat.controller.docs;

import dev.office.networkoffice.chat.dto.response.ChatRoomListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;

@Tag(name = "채팅방", description = "채팅방 관련 API")
public interface ChatRoomApiDocs {

    @Operation(summary = "호스트 또는 참여자로 있는 채팅방 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            )
    })
    ChatRoomListResponse getMyChatRooms(Principal principal, String role);
}
