package dev.office.networkoffice.feed.dto.request;

public record FeedWrite(
        String title,
        String contents,
        String category
) {
}
