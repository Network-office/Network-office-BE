package dev.office.networkoffice.gathering.controller.dto.response;

public record GatheringDeleteResponse(String gatheringStatus) {
    public static GatheringDeleteResponse from(String gatheringStatus) {
        return new GatheringDeleteResponse(gatheringStatus);
    }
}
