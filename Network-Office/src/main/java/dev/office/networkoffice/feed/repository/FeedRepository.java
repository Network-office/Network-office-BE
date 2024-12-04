package dev.office.networkoffice.feed.repository;

import dev.office.networkoffice.feed.entity.Feed;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Page<Feed> findAllByOrderByCreatedTimeDesc(Pageable pageable);

    @Modifying
    @Query("UPDATE Feed f SET f.view = f.view + 1 WHERE f.id = :feedId")
    void incrementViewCount(@Param("feedId") Long feedId);

    @Query("SELECT f FROM Feed f LEFT JOIN FETCH f.author WHERE f.id = :feedId")
    Optional<Feed> findFeedByIdWithAuthor(Long feedId);
}
