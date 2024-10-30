package dev.office.networkoffice.user.controller;

import dev.office.networkoffice.user.controller.docs.UserApiDocs;
import dev.office.networkoffice.user.dto.UpdateDescription;
import dev.office.networkoffice.user.dto.UpdateDisplayName;
import dev.office.networkoffice.user.dto.UpdateProfileImage;
import dev.office.networkoffice.user.dto.UserInfo;
import dev.office.networkoffice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController implements UserApiDocs {

    private final UserService userService;

    @GetMapping("profile")
    public UserInfo profile(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        return userService.profile(userId);
    }

    @PatchMapping("profile/display-name")
    public void updateDisplayName(Principal principal,
                                  @RequestBody UpdateDisplayName request) {
        Long userId = Long.parseLong(principal.getName());
        userService.updateDisplayName(userId, request);
    }

    @PatchMapping("profile/image")
    public void updateProfileImage(Principal principal,
                                   @RequestBody UpdateProfileImage request) {
        Long userId = Long.parseLong(principal.getName());
        userService.updateProfileImage(userId, request);
    }

    @PatchMapping("profile/description")
    public void updateDescription(Principal principal, UpdateDescription request) {
        Long userId = Long.parseLong(principal.getName());
        userService.updateDescription(userId, request);
    }
}
