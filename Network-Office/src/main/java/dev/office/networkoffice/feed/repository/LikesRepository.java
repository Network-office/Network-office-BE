package dev.office.networkoffice.feed.repository;

import dev.office.networkoffice.feed.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query(value = """
            SELECT EXISTS (
                SELECT 1 FROM likes l
                WHERE l.user_id = :userId
                AND l.feed_id = :feedId
            )
            """, nativeQuery = true)
    boolean existsByUserIdAndFeedId(@Param("userId") Long userId, @Param("feedId") Long feedId);

    @Modifying
    @Query("DELETE FROM Likes l WHERE l.user.id = :userId AND l.feed.id = :feedId")
    int deleteByUserIdAndFeedId(@Param("userId") Long userId, @Param("feedId") Long feedId);

    long countByFeedId(Long feedId);
}
