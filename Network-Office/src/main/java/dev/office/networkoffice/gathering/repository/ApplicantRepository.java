package dev.office.networkoffice.gathering.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.office.networkoffice.gathering.entity.Applicants;
import dev.office.networkoffice.gathering.entity.Gathering;

public interface ApplicantRepository extends JpaRepository<Applicants, Long> {
	List<Applicants> findAllByGathering(Gathering gathering);
}
