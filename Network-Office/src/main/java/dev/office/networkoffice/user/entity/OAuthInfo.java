package dev.office.networkoffice.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthInfo {

    @Column(name = "social_id")
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type")
    private SocialType socialType;

    @Column(name = "nickname")
    private String nickname;

    private OAuthInfo(String socialId,
                      SocialType socialType,
                      String nickname) {
        this.socialId = socialId;
        this.socialType = socialType;
        this.nickname = nickname;
    }

    public static OAuthInfo createForKakao(String socialId, String nickname) {
        return new OAuthInfo(socialId, SocialType.KAKAO, nickname);
    }
}
