package dev.office.networkoffice.global.config;

import dev.office.networkoffice.auth.dto.PhoneVerificationDetails;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * 휴대폰 인증 코드를 임시 저장하기 위한 RedisTemplate 빈 등록입니다.
     */
    @Bean
    public RedisTemplate<String, PhoneVerificationDetails> verificationCodeTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, PhoneVerificationDetails> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(PhoneVerificationDetails.class));
        return template;
    }

    /**
     * 조회수 기능을 위한 RedisTemplate 빈 등록입니다.
     */
    @Bean
    public RedisTemplate<String, Long> feedVisitTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        return template;
    }
}
