package dev.office.networkoffice.feed.service;

import dev.office.networkoffice.feed.dto.request.FeedWrite;
import dev.office.networkoffice.feed.entity.Feed;
import dev.office.networkoffice.feed.repository.CommentRepository;
import dev.office.networkoffice.feed.repository.FeedRepository;
import dev.office.networkoffice.feed.repository.LikesRepository;
import dev.office.networkoffice.feed.repository.VisitedRepository;
import dev.office.networkoffice.user.entity.OAuthInfo;
import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    @Mock
    private FeedRepository feedRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private VisitedRepository visitedRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private LikesRepository likesRepository;

    @InjectMocks
    private FeedService feedService;

    private User mockUser;
    private Feed mockFeed;

    @BeforeEach
    void setUp() {
        OAuthInfo oAuthInfo = OAuthInfo.createForKakao("1", "test");
        mockUser = User.createNewUserWithOAuth(oAuthInfo, "http://test.com");
        mockFeed = Feed.writeNewFeed("Test Title", "Test Contents", "Test Category", mockUser);
    }

    @DisplayName("동시에 여러 사용자가 피드를 조회해도 조회수가 정상적으로 증가한다. (동시성 테스트)")
    @Test
    void shouldIncrementViewCount_WhenMultipleUsersViewFeed() throws InterruptedException {
        // given
        Long feedId = 1L;
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 가짜 Feed 객체 설정
        when(feedRepository.findFeedByIdWithAuthor(feedId)).thenReturn(Optional.of(mock()));

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    feedService.getFeed(null, feedId);
                } finally {
                    latch.countDown();
                }
            });
        }

        // then
        latch.await(); // 모든 스레드가 작업을 완료할 때까지 대기
        executorService.shutdown();

        // 조회수 증가 메서드가 정확히 10번 호출되었는지 검증
        verify(feedRepository, times(threadCount)).incrementViewCount(feedId);
    }

    @DisplayName("동일한 사용자가 동일한 피드를 여러 번 조회해도 조회수는 한 번만 증가한다. (24시간 동안)")
    @Test
    void shouldIncrementViewCountOnce_WhenUserViewFeedMultipleTimes() {
        // given
        Long userId = 1L;
        Long feedId = 1L;

        when(visitedRepository.isFeedVisited(userId, feedId)).thenReturn(false);
        when(feedRepository.findFeedByIdWithAuthor(feedId)).thenReturn(Optional.of(mockFeed));
        when(commentRepository.findByFeedId(feedId)).thenReturn(new ArrayList<>());
        when(likesRepository.existsByUserIdAndFeedId(userId, feedId)).thenReturn(false);

        // when
        feedService.getFeed(userId, feedId);
        when(visitedRepository.isFeedVisited(userId, feedId)).thenReturn(true); // 캐시에 저장된 방문 여부를 true로 설정
        feedService.getFeed(userId, feedId);

        // then
        verify(feedRepository, times(1)).incrementViewCount(feedId); // 조회수 증가 메서드가 한 번만 호출되었는지 검증
        verify(visitedRepository, times(2)).isFeedVisited(userId, feedId);
        verify(visitedRepository, times(1)).save(userId, feedId);
    }

    @DisplayName("유효한 사용자와 요청이 제공된 경우 피드를 작성된다.")
    @Test
    void shouldWriteFeed_WhenValidUserAndRequestProvided() {
        // given
        Long userId = 1L;
        FeedWrite request = new FeedWrite("Test Title", "Test Contents", "Test Category");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // when
        assertDoesNotThrow(() -> feedService.writeFeed(userId, request));

        // then
        verify(userRepository).findById(userId);
        verify(feedRepository).save(any(Feed.class));
    }

    @DisplayName("사용자가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenUserNotFound() {
        // given
        Long userId = 1L;
        FeedWrite request = new FeedWrite("Test Title", "Test Contents", "Test Category");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> feedService.writeFeed(userId, request),
                "존재하지 않는 사용자입니다.");
    }

    @DisplayName("피드가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenFeedNotFound() {
        // given
        Long userId = 1L;
        Long feedId = 2L;

        when(feedRepository.findFeedByIdWithAuthor(feedId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> feedService.getFeed(userId, feedId),
                "존재하지 않는 피드입니다.");
    }
}
