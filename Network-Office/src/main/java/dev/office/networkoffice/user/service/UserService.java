package dev.office.networkoffice.user.service;

import dev.office.networkoffice.user.dto.UserInfo;
import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfo profile(Long userId) {
        User findUser = findUserById(userId);
        return responseUserInfo(findUser);
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
