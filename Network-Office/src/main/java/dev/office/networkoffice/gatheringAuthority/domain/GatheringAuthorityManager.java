package dev.office.networkoffice.gatheringAuthority.domain;

import dev.office.networkoffice.gathering.entity.Gathering;
import dev.office.networkoffice.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저와 모임의 다대다 관계를 분리해주는 중간 엔티티 역할.
 */
@Entity
@Getter
@Table(name = "gathering_authority_manager")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GatheringAuthorityManager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gathering_authority_manager_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private GatheringAuthority gatheringAuthority;

    @Builder
    private GatheringAuthorityManager(Gathering gathering, User user, GatheringAuthority gatheringAuthority) {
        this.gathering = gathering;
        this.user = user;
        this.gatheringAuthority = gatheringAuthority;
    }
}
