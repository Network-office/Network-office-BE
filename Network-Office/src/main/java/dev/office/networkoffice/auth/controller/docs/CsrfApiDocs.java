package dev.office.networkoffice.auth.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "CSRF", description = "CSRF 토큰 관련 API")
public interface CsrfApiDocs {

    @Operation(summary = "CSRF 토큰 발급(XSRF-TOKEN 쿠키 없을 때 호출)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            )
    })
    void refreshCsrfToken();
}
