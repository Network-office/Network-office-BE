package dev.office.networkoffice.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserInfo(
        @JsonProperty("id")
        Long id,
        @JsonProperty("display_name")
        String displayName,
        @JsonProperty("social_id")
        String socialId,
        @JsonProperty("social_type")
        String socialType,
        @JsonProperty("profile_image_url")
        String profileImageUrl,
        @JsonProperty("description")
        String description,
        @JsonProperty("phone_number")
        String phoneNumber
) {
    public static UserInfo create(Long id,
                                  String displayName,
                                  String socialId,
                                  String socialType,
                                  String profileImageUrl,
                                  String description,
                                  String phoneNumber) {
        return new UserInfo(id, displayName, socialId, socialType, profileImageUrl, description, phoneNumber);
    }
}
