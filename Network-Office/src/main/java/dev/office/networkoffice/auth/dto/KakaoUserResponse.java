package dev.office.networkoffice.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.office.networkoffice.user.entity.OAuthInfo;

public record KakaoUserResponse(
        @JsonProperty("id")
        String id,
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {
    public OAuthInfo toOAuthInfo() {
        String nickname = kakaoAccount.profile().nickname();
        return OAuthInfo.createForKakao(id, nickname);
    }
}
