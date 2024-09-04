package dev.office.networkoffice.global.exception;

/**
 * 외부 서비스 호출 중 예외가 발생한 경우 발생하는 예외입니다.
 * 예를 들어, 소셜 로그인 API 호출 중 500 에러가 발생한 경우 이 예외를 발생시킵니다.
 * @See org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
 */
public class ExternalServiceException extends RuntimeException {

    public ExternalServiceException(String message) {
        super(message);
    }
}
