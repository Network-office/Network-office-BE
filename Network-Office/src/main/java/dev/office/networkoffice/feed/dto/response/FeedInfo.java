package dev.office.networkoffice.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record FeedInfo(
        @JsonProperty("id")
        Long id,
        @JsonProperty("title")
        String title,
        @JsonProperty("contents")
        String contents,
        @JsonProperty("category")
        String category,
        @JsonProperty("author_id")
        Long authorId,
        @JsonProperty("author_display_name")
        String authorDisplayName,
        @JsonProperty("view")
        long view,
        @JsonProperty("like")
        long like,
        @JsonProperty("created_time")
        LocalDateTime createdTime
) {
}
