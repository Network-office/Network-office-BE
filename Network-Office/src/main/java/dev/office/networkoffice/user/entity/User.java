package dev.office.networkoffice.user.entity;

import dev.office.networkoffice.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Embedded
    private OAuthInfo oAuthInfo;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "is_verified")
    private boolean isVerified;

    private User(OAuthInfo oAuthInfo, String profileImageUrl) {
        this.oAuthInfo = oAuthInfo;
        this.profileImageUrl = profileImageUrl;
        this.isVerified = false;
    }

    public static User createNewUserWithOAuth(OAuthInfo oAuthInfo, String profileImageUrl) {
        Assert.notNull(oAuthInfo, "OAuth 정보는 필수입니다.");
        Assert.hasText(profileImageUrl, "프로필 이미지 URL은 필수입니다.");
        return new User(oAuthInfo, profileImageUrl);
    }

    public void verifyPhoneNumber(String phoneNumber) {
        Assert.hasText(phoneNumber, "전화번호는 필수입니다.");
        Assert.isTrue(!isVerified, "이미 인증된 사용자입니다.");
        this.phoneNumber = phoneNumber;
        this.isVerified = true;
    }
}
