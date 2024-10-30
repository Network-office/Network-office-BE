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
            SELECT distinct m
            FROM GatheringUser m
            join fetch m.gathering k
            WHERE k.id = :gatheringId
            AND k.host.id = :hostId
            AND m.gatheringUserStatus = :status""")
    List<GatheringUser> findApplicantsInGatheringByHost(Long gatheringId, Long hostId, GatheringUserStatus status);

    Optional<GatheringUser> findByUserAndGathering(User user, Gathering gathering);

    @Query("""
            SELECT m
            FROM GatheringUser m
            WHERE m.id = :gatheringUserId AND m.gathering.host.id = :userId""")
    Optional<GatheringUser> findGatheringUserByHost(Long gatheringUserId, Long userId);
}
