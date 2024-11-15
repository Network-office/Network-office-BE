package dev.office.networkoffice.feed.dto.response;

import java.time.LocalDateTime;

public record CommentDetails(
        Long id,
        String text,
        Long authorId,
        String authorDisplayName,
        LocalDateTime createdTime
) {
}
