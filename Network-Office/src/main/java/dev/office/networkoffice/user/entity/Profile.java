package dev.office.networkoffice.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    private static final String INIT_EMPTY_VALUE = null;

    @Column(name = "display_name", unique = true)
    private String displayName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "description")
    private String description;

    private Profile(String displayName, String imageUrl, String description) {
        this.displayName = displayName;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public static Profile createNewProfile(String profileImageUrl) {
        Assert.hasText(profileImageUrl, "프로필 이미지 URL은 필수입니다.");
        return new Profile(INIT_EMPTY_VALUE, profileImageUrl, INIT_EMPTY_VALUE);
    }

    protected void updateDisplayName(String displayName) {
        Assert.hasText(displayName, "닉네임은 필수입니다.");
        if (displayName.length() < 2 || displayName.length() > 20) {
            throw new IllegalArgumentException("닉네임은 2자 이상 20자 이하여야 합니다.");
        }
        this.displayName = displayName;
    }

    protected void updateProfileImageUrl(String profileImageUrl) {
        Assert.hasText(profileImageUrl, "프로필 이미지 URL은 필수입니다.");
        this.imageUrl = profileImageUrl;
    }

    protected void updateDescription(String description) {
        Assert.notNull(description, "자기소개는 필수입니다.");
        this.description = description;
    }
}
