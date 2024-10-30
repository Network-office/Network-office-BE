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

    @Embedded
    private Profile profile;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "is_verified")
    private boolean isVerified;

    private User(OAuthInfo oAuthInfo, Profile profile) {
        this.oAuthInfo = oAuthInfo;
        this.profile = profile;
        this.isVerified = false;
    }

    public static User createNewUserWithOAuth(OAuthInfo oAuthInfo, String profileImageUrl) {
        Assert.notNull(oAuthInfo, "OAuth 정보는 필수입니다.");
        Profile profile = Profile.createNewProfile(profileImageUrl);
        return new User(oAuthInfo, profile);
    }

    public void verifyPhoneNumber(String phoneNumber) {
        Assert.hasText(phoneNumber, "전화번호는 필수입니다.");
        Assert.isTrue(!isVerified, "이미 인증된 사용자입니다.");
        this.phoneNumber = phoneNumber;
        this.isVerified = true;
    }

    public void updateDisplayName(String displayName) {
        profile.updateDisplayName(displayName);
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        profile.updateProfileImageUrl(profileImageUrl);
    }

    public void updateDescription(String description) {
        profile.updateDescription(description);
    }
}
