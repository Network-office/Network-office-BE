package dev.office.networkoffice.auth.service;

import dev.office.networkoffice.auth.client.KakaoOAuthClient;
import dev.office.networkoffice.auth.dto.KakaoProfile;
import dev.office.networkoffice.auth.dto.KakaoUserResponse;
import dev.office.networkoffice.user.entity.OAuthInfo;
import dev.office.networkoffice.user.entity.SocialType;
import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Transactional
    public void kakaoLogin(String code) {
        KakaoUserResponse kakaoUserResponse = kakaoOAuthClient.requestUserInformation(code);
        String socialId = kakaoUserResponse.id();

        boolean isExistingUser = userRepository.existsBySocialLogin(socialId, SocialType.KAKAO);
        if (isExistingUser) {
            registerUserSession(socialId);
            return;
        }

        signup(kakaoUserResponse);
        registerUserSession(socialId);
    }

    private void signup(KakaoUserResponse kakaoUserResponse) {
        OAuthInfo oAuthKakaoInfo = kakaoUserResponse.toOAuthInfo();
        String profileImage = selectProfileImage(kakaoUserResponse);

        userRepository.save(User.create(oAuthKakaoInfo, profileImage));
    }

    private String selectProfileImage(KakaoUserResponse kakaoUserResponse) {
        KakaoProfile profile = getProfile(kakaoUserResponse);
        boolean isDefaultImage = profile.isDefaultImage();
        String profileImage = profile.profileImageUrl();

        return isDefaultImage ? "defaultProfile.png" : profileImage;
    }

    private KakaoProfile getProfile(KakaoUserResponse kakaoUserResponse) {
        return kakaoUserResponse.kakaoAccount().profile();
    }

    private void registerUserSession(String socialId) {
        User user = findUserBySocialId(socialId);
        httpSession.setAttribute("userId", user.getId());
    }

    private User findUserBySocialId(String socialId) {
        return userRepository.findBySocialLogin(socialId, SocialType.KAKAO)
                .orElseThrow(() -> new RuntimeException("없는 사용자입니다."));
    }
}
