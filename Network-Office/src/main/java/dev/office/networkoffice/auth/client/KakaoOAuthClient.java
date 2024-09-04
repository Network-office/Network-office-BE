package dev.office.networkoffice.auth.client;

import dev.office.networkoffice.auth.dto.KakaoTokenResponse;
import dev.office.networkoffice.auth.dto.KakaoUserResponse;
import dev.office.networkoffice.global.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.IOException;

@Component
public class KakaoOAuthClient {

    private final String redirectUri;
    private final String accessTokenRequestUri;
    private final String userInformationRequestUri;
    private final String restApiKey;
    private final RestClient restClient;

    public KakaoOAuthClient(
            @Value("${oauth.kakao.redirect-uri}") String redirectUri,
            @Value("${oauth.kakao.access-token-request-url}") String accessTokenRequestUri,
            @Value("${oauth.kakao.user-information-request-url}") String userInformationRequestUri,
            @Value("${oauth.kakao.rest-api-key}") String restApiKey) {
        this.redirectUri = redirectUri;
        this.accessTokenRequestUri = accessTokenRequestUri;
        this.userInformationRequestUri = userInformationRequestUri;
        this.restApiKey = restApiKey;
        this.restClient = initRestClient();
    }

    private RestClient initRestClient() {
        return RestClient.builder()
                .build();
    }

    public KakaoUserResponse requestUserInformation(String code) {
        KakaoTokenResponse tokenResponse = requestToken(code);
        return restClient.post()
                .uri(userInformationRequestUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.accessToken())
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleClientError)
                .toEntity(KakaoUserResponse.class)
                .getBody();
    }

    private KakaoTokenResponse requestToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", restApiKey);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        return restClient.post()
                .uri(accessTokenRequestUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleClientError)
                .toEntity(KakaoTokenResponse.class)
                .getBody();
    }

    private void handleClientError(HttpRequest request, ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError()) {
            throw new IllegalArgumentException("잘못된 요청입니다. 인가코드 및 리다이렉트 URI를 확인해주세요.");
        }
        throw new ExternalServiceException("카카오 서비스에 문제가 발생하여 요청을 처리할 수 없습니다.");
    }
}
