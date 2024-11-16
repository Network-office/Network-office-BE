package dev.office.networkoffice.feed.entity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.office.networkoffice.user.entity.OAuthInfo;
import dev.office.networkoffice.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("피드 엔티티 객체 테스트")
class FeedTest {

    @DisplayName("올바른 값을 전달한 경우 정상적으로 피드 객체가 생성된다.")
    @Test
    void shouldReturnFeedObject_WhenInputIsValid() {
        // given
        User testUser = createTestUser();
        String title = "title";
        String category = "category";
        String contents = "contents";

        // when
        Feed feed = Feed.writeNewFeed(title, contents, category, testUser);

        // then
        assertEquals(title, feed.getTitle());
        assertEquals(contents, feed.getContents());
        assertEquals(category, feed.getCategory());
        assertEquals(testUser, feed.getAuthor());
    }

    @DisplayName("피드 생성 시 조회 수는 0이며, 조회 수 증가 후 값은 1이 된다.")
    @Test
    void shouldInitializeViewCountToZeroAndIncrementToOne() {
        // given
        User testUser = createTestUser();
        String title = "title";
        String category = "category";
        String contents = "contents";

        // when
        Feed feed = Feed.writeNewFeed(title, contents, category, testUser);

        // then
        assertAll(
                () -> assertEquals(0L, feed.getView(), "피드 생성 시 조회 수는 0이어야 합니다."),
                () -> {
                    feed.increaseView();
                    assertEquals(1L, feed.getView(), "조회 수 증가 후 값은 1이어야 합니다.");
                }
        );
    }

    @DisplayName("제목, 카테고리 또는 내용이 비어있는 경우 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource({
            ", category, contents",
            "title, , contents",
            "title, category,"
    })
    void shouldThrowException_WhenFeedFieldsAreNull(String title, String category, String contents) {
        // given
        User testUser = createTestUser();

        // when, then
        assertThrows(IllegalArgumentException.class, () -> Feed.writeNewFeed(title, contents, category, testUser));
    }

    private static User createTestUser() {
        OAuthInfo oAuthInfo = OAuthInfo.createForKakao("1", "test");
        return User.createNewUserWithOAuth(oAuthInfo, "http://test.com");
    }
}
