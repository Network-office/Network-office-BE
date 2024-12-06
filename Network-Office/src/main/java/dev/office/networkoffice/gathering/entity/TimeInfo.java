package dev.office.networkoffice.gathering.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Builder;
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

    @Column(name = "start_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startTime;

    @Column(name = "end_time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime endTime;

    @Builder
    private TimeInfo(String date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
