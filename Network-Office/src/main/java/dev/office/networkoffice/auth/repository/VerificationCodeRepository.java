package dev.office.networkoffice.auth.repository;

import dev.office.networkoffice.auth.dto.PhoneVerificationDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class VerificationCodeRepository {

    private static final String VERIFICATION_CODE_NAMESPACE = "verification-code:";
    private static final Duration EXPIRATION = Duration.ofMinutes(10);

    private final RedisTemplate<String, PhoneVerificationDetails> verificationCodeTemplate;

    public void save(Long userId, String phoneNumber, String verificationCode) {
        PhoneVerificationDetails code = new PhoneVerificationDetails(verificationCode, phoneNumber);
        String key = VERIFICATION_CODE_NAMESPACE + userId.toString();
        verificationCodeTemplate.opsForValue()
                .set(key, code, EXPIRATION);
    }

    public PhoneVerificationDetails findByUserId(Long userId) {
        String key = VERIFICATION_CODE_NAMESPACE + userId.toString();
        return verificationCodeTemplate.opsForValue()
                .get(key);
    }

    public void deleteByUserId(Long userId) {
        String key = VERIFICATION_CODE_NAMESPACE + userId.toString();
        verificationCodeTemplate.delete(key);
    }
}
