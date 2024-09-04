package dev.office.networkoffice.global.exception;

/**
 * 인증이 필요한 서비스에 인증되지 않은 사용자가 접근하려고 할 때 발생하는 예외입니다.
 * @See org.springframework.http.HttpStatus.UNAUTHORIZED
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}