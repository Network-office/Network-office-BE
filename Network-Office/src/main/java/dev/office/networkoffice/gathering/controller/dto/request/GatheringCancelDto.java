package dev.office.networkoffice.gathering.controller.dto.request;

import dev.office.networkoffice.gathering.domain.ReasonForCanceled;

public record GatheringCancelDto(Long gatheringId, ReasonForCanceled reason) {
}
