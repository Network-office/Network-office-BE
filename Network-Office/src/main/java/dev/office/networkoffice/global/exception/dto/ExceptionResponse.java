package dev.office.networkoffice.global.exception.dto;

public record ExceptionResponse(String message) {

    public static ExceptionResponse of(String message) {
        return new ExceptionResponse(message);
    }

    @Override
    public String toString() {
        return "{\"message\":\"" + message + "\"}";
    }
}
