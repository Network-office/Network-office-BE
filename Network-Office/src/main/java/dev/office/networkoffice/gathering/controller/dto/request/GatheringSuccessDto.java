package dev.office.networkoffice.gathering.controller.dto.request;

public record GatheringSuccessDto(
        Long gatheringId,
        String review,
        Integer star) {
}
