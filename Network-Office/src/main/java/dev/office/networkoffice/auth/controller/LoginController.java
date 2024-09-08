package dev.office.networkoffice.auth.controller;

import dev.office.networkoffice.auth.controller.docs.LoginApiDocs;
import dev.office.networkoffice.auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/login")
public class LoginController implements LoginApiDocs {

    private final LoginService loginService;

    @PostMapping("oauth/kakao")
    public void kakaoLogin(@RequestParam String code) {
        loginService.kakaoLogin(code);
    }
}
