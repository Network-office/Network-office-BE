package dev.office.networkoffice.user.controller.docs;

import dev.office.networkoffice.user.dto.UpdateDescription;
import dev.office.networkoffice.user.dto.UpdateDisplayName;
import dev.office.networkoffice.user.dto.UpdateProfileImage;
import dev.office.networkoffice.user.dto.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;

@Tag(name = "사용자", description = "사용자 관련 API")
public interface UserApiDocs {

    @Operation(summary = "내 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자가 요청했을 때"
            )
    })
    UserInfo profile(Principal principal);

    @Operation(summary = "내 닉네임 변경")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청이 들어왔을 때"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자가 요청했을 때"
            )
    })
    void updateDisplayName(Principal principal, UpdateDisplayName request);

    @Operation(summary = "내 프로필 이미지 변경")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청이 들어왔을 때"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자가 요청했을 때"
            )
    })
    void updateProfileImage(Principal principal, UpdateProfileImage request);

    @Operation(summary = "내 상태 메시지 변경")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청이 들어왔을 때"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자가 요청했을 때"
            )
    })
    void updateDescription(Principal principal, UpdateDescription request);
}
