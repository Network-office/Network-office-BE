package dev.office.networkoffice.feed.repository;

import dev.office.networkoffice.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {

}
