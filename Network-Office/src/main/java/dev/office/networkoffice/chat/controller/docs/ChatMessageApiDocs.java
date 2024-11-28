package dev.office.networkoffice.chat.controller.docs;

import dev.office.networkoffice.chat.dto.response.ChatMessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "채팅 메세지", description = "채팅 메세지 관련 API")
public interface ChatMessageApiDocs {

    @Operation(summary = "해당 채팅방의 채팅 내역 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            )
    })
    List<ChatMessageResponse> getMessageList(Long roomId);
}
