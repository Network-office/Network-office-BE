package dev.office.networkoffice.gatheringUser.controller.request;

public record DenyUserDto(
        Long applicantId,
        String reason
) {
}
