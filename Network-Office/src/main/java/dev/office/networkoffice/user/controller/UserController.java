package dev.office.networkoffice.user.controller;

import dev.office.networkoffice.global.annotation.CurrentUserId;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController implements UserApiDocs {

    private final UserService userService;

    @GetMapping("profile")
    public UserInfo profile(@CurrentUserId Long userId) {
        return userService.profile(userId);
    }

    @PatchMapping("profile/display-name")
    public void updateDisplayName(@CurrentUserId Long userId, @RequestBody UpdateDisplayName request) {
        userService.updateDisplayName(userId, request);
    }

    @PatchMapping("profile/image")
    public void updateProfileImage(@CurrentUserId Long userId, @RequestBody UpdateProfileImage request) {
        userService.updateProfileImage(userId, request);
    }

    @PatchMapping("profile/description")
    public void updateDescription(@CurrentUserId Long userId, UpdateDescription request) {
        userService.updateDescription(userId, request);
    }
}
