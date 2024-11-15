package dev.office.networkoffice.feed.dto.response;

import java.time.LocalDateTime;

public record FeedInfo(
        Long id,
        String title,
        String contents,
        String category,
        Long authorId,
        String authorDisplayName,
        Long view,
        Long like,
        LocalDateTime createdTime
) {
}
