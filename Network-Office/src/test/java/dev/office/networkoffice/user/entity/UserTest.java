package dev.office.networkoffice.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {

    @Test
    @DisplayName("OAuth2로 가입한 사용자는 프로필 이미지 URL이 없으면 예외가 발생해야 한다.")
    void shouldThrowExceptionWhenProfileImageUrlIsEmptyOrNull() {
        // given
        OAuthInfo oAuthInfo = OAuthInfo.createForKakao("1", "test");

        // when, then
        assertThrows(IllegalArgumentException.class, () -> User.createNewUserWithOAuth(oAuthInfo, ""));
        assertThrows(IllegalArgumentException.class, () -> User.createNewUserWithOAuth(oAuthInfo, null));
    }

    @Test
    @DisplayName("OAuth2로 가입한 사용자는 OAuth 정보가 없으면 예외가 발생해야 한다.")
    void shouldThrowExceptionWhenOAuthInfoIsNull() {
        // when, then
        assertThrows(IllegalArgumentException.class, () -> User.createNewUserWithOAuth(null, "test"));
    }

    @Test
    @DisplayName("처음 생성된 사용자의 인증 상태는 false여야 한다.")
    void shouldReturnFalseWhenUserIsNotVerified() {
        // given
        User user = new User();

        // when
        boolean isVerified = user.isVerified();

        // then
        assertFalse(isVerified);
    }

    @Test
    @DisplayName("휴대폰 번호를 인증하면 사용자의 인증 상태가 true로 변경되어야 한다.")
    void shouldReturnTrueWhenUserIsVerified() {
        // given
        User user = new User();

        // when
        user.verifyPhoneNumber("01012345678");

        // then
        assertTrue(user.isVerified());
    }

    @Test
    @DisplayName("휴대폰 번호를 인증하면 사용자의 휴대폰 번호가 변경되어야 한다.")
    void shouldChangePhoneNumberWhenUserIsVerified() {
        // given
        User user = new User();
        String testPhoneNumber = "01012345678";

        // when
        user.verifyPhoneNumber(testPhoneNumber);

        // then
        assertEquals(testPhoneNumber, user.getPhoneNumber());
    }

    @Test
    @DisplayName("휴대폰 번호 없이 휴대폰 인증을 시도하면 예외가 발생해야 한다.")
    void shouldThrowExceptionWhenPhoneNumberIsEmptyOrNull() {
        // given
        User user = new User();

        // when, then
        assertThrows(IllegalArgumentException.class, () -> user.verifyPhoneNumber(""));
        assertThrows(IllegalArgumentException.class, () -> user.verifyPhoneNumber(null));
    }

    @Test
    @DisplayName("이미 인증된 사용자를 다시 인증하려고 할 때 예외가 발생해야 한다.")
    void shouldThrowExceptionWhenUserIsAlreadyVerified() {
        // given
        User user = new User();
        user.verifyPhoneNumber("01012345678");

        // when, then
        assertThrows(IllegalArgumentException.class, () -> user.verifyPhoneNumber("01012345678"));
    }
}