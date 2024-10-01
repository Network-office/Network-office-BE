package dev.office.networkoffice.auth.controller.docs;

import dev.office.networkoffice.auth.dto.RequestVerificationCode;
import dev.office.networkoffice.auth.dto.PhoneVerificationDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Tag(name = "추가 인증", description = "사용자 추가 인증 관련 API")
public interface VerificationApiDocs {

    @Operation(summary = "휴대폰 인증 번호 발급 (생성)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자가 요청했을 때"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "인증번호를 생성할 권한이 없을 때(이미 인증된 사용자)"
            ),
    })
    PhoneVerificationDetails makeVerificationCode(Principal principal, @RequestBody RequestVerificationCode request);

    @Operation(summary = "휴대폰 인증 확인")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "요청이 정상적으로 처리되었을 때"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "인증번호를 찾을 수 없을 때"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자가 요청했을 때"
            )
    })
    String verifyPhoneNumber(Principal principal, @RequestBody RequestVerificationCode request);
}
