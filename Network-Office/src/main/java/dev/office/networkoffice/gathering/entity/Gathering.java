package dev.office.networkoffice.gathering.entity;

import java.util.ArrayList;
import java.util.List;

import dev.office.networkoffice.gathering.controller.dto.request.GatheringModifyDto;
import dev.office.networkoffice.gathering.domain.Category;
import dev.office.networkoffice.gathering.domain.GatheringStatus;
import dev.office.networkoffice.gathering.domain.ReasonForCanceled;
import dev.office.networkoffice.gathering.domain.ReasonForDeportation;
import dev.office.networkoffice.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 모임은 호스트 & 유저들을 묶어주는 중간 엔티티 개념.
 */
@Entity
@Getter
@Table(name = "gatherings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gathering {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gathering_id")
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Embedded
    private PlaceInfo placeInfo;

    @Embedded
    private TimeInfo timeInfo;

    @Enumerated(EnumType.STRING)
    private GatheringStatus gatheringStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_gathering_id")
    private DeletedGathering deletedGathering;


    @OneToMany(mappedBy = "gathering", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<GatheringUserConfirmManager> confirmedUserList = new ArrayList<>();

    @Builder
    private Gathering(String title, String description, Category category, PlaceInfo placeInfo, TimeInfo timeInfo,
                      List<GatheringUserConfirmManager> confirmedUserList) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.placeInfo = placeInfo;
        this.timeInfo = timeInfo;
        this.confirmedUserList = confirmedUserList;
    }

    public void modifyGatheringInfo(GatheringModifyDto modifyDto) {
        this.title = modifyDto.title();
        this.description = modifyDto.description();
        this.category = Category.valueOf(modifyDto.category());
        this.placeInfo = modifyDto.placeInfoConstructor();
        this.timeInfo = modifyDto.timeInfoConstructor();
    }

    /*
        확정된 인원 리스트 조회
     */
    public List<User> getConfiremedUserList() {
        return confirmedUserList.stream()
                .map(GatheringUserConfirmManager::getUser)
                .toList();
    }

    public void changeStatusToCanceled(GatheringStatus status, DeletedGathering deletedGathering){
        this.gatheringStatus = GatheringStatus.CLOSED;
        this.deletedGathering = deletedGathering;
    }

	/*
		모임 파토
	 */


}
