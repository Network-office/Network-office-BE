package dev.office.networkoffice.feed.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.office.networkoffice.user.entity.OAuthInfo;
import dev.office.networkoffice.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Comment 엔티티 객체 테스트")
class CommentTest {

    private User testUser;
    private Feed testFeed;
    private String validText;

    @BeforeEach
    void setUp() {
        testUser = createTestUser();
        testFeed = createTestFeed(testUser);
        validText = "Valid Comment.";
    }

    @Nested
    @DisplayName("댓글 생성 테스트")
    class CommentCreationTest {

        @Test
        @DisplayName("유효한 입력값으로 댓글이 정상 생성된다")
        void shouldCreateCommentSuccessfully() {
            // when
            Comment comment = Comment.writeNewComment(testFeed, testUser, validText);

            // then
            assertAll(
                    "댓글 생성 검증",
                    () -> assertThat(comment.getFeed()).isEqualTo(testFeed),
                    () -> assertThat(comment.getAuthor()).isEqualTo(testUser),
                    () -> assertThat(comment.getText()).isEqualTo(validText)
            );
        }

        @Test
        @DisplayName("피드가 null인 경우 예외가 발생한다")
        void shouldThrowException_WhenFeedIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Comment.writeNewComment(null, testUser, validText),
                    "피드가 null일 때 예외가 발생해야 합니다"
            );
        }

        @Test
        @DisplayName("작성자가 null인 경우 예외가 발생한다")
        void shouldThrowException_WhenAuthorIsNull() {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Comment.writeNewComment(testFeed, null, validText),
                    "작성자가 null일 때 예외가 발생해야 합니다"
            );
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "   "})
        @DisplayName("댓글 내용이 null이거나 공백인 경우 예외가 발생한다")
        void shouldThrowException_WhenTextIsNullOrBlank(String invalidText) {
            // when & then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Comment.writeNewComment(testFeed, testUser, invalidText),
                    "댓글 내용이 null이거나 공백일 때 예외가 발생해야 합니다"
            );
        }
    }

    @Nested
    @DisplayName("댓글 작성자 확인 테스트")
    class CommentAuthorshipTest {

        @Test
        @DisplayName("동일한 작성자인 경우 true를 반환한다")
        void shouldReturnTrue_WhenAuthorMatches() {
            // given
            Comment comment = Comment.writeNewComment(testFeed, testUser, validText);

            // when
            boolean isCreatedByUser = comment.isCreatedBy(testUser);

            // then
            assertThat(isCreatedByUser).isTrue();
        }

        @Test
        @DisplayName("다른 작성자인 경우 false를 반환한다")
        void shouldReturnFalse_WhenAuthorDoesNotMatch() {
            // given
            Comment comment = Comment.writeNewComment(testFeed, testUser, validText);
            OAuthInfo oAuthInfo = OAuthInfo.createForKakao("2", "test-user-2");
            User differentUser = User.createNewUserWithOAuth(oAuthInfo, "http://test-profile-image.com");

            // when
            boolean isCreatedByUser = comment.isCreatedBy(differentUser);

            // then
            assertThat(isCreatedByUser).isFalse();
        }

        @Test
        @DisplayName("null 작성자 검사 시 false를 반환한다")
        void shouldReturnFalse_WhenCheckedWithNullAuthor() {
            // given
            Comment comment = Comment.writeNewComment(testFeed, testUser, validText);

            // when
            boolean isCreatedByUser = comment.isCreatedBy(null);

            // then
            assertThat(isCreatedByUser).isFalse();
        }
    }

    private User createTestUser() {
        OAuthInfo oAuthInfo = OAuthInfo.createForKakao("1", "test-user");
        return User.createNewUserWithOAuth(oAuthInfo, "http://test-profile-image.com");
    }

    private Feed createTestFeed(User user) {
        String title = "Test Title";
        String category = "Test Category";
        String contents = "Test Feed Contents";
        return Feed.writeNewFeed(title, contents, category, user);
    }
}
