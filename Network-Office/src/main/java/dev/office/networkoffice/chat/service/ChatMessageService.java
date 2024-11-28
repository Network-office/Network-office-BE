package dev.office.networkoffice.chat.service;

import dev.office.networkoffice.chat.dto.request.ChatMessageRequest;
import dev.office.networkoffice.chat.dto.response.ChatMessageResponse;
import dev.office.networkoffice.chat.entity.ChatMessage;
import dev.office.networkoffice.gathering.entity.Gathering;
import dev.office.networkoffice.gathering.repository.GatheringRepository;
import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> loadChatMessageList(Long roomId) {
        Gathering gathering = findChatRoomWithMessagesById(roomId);
        return gathering.getChatMessageList().stream().map(ChatMessageResponse::from).toList();
    }

    @Transactional
    public ChatMessageResponse createChatMessage(ChatMessageRequest request, Long userId, Long roomId) {
        User author = findUserById(userId);
        Gathering gathering = findChatRoomById(roomId);
        ChatMessage chatMessage = createChatMessageByRequest(request, author, gathering);
        gathering.createMessage(chatMessage);
        gatheringRepository.save(gathering);
        return ChatMessageResponse.from(chatMessage);
    }

    private ChatMessage createChatMessageByRequest(ChatMessageRequest request, User author, Gathering gathering) {
        return ChatMessage.builder()
                .author(author)
                .gathering(gathering)
                .content(request.text())
                .build();
    }

    @Transactional
    public ChatMessageResponse createAnonymousChatMessage(ChatMessageRequest request, Long roomId) {
        Gathering gathering = findChatRoomById(roomId);
        ChatMessage chatMessage = createChatAnonymousMessageByRequest(request, gathering);
        gathering.createMessage(chatMessage);
        gatheringRepository.save(gathering);
        return ChatMessageResponse.anonymousFrom(chatMessage);
    }

    private ChatMessage createChatAnonymousMessageByRequest(ChatMessageRequest request, Gathering gathering) {
        return ChatMessage.builder()
                .gathering(gathering)
                .content(request.text())
                .writer(request.from())
                .build();
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private Gathering findChatRoomById(Long roomId) {
        return gatheringRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("모임(채팅방)을 찾을 수 없습니다."));
    }

    private Gathering findChatRoomWithMessagesById(Long roomId) {
        return gatheringRepository.findWithMessagesById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
    }
}
