package dev.office.networkoffice.gatheringUser.controller.request;

import dev.office.networkoffice.gatheringUser.domain.GatheringUserStatus;

public record ChangeStatusDto(
        GatheringUserStatus status,
        String reason
) {
}
