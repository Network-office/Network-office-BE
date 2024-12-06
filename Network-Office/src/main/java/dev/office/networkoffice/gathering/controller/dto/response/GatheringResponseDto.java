package dev.office.networkoffice.gathering.controller.dto.response;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.office.networkoffice.gathering.entity.Gathering;
import lombok.Builder;

@Builder
public record GatheringResponseDto(
        Long id,
        String title,
        String category,
        String description,

        String place,
        String detailPlace,

        Double x,
        Double y,

        String date,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        String startTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        String endTime
) {
    public static GatheringResponseDto from(Gathering gathering) {
        return GatheringResponseDto.builder()
                .id(gathering.getId())
                .title(gathering.getTitle())
                .category(String.valueOf(gathering.getCategory()))
                .description(gathering.getDescription())
                .place(gathering.getPlaceInfo().getPlace())
                .detailPlace(gathering.getPlaceInfo().getDetailPlace())
                .x(gathering.getPlaceInfo().getX())
                .y(gathering.getPlaceInfo().getY())
                .date(gathering.getTimeInfo().getDate())
                .startTime(String.valueOf(gathering.getTimeInfo().getStartTime()))
                .endTime(String.valueOf(gathering.getTimeInfo().getEndTime()))
                .build();
    }
}
