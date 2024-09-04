package dev.office.networkoffice.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserInfo(
        @JsonProperty("id")
        Long id,
        @JsonProperty("nickname")
        String nickname,
        @JsonProperty("social_id")
        String socialId,
        @JsonProperty("social_type")
        String socialType,
        @JsonProperty("profile_image_url")
        String profileImageUrl,
        @JsonProperty("phone_number")
        String phoneNumber
) {
    public static UserInfo create(Long id,
                                  String nickname,
                                  String socialId,
                                  String socialType,
                                  String profileImageUrl,
                                  String phoneNumber) {
        return new UserInfo(id, nickname, socialId, socialType, profileImageUrl, phoneNumber);
    }
}
