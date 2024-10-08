package dev.office.networkoffice.gathering.controller.dto.request;

import java.time.LocalDateTime;
import dev.office.networkoffice.gathering.entity.PlaceInfo;
import dev.office.networkoffice.gathering.entity.TimeInfo;

public record GatheringDto(
        String title,
        String category,
        String place,

        String detailPlace,
        String description,

        String si,
        String dong,
        String gu,

        Double x,

        Double y,
        String date,

        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public PlaceInfo placeInfoConstructor() {
        return PlaceInfo.setPlaceInfo(place, detailPlace, si, dong, gu, x, y);
    }

    public TimeInfo timeInfoConstructor() {
        return TimeInfo.setTimeInfo(date, startTime, endTime);
    }
}
