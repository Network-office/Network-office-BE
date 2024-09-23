package dev.office.networkoffice.gathering.controller.dto.request;

import java.time.LocalDateTime;

import dev.office.networkoffice.gathering.entity.PlaceInfo;
import dev.office.networkoffice.gathering.entity.TimeInfo;

public record GatheringModifyDto(
	Long id,
	String title,
	String category,
	String place,
	String detailPlace,
	String si,
	String dong,
	String gu,
	String description,

	Double x,

	Double y,
	String date,

	LocalDateTime startTime,
	LocalDateTime endTime


	) {
	public PlaceInfo placeInfoConstructor(){
		return PlaceInfo.setPlaceInfo(place,detailPlace,si,dong,gu,x,y);
	}

	public TimeInfo timeInfoConstructor(){
		return TimeInfo.setTimeInfo(date, startTime, endTime);
	}
}
