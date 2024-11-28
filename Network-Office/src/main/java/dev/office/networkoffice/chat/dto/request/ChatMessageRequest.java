package dev.office.networkoffice.chat.dto.request;

public record ChatMessageRequest(
        String from,
        String text
) {
}
