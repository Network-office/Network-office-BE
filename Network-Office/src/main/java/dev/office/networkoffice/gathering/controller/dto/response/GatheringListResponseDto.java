package dev.office.networkoffice.gathering.controller.dto.response;

import java.util.List;

import lombok.Builder;

public record GatheringListResponseDto(List<GatheringResponseDto> gatherings) {
	public static GatheringListResponseDto from(List<GatheringResponseDto> gatherings){
		return new GatheringListResponseDto(gatherings);
	}
}
