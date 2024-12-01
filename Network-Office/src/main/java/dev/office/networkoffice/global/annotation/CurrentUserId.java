package dev.office.networkoffice.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 현재 로그인한 사용자의 ID를 가져오기 위한 어노테이션입니다.
 * @see dev.office.networkoffice.global.resolver.LoginUserArgumentResolver
 * @see dev.office.networkoffice.global.config.WebConfig
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface CurrentUserId {
}
