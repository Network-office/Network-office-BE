package dev.office.networkoffice.gatheringUser.domain;

import dev.office.networkoffice.gathering.domain.ReasonForDeportation;
import dev.office.networkoffice.gathering.entity.Gathering;
import dev.office.networkoffice.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

/**
 * 신청 테이블을 따로 작성한다.
 * 신청을 넣는다 ->  신청 엔티티를 생성한다.
 * 신청을 수락한다. -> 신청 엔티티를 제거하고 모임유저에 추가.
 * 신청을 거절한다. -> 신청 엔티티를 제거한다.
 */
@Entity
@Getter
@Table(name = "gathering_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GatheringUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gathering_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private GatheringUserStatus gatheringUserStatus = GatheringUserStatus.APPLY_USER;

    private String deniedReason;

    private ReasonForDeportation reasonForDeportation;

    @Builder
    private GatheringUser(Long id, Gathering gathering, User user, GatheringUserStatus gatheringUserStatus) {
        this.id = id;
        this.gathering = gathering;
        this.user = user;
        this.gatheringUserStatus = gatheringUserStatus;
    }

    public void updateGatheringUserStatus(GatheringUserStatus status) {
        Assert.notNull(status,"모임 유저 상태는 null일 수 없습니다.");
        this.gatheringUserStatus = status;
    }

    public void updateDeniedReason(String deniedReason){
        Assert.hasText(deniedReason, "거부사유는 비어 있을 수 없습니다.");
        this.deniedReason = deniedReason;
    }

    public void updateDeportationReason(ReasonForDeportation reasonForDeportation){
        Assert.notNull(reasonForDeportation, "추방사유는 null일 수 없습니다.");
        this.reasonForDeportation = reasonForDeportation;
    }
}
