package dev.office.networkoffice.gathering.controller.dto.response;

public record GatheringClosedResponse(
        String gatheringStatus
) {
    public static GatheringClosedResponse from(String gatheringStatus) {
        return new GatheringClosedResponse(gatheringStatus);
    }
}
