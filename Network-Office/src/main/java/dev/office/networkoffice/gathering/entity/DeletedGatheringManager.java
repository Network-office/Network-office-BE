package dev.office.networkoffice.gathering.entity;

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

@Entity
@Getter
@Table(name = "deleted_gatherings_manager")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeletedGatheringManager {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deleted_gathering_manager_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_gathering_id")
    private DeletedGathering deletedGathering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long originGatheringId;

    @Builder
    private DeletedGatheringManager(DeletedGathering deletedGathering, User user, Long originGatheringId) {
        this.deletedGathering = deletedGathering;
        this.user = user;
        this.originGatheringId = originGatheringId;
    }

}
