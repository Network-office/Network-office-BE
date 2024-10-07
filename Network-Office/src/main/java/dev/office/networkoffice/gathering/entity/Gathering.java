package dev.office.networkoffice.gathering.entity;

import java.util.ArrayList;
import java.util.List;

import dev.office.networkoffice.gathering.domain.Category;
import dev.office.networkoffice.gathering.domain.GatheringStatus;
import dev.office.networkoffice.gathering.domain.ReasonForCanceled;
import dev.office.networkoffice.gatheringUser.domain.GatheringUser;
import dev.office.networkoffice.gatheringUser.domain.GatheringUserStatus;
import dev.office.networkoffice.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User host;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private GatheringStatus gatheringStatus = GatheringStatus.IN_PROGRESS;

    @Enumerated(EnumType.STRING)
    private ReasonForCanceled reasonForCanceled; // 모임이 취소된 경우 사유 저장.

    private String review; // 성공적으로 마무리한 모임의 리뷰

    private Integer star; // SUCCESS로 모임이 마무리된 경우만 반영되기때문에 nullable 1~5.

    @OneToMany(mappedBy = "gathering", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<GatheringUser> gatheringUserList = new ArrayList<>();

    @Builder
    private Gathering(User host, String title, String description, Category category, PlaceInfo placeInfo, TimeInfo timeInfo,
                      GatheringStatus gatheringStatus, List<GatheringUser> gatheringUsers) {
        this.host = host;
        this.title = title;
        this.description = description;
        this.category = category;
        this.placeInfo = placeInfo;
        this.timeInfo = timeInfo;
        this.gatheringStatus = gatheringStatus;
        this.gatheringUserList = gatheringUsers;
    }

    public void modifyGatheringInfo(String title, String description, Category category, PlaceInfo placeInfo, TimeInfo timeInfo) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.placeInfo = placeInfo;
        this.timeInfo = timeInfo;
    }

    /*
        확정된 인원 리스트 조회
     */
    public List<User> getConfiremedUserList() {
        return gatheringUserList.stream()
                .filter(this::isConfirmedUser)
                .map(GatheringUser::getUser)
                .toList();
    }

    private boolean isConfirmedUser(GatheringUser gatheringUser){
        return gatheringUser.getGatheringUserStatus()
                .equals(GatheringUserStatus.CONFIRMED_USER);
    }

    /**
     * 파토난 모임은 파토사유가 필요하다.
     * @param reasonForCanceled
     */
    public void changeStatusToCancel(ReasonForCanceled reasonForCanceled){
        Assert.notNull(reasonForCanceled,"취소 사유는 null이 될 수 없습니다.");
        this.gatheringStatus = GatheringStatus.CANCELED;
        this.reasonForCanceled = reasonForCanceled;
    }

    /**
     * 성공한 모임은 리뷰와 별점이 필요하다.
     */
    public void changeStatusToSuccessFul(String review, Integer star){
        Assert.hasText(review,"review는 비어있을 수 없습니다.");
        Assert.notNull(star, "별점을 남겨주세요.");
        this.gatheringStatus = GatheringStatus.SUCCESSFUL;
        this.review = review;
        this.star = star;
    }

}
