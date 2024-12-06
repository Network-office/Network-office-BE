package dev.office.networkoffice.gathering.controller.dto.request;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import dev.office.networkoffice.gathering.entity.PlaceInfo;
import dev.office.networkoffice.gathering.entity.TimeInfo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GatheringDto(
        String title,
        String category,
        String description,
        @Min(value = 5, message = "최소 인원은 5명 이상이어야 합니다.")
        @Max(value = 100, message = "최대 인원은 100명 이하여야 합니다.")
        int maxMembers,

        String place,
        String detailPlace,

        String si,
        String dong,
        String gu,

        Double x,
        Double y,

        String date,
        String startTime,
        String endTime
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
                .startTime(LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm")))
                .endTime(LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm")))
                .build();
    }
}
