package dev.office.networkoffice.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateProfileImage(
        @JsonProperty("profile_image_url")
        String profileImageUrl
) {
}
