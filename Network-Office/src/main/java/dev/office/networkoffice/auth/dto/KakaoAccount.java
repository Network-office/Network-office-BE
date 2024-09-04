package dev.office.networkoffice.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccount(
        @JsonProperty("profile")
        KakaoProfile profile
) {
}
