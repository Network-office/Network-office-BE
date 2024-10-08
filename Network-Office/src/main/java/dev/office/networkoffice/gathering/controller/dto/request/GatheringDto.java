package dev.office.networkoffice.gathering.controller.dto.request;

import java.time.LocalDateTime;
import dev.office.networkoffice.gathering.entity.PlaceInfo;
import dev.office.networkoffice.gathering.entity.TimeInfo;

public record GatheringDto(
        String title,
        String category,
        String description,

        String place,
        String detailPlace,

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
        return PlaceInfo.builder()
                .place(place).detailPlace(detailPlace)
                .si(si).dong(dong).gu(gu)
                .x(x).y(y)
                .build();
    }

    public TimeInfo timeInfoConstructor() {
        return TimeInfo.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}
