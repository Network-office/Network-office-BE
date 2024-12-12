package dev.office.networkoffice.chat.service;

import dev.office.networkoffice.chat.dto.response.ChatRoomListResponse;
import dev.office.networkoffice.chat.dto.response.ChatRoomResponse;
import dev.office.networkoffice.gathering.entity.Gathering;
import dev.office.networkoffice.gathering.repository.GatheringRepository;
import dev.office.networkoffice.gatheringUser.domain.GatheringUser;
import dev.office.networkoffice.gatheringUser.repository.GatheringUserRepository;
import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final GatheringRepository gatheringRepository;
    private final GatheringUserRepository gatheringUserRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ChatRoomListResponse getChatRoomsByUserId(Long userId) {
        List<ChatRoomResponse> chatRoomsInHost = getChatRoomsByHostId(userId);
        List<ChatRoomResponse> chatRoomsInMember = getChatRoomsByMemberId(userId);
        chatRoomsInHost.addAll(chatRoomsInMember);
        return ChatRoomListResponse.from(chatRoomsInHost);
    }

    private List<ChatRoomResponse> getChatRoomsByMemberId(Long memberId) {
        List<GatheringUser> gatheringUsers = gatheringUserRepository.findByUser(findUserById(memberId));

        return gatheringUsers.stream()
                .map(GatheringUser::getGathering)
                .map(ChatRoomResponse::from)
                .toList();
    }

    private List<ChatRoomResponse> getChatRoomsByHostId(Long hostId) {
        List<Gathering> gatherings = gatheringRepository.findByHost(findUserById(hostId));

        return gatherings.stream()
                .map(ChatRoomResponse::from)
                .toList();
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
