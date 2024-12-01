package dev.office.networkoffice.auth.controller;

import dev.office.networkoffice.auth.controller.docs.VerificationApiDocs;
import dev.office.networkoffice.auth.dto.RequestVerificationCode;
import dev.office.networkoffice.auth.dto.PhoneVerificationDetails;
import dev.office.networkoffice.auth.service.VerificationService;
import dev.office.networkoffice.global.annotation.CurrentUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/verification")
public class VerificationController implements VerificationApiDocs {

    private final VerificationService verificationService;

    @PostMapping("phone/code")
    public PhoneVerificationDetails makeVerificationCode(@CurrentUserId Long userId,
                                                         @RequestBody RequestVerificationCode request) {
        return verificationService.generateVerificationCode(userId, request.phoneNumber());
    }

    @PostMapping("phone/verify")
    public String verifyPhoneNumber(@CurrentUserId Long userId,
                                    @RequestBody RequestVerificationCode request) {
        verificationService.verifyPhoneNumber(userId, request.phoneNumber());
        return "인증이 완료되었습니다.";
    }
}
