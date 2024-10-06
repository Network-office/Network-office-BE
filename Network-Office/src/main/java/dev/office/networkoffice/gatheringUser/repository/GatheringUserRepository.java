package dev.office.networkoffice.gatheringUser.repository;

import java.util.List;
import java.util.Optional;

import dev.office.networkoffice.gathering.entity.Gathering;
import dev.office.networkoffice.gatheringUser.domain.GatheringUserStatus;
import dev.office.networkoffice.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.office.networkoffice.gatheringUser.domain.GatheringUser;
import org.springframework.data.jpa.repository.Query;

public interface GatheringUserRepository extends JpaRepository<GatheringUser, Long> {

    @Query("""
            SELECT m
            FROM GatheringUser m
            WHERE m.gathering = :gathering AND m.gatheringUserStatus = :status""")
    List<GatheringUser> findAllByGatheringAndGatheringUserStatus(Gathering gathering, GatheringUserStatus status);

    GatheringUser findByUserAndGathering(User user, Gathering gathering);

    Optional<GatheringUser> findByIdAndUserId(Long applicantId, Long userId);
}
