package dev.office.networkoffice.feed.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FeedWrite(
        @JsonProperty("title")
        String title,
        @JsonProperty("contents")
        String contents,
        @JsonProperty("category")
        String category
) {
}
