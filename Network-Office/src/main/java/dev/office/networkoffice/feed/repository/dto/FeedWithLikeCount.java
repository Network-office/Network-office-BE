package dev.office.networkoffice.feed.repository.dto;

import dev.office.networkoffice.feed.entity.Feed;

public record FeedWithLikeCount(
        Feed feed,
        Long likeCount
) {
}
