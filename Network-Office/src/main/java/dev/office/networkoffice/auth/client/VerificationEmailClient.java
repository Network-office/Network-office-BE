package dev.office.networkoffice.auth.client;

import jakarta.annotation.PostConstruct;
import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.search.ReceivedDateTerm;
import jakarta.mail.search.SearchTerm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 이메일 보관함에서 인증 코드를 가져오는 클라이언트입니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class VerificationEmailClient {

    private static final String VERIFICATION_CODE_PATTERN = "\\b[0-9a-fA-F\\-]{36}\\b"; // UUID 패턴

    @Value("${email.imap.server}")
    private String imapServer;

    @Value("${email.imap.port}")
    private int imapPort;

    @Value("${email.account}")
    private String emailAccount;

    @Value("${email.password}")
    private String password;

    private Session session;
    private Store store;

    @PostConstruct
    private void init() {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", imapServer);
        properties.put("mail.imaps.port", imapPort);
        properties.put("mail.imaps.ssl.enable", "true");
        session = Session.getDefaultInstance(properties);
    }

    public String getVerificationCode(String phoneNumber) {
        try {
            connect();
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            SearchTerm searchTerm = createSearchTerm();

            // 이메일 목록을 가져와 최신순으로 정렬합니다.
            Message[] messages = inbox.search(searchTerm);
            sortDesc(messages);

            // 특정 번호로부터 온 메일 중 가장 최신 메일을 찾습니다.
            Message latestMessage = findLatestMessageByPhoneNumber(messages, phoneNumber);

            // 메일 본문에서 인증 코드를 추출하여 반환합니다.
            String content = getTextFromMessage(latestMessage);
            return extractCertificationCode(content);
        } catch (Exception e) {
            throw new IllegalStateException("이메일 클라이언트에서 인증 코드를 가져오는 중 오류가 발생했습니다.", e);
        } finally {
            disconnect();
        }
    }

    private void sortDesc(Message[] messages) {
        Arrays.sort(messages, (m1, m2) -> {
            try {
                return m2.getSentDate().compareTo(m1.getSentDate());
            } catch (MessagingException e) {
                return 0;
            }
        });
    }

    private void connect() {
        try {
            store = session.getStore("imaps");
            store.connect(imapServer, emailAccount, password);
        } catch (MessagingException e) {
            throw new IllegalStateException("이메일 클라이언트 연결 중 오류가 발생했습니다.", e);
        }
    }

    private void disconnect() {
        try {
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (MessagingException e) {
            throw new IllegalStateException("이메일 클라이언트 연결을 종료하는 중 오류가 발생했습니다.", e);
        }
    }

    // 이메일을 가져올 시작 시간을 설정합니다.
    private SearchTerm createSearchTerm() {
        ZonedDateTime standard = ZonedDateTime.now(ZoneId.of("UTC"))
                .minusDays(1); // 하루 전부터 가져옵니다.
        Date startDate = Date.from(standard.toInstant());

        return new ReceivedDateTerm(ReceivedDateTerm.GT, startDate);
    }

    private Message findLatestMessageByPhoneNumber(Message[] messages, String phoneNumber) {
        return Arrays.stream(messages)
                .filter(message -> isSenderPhoneNumberMatch(message, phoneNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("인증 코드가 포함된 이메일을 찾을 수 없습니다."));
    }

    private boolean isSenderPhoneNumberMatch(Message message, String phoneNumber) {
        try {
            // 이메일의 발신자 주소를 확인합니다.
            // 이메일의 발신자 주소는 여러 개일 수 있습니다.
            Address[] fromAddresses = message.getFrom();
            if (fromAddresses == null || fromAddresses.length == 0) {
                return false;
            }

            // 발신자 주소가 사용자 휴대폰 번호로 시작하는지 확인합니다.
            String fromAddress = fromAddresses[0].toString();
            return fromAddress.startsWith(phoneNumber);
        } catch (MessagingException e) {
            return false;
        }
    }

    private String getTextFromMessage(Message message) throws IOException, MessagingException {
        StringBuilder contentBuilder = new StringBuilder();
        if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
                    contentBuilder.append(part.getContent().toString());
                }
            }
            return contentBuilder.toString();
        }

        throw new IllegalArgumentException("잘못된 이메일 형식입니다. (주어진 형태로 전송하지 않았거나, 첨부 파일이 포함되어 있습니다.)");
    }

    private String extractCertificationCode(String emailContent) {
        // 이메일 본문에서 인증 코드를 추출합니다.
        Pattern regex = Pattern.compile(VERIFICATION_CODE_PATTERN);
        Matcher matcher = regex.matcher(emailContent);
        return matcher.find() ? matcher.group() : null;
    }
}
