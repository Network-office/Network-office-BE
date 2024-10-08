package dev.office.networkoffice.gathering.controller.docs;

import dev.office.networkoffice.gathering.controller.dto.request.GatheringCancelDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringDto;
import dev.office.networkoffice.gathering.controller.dto.request.GatheringSuccessDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringClosedResponse;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringListResponseDto;
import dev.office.networkoffice.gathering.controller.dto.response.GatheringResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;

@Tag(name = "모임", description = "모임 관련 API")
public interface GatheringApiDocs {

    @Operation(summary = "모임 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 코드가 전달되었을 때"
            )
    })
    GatheringListResponseDto findGatheringList(Principal principal, String si, String dong, String gu);

    @Operation(summary = "모임 생성")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 코드가 전달되었을 때"
            )
    })
    GatheringResponseDto createGathering(Principal principal, GatheringDto gatheringDto);

    @Operation(summary = "모임 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 코드가 전달되었을 때"
            )
    })
    GatheringResponseDto modifyGatheringByHost(Principal principal,
                                               Long gatheringId,
                                               GatheringDto gatheringDto);

    @Operation(summary = "모임 파토")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 코드가 전달되었을 때"
            )
    })
    GatheringClosedResponse cancelGatheringByHost(Principal principal, GatheringCancelDto cancelDto);

    @Operation(summary = "모임 성공적 종료")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 코드가 전달되었을 때"
            )
    })
    GatheringClosedResponse successGatheringByHost(Principal principal, GatheringSuccessDto successDto);
}
