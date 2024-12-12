package dev.office.networkoffice.chat.dto.response;

import dev.office.networkoffice.gathering.entity.Gathering;
import lombok.Builder;

@Builder
public record ChatRoomResponse(
        Long id,
        String title,
        AuthorInfoDto authorInfo,
        CurrentMessageDto currentMessage,
        String myRole,
        Integer unreadCount
) {
    @Builder
    public record AuthorInfoDto(
            String name,
            String avatarSrc
    ) {}

    @Builder
    public record CurrentMessageDto(
            String content,
            String timestamp
    ) {}

    public static ChatRoomResponse from(Gathering gathering) {
        AuthorInfoDto authorInfoDto = AuthorInfoDto.builder()
                .name(gathering.getHost().getProfile().getDisplayName())
                .avatarSrc(gathering.getHost().getProfile().getImageUrl())
                .build();

        CurrentMessageDto currentMessageDto = CurrentMessageDto.builder()
                .content(gathering.getDescription())
                .timestamp(gathering.getTimeInfo().getStartTime().toString())
                .build();

        return ChatRoomResponse.builder()
                .id(gathering.getId())
                .title(gathering.getTitle())
                .authorInfo(authorInfoDto)
                .currentMessage(currentMessageDto)
                .myRole("all")
                .unreadCount(1).build();
    }
}
