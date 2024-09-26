package dev.office.networkoffice.gathering.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.office.networkoffice.gathering.entity.GatheringApplicants;

public interface ApplicantRepository extends JpaRepository<GatheringApplicants, Long> {

    List<GatheringApplicants> findAllByGatheringId(Long gatheringId);

    Optional<GatheringApplicants> findByIdAndUserId(Long applicantId, Long userId);
}
