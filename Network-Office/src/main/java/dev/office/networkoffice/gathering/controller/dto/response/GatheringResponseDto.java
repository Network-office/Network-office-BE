package dev.office.networkoffice.gathering.controller.dto.response;

import java.time.LocalDateTime;

import dev.office.networkoffice.gathering.entity.Gathering;
import lombok.Builder;

@Builder
public record GatheringResponseDto(
        String title,
        String category,
        String description,

        String place,
        String detailPlace,

        Double x,
        Double y,

        String date,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static GatheringResponseDto from(Gathering gathering) {
        return GatheringResponseDto.builder()
                .title(gathering.getTitle())
                .category(String.valueOf(gathering.getCategory()))
                .description(gathering.getDescription())
                .place(gathering.getPlaceInfo().getPlace())
                .detailPlace(gathering.getPlaceInfo().getDetailPlace())
                .x(gathering.getPlaceInfo().getX())
                .y(gathering.getPlaceInfo().getY())
                .date(gathering.getTimeInfo().getDate())
                .startTime(gathering.getTimeInfo().getStartTime())
                .endTime(gathering.getTimeInfo().getEndTime())
                .build();
    }
}
