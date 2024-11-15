package dev.office.networkoffice.feed.controller.docs;

import dev.office.networkoffice.feed.dto.response.FeedDetails;
import dev.office.networkoffice.feed.dto.response.FeedInfo;
import dev.office.networkoffice.feed.dto.request.FeedWrite;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@Tag(name = "피드", description = "피드 관련 API")
public interface FeedApiDocs {

    @Operation(summary = "새로운 피드 작성")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            )
    })
    void writeFeed(Principal principal, FeedWrite feedWrite);

    @Operation(summary = "피드 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            )
    })
    Slice<FeedInfo> viewFeeds(Pageable pageable);

    @Operation(summary = "특정 피드 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            )
    })
    FeedDetails viewFeedDetail(Principal principal, Long feedId);
}
