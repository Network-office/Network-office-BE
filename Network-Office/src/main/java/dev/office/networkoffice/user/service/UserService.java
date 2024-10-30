package dev.office.networkoffice.user.service;

import dev.office.networkoffice.user.dto.UpdateDescription;
import dev.office.networkoffice.user.dto.UpdateDisplayName;
import dev.office.networkoffice.user.dto.UpdateProfileImage;
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
                findUser.getProfile().getDisplayName(),
                findUser.getOAuthInfo().getSocialId(),
                findUser.getOAuthInfo().getSocialType().name(),
                findUser.getProfile().getImageUrl(),
                findUser.getProfile().getDescription(),
                findUser.getPhoneNumber()
        );
    }

    @Transactional
    public void updateDisplayName(Long userId, UpdateDisplayName request) {
        User findUser = findUserById(userId);
        checkDuplicateDisplayName(request);
        findUser.updateDisplayName(request.displayName());
    }

    private void checkDuplicateDisplayName(UpdateDisplayName request) {
        if (userRepository.existsByProfileDisplayName(request.displayName())) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }
    }

    @Transactional
    public void updateProfileImage(Long userId, UpdateProfileImage request) {
        User findUser = findUserById(userId);
        findUser.updateProfileImageUrl(request.profileImageUrl());
    }

    @Transactional
    public void updateDescription(Long userId, UpdateDescription request) {
        User findUser = findUserById(userId);
        findUser.updateDescription(request.description());
    }
}
