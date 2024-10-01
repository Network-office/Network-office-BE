package dev.office.networkoffice.auth.dto;

public record PhoneVerificationDetails(
        String code,
        String phoneNumber
) {
}
