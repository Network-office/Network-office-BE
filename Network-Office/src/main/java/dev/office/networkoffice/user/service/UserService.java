package dev.office.networkoffice.user.service;

import dev.office.networkoffice.global.exception.UnauthorizedException;
import dev.office.networkoffice.user.dto.UserInfo;
import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Transactional(readOnly = true)
    public UserInfo me() {
        Long userId = getUserId();
        User findUser = findUserById(userId);

        return responseUserInfo(findUser);
    }

    private Long getUserId() {
        Long userId = (Long) httpSession.getAttribute("userId");
        if (userId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        return userId;
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    private UserInfo responseUserInfo(User findUser) {
        return UserInfo.create(
                findUser.getId(),
                findUser.getOAuthInfo().getNickname(),
                findUser.getOAuthInfo().getSocialId(),
                findUser.getOAuthInfo().getSocialType().name(),
                findUser.getProfileImageUrl(),
                findUser.getPhoneNumber()
        );
    }
}
