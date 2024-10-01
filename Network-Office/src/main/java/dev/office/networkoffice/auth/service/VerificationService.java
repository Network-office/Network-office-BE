package dev.office.networkoffice.auth.service;

import dev.office.networkoffice.auth.client.VerificationEmailClient;
import dev.office.networkoffice.auth.dto.PhoneVerificationDetails;
import dev.office.networkoffice.user.entity.User;
import dev.office.networkoffice.user.repository.UserRepository;
import dev.office.networkoffice.auth.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final VerificationEmailClient emailClient;

    @Transactional
    public PhoneVerificationDetails generateVerificationCode(Long userId, String phoneNumber) {
        User user = getUser(userId);
        checkAlreadyVerify(user);
        checkAlreadyUsedPhoneNumber(phoneNumber);
        String verificationCode = generateRandomCode();
        verificationCodeRepository.save(userId, phoneNumber, verificationCode);

        return new PhoneVerificationDetails(verificationCode, phoneNumber);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private void checkAlreadyUsedPhoneNumber(String phoneNumber) {
        boolean existsPhoneNumber = userRepository.existsByPhoneNumber(phoneNumber);
        if (existsPhoneNumber) {
            throw new IllegalArgumentException("이미 등록된 전화번호입니다.");
        }
    }

    private void checkAlreadyVerify(User user) {
        if (user.isVerified()) {
            throw new IllegalArgumentException("이미 인증된 사용자입니다.");
        }
    }

    private String generateRandomCode() {
        return randomUUID().toString();
    }

    @Transactional
    public void verifyPhoneNumber(Long userId, String inputPhoneNumber) {
        User user = getUser(userId);
        checkAlreadyVerify(user);
        checkPhoneNumberAndVerify(userId, inputPhoneNumber);
        user.verifyPhoneNumber(inputPhoneNumber);
        verificationCodeRepository.deleteByUserId(userId);
    }

    private void checkPhoneNumberAndVerify(Long userId, String inputPhoneNumber) {
        PhoneVerificationDetails phoneVerificationDetails = getPhoneVerificationDetails(userId);
        String savedPhoneNumber = phoneVerificationDetails.phoneNumber();
        checkPhoneNumberMatch(inputPhoneNumber, savedPhoneNumber);

        String inputCode = emailClient.getVerificationCode(inputPhoneNumber);
        String savedCode = phoneVerificationDetails.code();
        checkVerificationCode(inputCode, savedCode);
    }

    private PhoneVerificationDetails getPhoneVerificationDetails(Long userId) {
        PhoneVerificationDetails phoneVerificationDetails = verificationCodeRepository.findByUserId(userId);
        if (phoneVerificationDetails == null) {
            throw new IllegalArgumentException("인증 코드가 만료되었습니다.");
        }
        return phoneVerificationDetails;
    }

    private void checkPhoneNumberMatch(String inputPhoneNumber, String savedPhoneNumber) {
        if (!inputPhoneNumber.equals(savedPhoneNumber)) {
            throw new IllegalArgumentException("인증 코드를 요청한 전화번호와 사용자가 입력한 전화번호가 일치하지 않습니다.");
        }
    }

    private void checkVerificationCode(String inputCode, String savedCode) {
        if (!inputCode.equals(savedCode)) {
            throw new IllegalArgumentException("발급된 인증 코드와 사용자가 전송한 인증 코드가 일치하지 않습니다.");
        }
    }
}
