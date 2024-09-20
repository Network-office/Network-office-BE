package dev.office.networkoffice.gathering.controller.dto.request;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.parameters.P;

import dev.office.networkoffice.gathering.entity.PlaceInfo;
import dev.office.networkoffice.gathering.entity.TimeInfo;
import jakarta.persistence.Column;

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

	String startTime,
	String endTime
) {
	public PlaceInfo placeInfoConstructor(){
		return PlaceInfo.setPlaceInfo(place,detailPlace,si,dong,gu,x,y);
	}

	public TimeInfo timeInfoConstructor(){
		return TimeInfo.setTimeInfo(date, startTime, endTime);
	}
}
