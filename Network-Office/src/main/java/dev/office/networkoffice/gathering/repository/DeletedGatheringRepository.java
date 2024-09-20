package dev.office.networkoffice.gathering.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.office.networkoffice.gathering.entity.DeletedGathering;

public interface DeletedGatheringRepository extends JpaRepository<DeletedGathering, Long> {
}
