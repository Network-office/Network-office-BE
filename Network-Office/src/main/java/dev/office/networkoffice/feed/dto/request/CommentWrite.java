package dev.office.networkoffice.feed.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentWrite(
        @JsonProperty("feed_id")
        Long feedId,
        @JsonProperty("text")
        String text
) {
}
