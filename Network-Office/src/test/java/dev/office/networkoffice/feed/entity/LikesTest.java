package dev.office.networkoffice.feed.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.office.networkoffice.user.entity.OAuthInfo;
import dev.office.networkoffice.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("좋아요 엔티티 객체 테스트")
class LikesTest {

    @DisplayName("유효한 User와 Feed로 Likes 객체를 생성할 수 있다.")
    @Test
    void shouldCreateLikes_WhenValidUserAndFeedAreProvided() {
        // given
        User testUser = createTestUser();
        Feed testFeed = createTestFeed(testUser);

        // when
        Likes likes = Likes.createLikes(testUser, testFeed);

        // then
        assertNotNull(likes, "Likes 객체가 null이면 안 됩니다.");
        assertEquals(testUser, likes.getUser(), "Likes의 User 정보가 올바르지 않습니다.");
        assertEquals(testFeed, likes.getFeed(), "Likes의 Feed 정보가 올바르지 않습니다.");
    }

    @DisplayName("파라미터가 null인 경우 Likes 생성 시 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource({
            "null, validFeed",
            "validUser, null"
    })
    void shouldThrowException_WhenUserOrFeedIsNull(String user, String feed) {
        // given
        final User testUser = createTestUser();
        final Feed testFeed = createTestFeed(testUser);
        User userToTest = "null".equals(user) ? null : testUser;
        Feed feedToTest = "null".equals(feed) ? null : testFeed;

        // when, then
        assertThrows(
                IllegalArgumentException.class,
                () -> Likes.createLikes(userToTest, feedToTest),
                "User 또는 Feed가 null일 경우 예외가 발생해야 합니다."
        );
    }

    private User createTestUser() {
        OAuthInfo oAuthInfo = OAuthInfo.createForKakao("1", "test-user");
        return User.createNewUserWithOAuth(oAuthInfo, "http://test-profile-image.com");
    }

    private Feed createTestFeed(User user) {
        String title = "title";
        String category = "category";
        String contents = "contents";
        return Feed.writeNewFeed(title, contents, category, user);
    }
}
