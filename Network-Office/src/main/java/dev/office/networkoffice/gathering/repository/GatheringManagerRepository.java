package dev.office.networkoffice.gathering.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.office.networkoffice.gathering.domain.GatheringAuthority;
import dev.office.networkoffice.gathering.entity.GatheringUserConfirmManager;

public interface GatheringManagerRepository extends JpaRepository<GatheringUserConfirmManager, Long> {
	@Query("""
            SELECT m
            FROM GatheringUserConfirmManager m
            WHERE m.gathering.id = :gatheringId AND m.user.id = :userId AND m.gatheringAuthority = :authority""")
	Optional<GatheringUserConfirmManager> findByGatheringAndUser(Long gatheringId, Long userId, GatheringAuthority authority);
}
