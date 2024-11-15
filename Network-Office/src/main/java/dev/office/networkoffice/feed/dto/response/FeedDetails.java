package dev.office.networkoffice.feed.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record FeedDetails(
        Long id,
        String title,
        String contents,
        String category,
        Long authorId,
        String authorDisplayName,
        Long view,
        Long like,
        LocalDateTime createdTime,
        List<CommentDetails> comments
) {
}
