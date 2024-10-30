package dev.office.networkoffice.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateDescription(
        @JsonProperty("description")
        String description
) {
}
