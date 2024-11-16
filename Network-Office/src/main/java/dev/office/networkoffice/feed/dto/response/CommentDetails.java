package dev.office.networkoffice.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record CommentDetails(
        @JsonProperty("id")
        Long id,
        @JsonProperty("text")
        String text,
        @JsonProperty("author_id")
        Long authorId,
        @JsonProperty("author_display_name")
        String authorDisplayName,
        @JsonProperty("created_time")
        LocalDateTime createdTime
) {
}
