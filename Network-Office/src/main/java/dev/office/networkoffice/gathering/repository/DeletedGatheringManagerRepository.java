package dev.office.networkoffice.gathering.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.office.networkoffice.gathering.entity.DeletedGatheringManager;

public interface DeletedGatheringManagerRepository extends JpaRepository<DeletedGatheringManager, Long> {
    Optional<DeletedGatheringManager> findByOriginGatheringId(Long gatheringId);
}
