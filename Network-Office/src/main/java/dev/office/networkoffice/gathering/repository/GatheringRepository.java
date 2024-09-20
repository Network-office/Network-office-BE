package dev.office.networkoffice.gathering.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.office.networkoffice.gathering.entity.Gathering;
import dev.office.networkoffice.user.entity.User;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {
	Optional<Gathering> findByHostId(Long hostId);
	@Query("""
            SELECT g
            FROM Gathering g
            WHERE g.placeInfo.si = :si AND g.placeInfo.dong = :dong AND g.placeInfo.gu = :gu""")
	List<Gathering> findDetailAddressBySiAndDongAndGu(String si, String dong, String gu);
}
