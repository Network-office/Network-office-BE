package dev.office.networkoffice.gathering.controller.dto.response;

import dev.office.networkoffice.gathering.entity.Gathering;
import lombok.Builder;
@Builder
public record GatheringResponseDto(
	String title,
	String category,
	String place,

	String detailPlace,
	String description,

	Double x,

	Double y,
	String date,

	String startTime,
	String endTime
) {
	public static GatheringResponseDto from(Gathering gathering){
		return GatheringResponseDto.builder()
			.title(gathering.getTitle())
			.category(String.valueOf(gathering.getCategory()))
			.place(gathering.getPlaceInfo().getPlace())
			.detailPlace(gathering.getPlaceInfo().getDetailPlace())
			.description(gathering.getDescription())
			.x(gathering.getPlaceInfo().getX())
			.y(gathering.getPlaceInfo().getY())
			.date(gathering.getTimeInfo().getDate())
			.startTime(gathering.getTimeInfo().getStartTime())
			.endTime(gathering.getTimeInfo().getEndTime())
			.build();
	}
}
