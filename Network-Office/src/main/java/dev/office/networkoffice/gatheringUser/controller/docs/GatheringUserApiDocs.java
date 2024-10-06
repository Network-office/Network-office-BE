package dev.office.networkoffice.gatheringUser.controller.docs;

import dev.office.networkoffice.gatheringUser.controller.request.DenyUserDto;
import dev.office.networkoffice.gatheringUser.controller.response.ApplicantUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "모임유저", description = "모임 유저 관련 API")
public interface GatheringUserApiDocs {
    @Operation(summary = "모임 신청")
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
    ResponseEntity<Void> applyGathering(Long userId, @PathVariable(name = "gatheringId") Long gatheringId);

    @Operation(summary = "모임 신청 목록 조회")
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
    ResponseEntity<ApplicantUserDto> getApplicantsInGatheringByHost(Long userId, @PathVariable(name = "gatheringId") Long gatheringId);

    @Operation(summary = "모임 거절")
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
    ResponseEntity<Void> denyApplicants(Long userId, @RequestBody DenyUserDto denyUserDto);

    @Operation(summary = "모임 추방")
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
    ResponseEntity<Void> deportsUser(Long userId, @RequestBody DenyUserDto denyUserDto);

    @Operation(summary = "모임 신청 수락")
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
    ResponseEntity<Void> approvedApplicants(Long userId, @PathVariable(name = "applicantId") Long applicantId);

    @Operation(summary = "모임에 해당 유저 차단")
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
    ResponseEntity<Void> blockUserInGathering(Long userId, @RequestBody DenyUserDto denyUserDto);


}
