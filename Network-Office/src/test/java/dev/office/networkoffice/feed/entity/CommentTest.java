package dev.office.networkoffice.feed.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.office.networkoffice.user.entity.OAuthInfo;
import dev.office.networkoffice.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Comment 엔티티 테스트")
class CommentTest {

    @DisplayName("유효한 입력값으로 댓글이 정상 생성된다.")
    @Test
    void shouldCreateCommentSuccessfully_WhenValidInputsAreProvided() {
        // given
        User testUser = createTestUser();
        Feed testFeed = createTestFeed(testUser);
        String text = "This is a valid comment.";

        // when
        Comment comment = Comment.writeNewComment(testFeed, testUser, text);

        // then
        assertEquals(testFeed, comment.getFeed(), "댓글의 피드 정보가 올바르지 않습니다.");
        assertEquals(testUser, comment.getAuthor(), "댓글 작성자 정보가 올바르지 않습니다.");
        assertEquals(text, comment.getText(), "댓글 내용이 올바르지 않습니다.");
    }

    @DisplayName("댓글 작성 시 피드, 작성자, 또는 댓글 내용이 null인 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    void shouldThrowException_WhenAnyFieldIsNull(String text) {
        // given
        User testUser = createTestUser();
        Feed testFeed = createTestFeed(testUser);

        // when, then
        assertThrows(
                IllegalArgumentException.class,
                () -> Comment.writeNewComment(testFeed, testUser, text),
                "댓글 생성 시 null 값이 허용되지 않아야 합니다."
        );
    }

    @DisplayName("댓글 작성 시 댓글 내용이 공백 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldThrowException_WhenCommentTextIsBlank(String text) {
        // given
        User testUser = createTestUser();
        Feed testFeed = createTestFeed(testUser);

        // when, then
        assertThrows(
                IllegalArgumentException.class,
                () -> Comment.writeNewComment(testFeed, testUser, text),
                "댓글 내용이 공백 문자열일 경우 예외가 발생해야 합니다."
        );
    }

    private User createTestUser() {
        OAuthInfo oAuthInfo = OAuthInfo.createForKakao("1", "test-user");
        return User.createNewUserWithOAuth(oAuthInfo, "http://test-profile-image.com");
    }

    private Feed createTestFeed(User user) {
        String title = "Test Feed Title";
        String category = "Test Category";
        String contents = "Test Feed Contents";
        return Feed.writeNewFeed(title, contents, category, user);
    }
}
