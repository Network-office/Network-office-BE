package dev.office.networkoffice.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoErrorResponse(
        @JsonProperty("error")
        String error,
        @JsonProperty("error_description")
        String errorDescription,
        @JsonProperty("error_code")
        String errorCode
) {
    private static final String ERROR_MESSAGE_FORMAT = "{ %s(%s): %s }";

    @Override
    public String toString() {
        return String.format(ERROR_MESSAGE_FORMAT, error, errorCode, errorDescription);
    }
}
