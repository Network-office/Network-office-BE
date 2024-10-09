package dev.office.networkoffice.gathering.repository;

import java.util.List;
import java.util.Optional;

import dev.office.networkoffice.gathering.domain.GatheringStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.office.networkoffice.gathering.entity.Gathering;

public interface GatheringRepository extends JpaRepository<Gathering, Long> {

    @Query("""
            SELECT g
            FROM Gathering g
            WHERE g.placeInfo.si = :si AND g.placeInfo.dong = :dong AND g.placeInfo.gu = :gu AND g.gatheringStatus = :status""")
    List<Gathering> findGatheringsByPlaceInfoAndStatus(String si, String dong, String gu, GatheringStatus gatheringStatus);

    Optional<Gathering> findByHostIdAndId(Long hostId, Long gatheringId);
}
