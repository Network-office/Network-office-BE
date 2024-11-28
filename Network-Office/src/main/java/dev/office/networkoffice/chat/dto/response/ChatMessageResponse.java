package dev.office.networkoffice.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.office.networkoffice.chat.entity.ChatMessage;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessageResponse(
        Long id,
        @JsonProperty("writer")
        String writer,
        @JsonProperty("content")
        String content,
        @JsonProperty("created_time")
        LocalDateTime createdTime
) {
    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .writer(chatMessage.getAuthor().getProfile().getDisplayName())
                .createdTime(chatMessage.getCreatedTime())
                .build();
    }

    public static ChatMessageResponse anonymousFrom(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .writer(chatMessage.getWriter())
                .createdTime(chatMessage.getCreatedTime())
                .build();
    }
}
