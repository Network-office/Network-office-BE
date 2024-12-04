package dev.office.networkoffice.feed.repository;

import dev.office.networkoffice.feed.entity.Feed;
import dev.office.networkoffice.feed.repository.dto.FeedWithLikeCount;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    @Query("""
            SELECT new dev.office.networkoffice.feed.repository.dto.FeedWithLikeCount(f, COUNT(l)) 
            FROM Feed f LEFT JOIN FETCH f.author a
            LEFT JOIN Likes l ON l.feed.id = f.id
            GROUP BY f.id
            ORDER BY f.createdTime DESC""")
    Page<FeedWithLikeCount> findAllFeedsWithLikeCount(Pageable pageable);

    @Modifying
    @Query("UPDATE Feed f SET f.view = f.view + 1 WHERE f.id = :feedId")
    void incrementViewCount(@Param("feedId") Long feedId);

    @Query("SELECT f FROM Feed f LEFT JOIN FETCH f.author WHERE f.id = :feedId")
    Optional<Feed> findFeedByIdWithAuthor(Long feedId);
}
