package dev.office.networkoffice.auth.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "로그인", description = "로그인 관련 API")
public interface LoginApiDocs {

    @Operation(summary = "카카오 소셜 로그인")
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
    void kakaoLogin(String code);
}
