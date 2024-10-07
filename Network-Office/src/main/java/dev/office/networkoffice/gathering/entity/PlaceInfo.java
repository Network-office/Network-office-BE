package dev.office.networkoffice.gathering.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceInfo {

    @Column(name = "place")
    private String place;

    @Column(name = "detail_place")
    private String detailPlace;

    @Column(name = "si")
    private String si;

    @Column(name = "dong")
    private String dong;

    @Column(name = "gu")
    private String gu;

    @Column(name = "x_position")
    private Double x;

    @Column(name = "y_position")
    private Double y;


    private PlaceInfo(String place, String detailPlace, String si, String dong, String gu, Double x, Double y) {
        this.place = place;
        this.detailPlace = detailPlace;
        this.si = si;
        this.dong = dong;
        this.gu = gu;
        this.x = x;
        this.y = y;
    }

    public static PlaceInfo setPlaceInfo(String place, String detailPlace, String si, String dong, String gu, Double x, Double y) {
        return new PlaceInfo(place, detailPlace, si, dong, gu, x, y);
    }
}
