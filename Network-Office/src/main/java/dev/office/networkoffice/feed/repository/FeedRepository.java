package dev.office.networkoffice.feed.repository;

import dev.office.networkoffice.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Page<Feed> findAllByOrderByCreatedTimeDesc(Pageable pageable);
}
