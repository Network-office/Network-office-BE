package dev.office.networkoffice.feed.controller.docs;

import dev.office.networkoffice.feed.dto.request.CommentWrite;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;

@Tag(name = "댓글", description = "피드 댓글 관련 API")
public interface CommentApiDocs {

    @Operation(summary = "피드에 댓글 작성")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            )
    })
    void writeComment(Principal principal, CommentWrite commentWrite);

    @Operation(summary = "피드에 작성한 댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            )
    })
    void removeComment(Principal principal, Long commentId);
}
