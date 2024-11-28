package dev.office.networkoffice.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ChatRoomListResponse(
        @JsonProperty("rooms")
        List<ChatRoomResponse> rooms
) {
    public static ChatRoomListResponse from(List<ChatRoomResponse> chatRooms) {
        return new ChatRoomListResponse(chatRooms);
    }
}
