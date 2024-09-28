package dev.office.networkoffice.gatheringAuthority.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.office.networkoffice.gatheringAuthority.domain.GatheringAuthority;
import dev.office.networkoffice.gatheringAuthority.domain.GatheringAuthorityManager;

public interface GatheringAuthorityRepository extends JpaRepository<GatheringAuthorityManager, Long> {
    @Query("""
            SELECT m
            FROM GatheringAuthorityManager m
            WHERE m.gathering.id = :gatheringId AND m.user.id = :userId AND m.gatheringAuthority = :authority""")
    Optional<GatheringAuthorityManager> findByGatheringAndUser(Long gatheringId, Long userId, GatheringAuthority authority);
}
