package dev.office.networkoffice.feed.repository;

import dev.office.networkoffice.feed.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByFeedId(Long feedId);
}
