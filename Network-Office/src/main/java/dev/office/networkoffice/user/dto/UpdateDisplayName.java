package dev.office.networkoffice.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateDisplayName(
        @JsonProperty("display_name")
        String displayName
) {
}
