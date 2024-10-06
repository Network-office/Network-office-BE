package dev.office.networkoffice.gatheringUser.controller.response;

import dev.office.networkoffice.gatheringUser.domain.GatheringUser;

import java.util.List;

public record ApplicantUserDto(List<GatheringUser> userList) {
}
