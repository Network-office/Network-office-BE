package dev.office.networkoffice.gathering.entity;

import java.sql.Time;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeInfo {

	@Column(name = "date")
	private String date;

	@DateTimeFormat()
	@Column(name = "start_time")
	private String startTime;
	@DateTimeFormat()
	@Column(name = "end_time")
	private String endTime;



	private TimeInfo(String date, String startTime , String endTime) {
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public static TimeInfo setTimeInfo(String date, String startTime , String endTime) {
		return new TimeInfo(date, startTime, endTime);
	}
}

