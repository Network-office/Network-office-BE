package dev.office.networkoffice.gatheringUser.controller.dto.response;

import dev.office.networkoffice.gatheringUser.domain.GatheringUser;

import java.util.List;

public record ApplicantUserDto(
        List<GatheringUser> userList
) {
}
