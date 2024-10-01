package dev.office.networkoffice.auth.controller;

import dev.office.networkoffice.auth.controller.docs.VerificationApiDocs;
import dev.office.networkoffice.auth.dto.RequestVerificationCode;
import dev.office.networkoffice.auth.dto.PhoneVerificationDetails;
import dev.office.networkoffice.auth.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/verification")
public class VerificationController implements VerificationApiDocs {

    private final VerificationService verificationService;

    @PostMapping("phone/code")
    public PhoneVerificationDetails makeVerificationCode(Principal principal,
                                                         @RequestBody RequestVerificationCode request) {
        Long userId = Long.parseLong(principal.getName());
        return verificationService.generateVerificationCode(userId, request.phoneNumber());
    }

    @PostMapping("phone/verify")
    public String verifyPhoneNumber(Principal principal,
                                    @RequestBody RequestVerificationCode request) {
        Long userId = Long.parseLong(principal.getName());
        verificationService.verifyPhoneNumber(userId, request.phoneNumber());
        return "인증이 완료되었습니다.";
    }
}
