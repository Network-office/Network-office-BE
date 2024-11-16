package dev.office.networkoffice.feed.repository;

import dev.office.networkoffice.feed.entity.Feed;
import dev.office.networkoffice.feed.entity.Likes;
import dev.office.networkoffice.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsByUserAndFeed(User user, Feed feed);
    Optional<Likes> findByUserAndFeed(User user, Feed feed);
    Long countByFeedId(Long feedId);
}
