package dev.office.networkoffice.gathering.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.office.networkoffice.gathering.entity.Applicants;

public interface ApplicantRepository extends JpaRepository<Applicants, Long> {

	List<Applicants> findAllByGatheringId(Long gatheringId);

	Optional<Applicants> findByIdAndUserId(Long applicantId, Long userId);
}
