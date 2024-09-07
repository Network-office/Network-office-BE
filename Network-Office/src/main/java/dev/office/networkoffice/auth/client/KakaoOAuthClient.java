package dev.office.networkoffice.auth.client;

import dev.office.networkoffice.auth.dto.KakaoErrorResponse;
import dev.office.networkoffice.auth.dto.KakaoTokenResponse;
import dev.office.networkoffice.auth.dto.KakaoUserResponse;
import dev.office.networkoffice.global.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

@Slf4j
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
                .exchange((request, response) -> handleClientResponse(request, response, KakaoUserResponse.class));
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
                .exchange((request, response) -> handleClientResponse(request, response, KakaoTokenResponse.class));
    }

    private <T> T handleClientResponse(HttpRequest request,
                                       ConvertibleClientHttpResponse response,
                                       Class<T> responseType) throws IOException {
        if (is4xxError(response)) {
            KakaoErrorResponse errorResponse = getErrorResponse(response);
            throw new IllegalArgumentException("잘못된 요청입니다. 사유: " + errorResponse);
        }
        if (isError(response)) {
            String requestDetails = getRequestDetails(request);
            KakaoErrorResponse errorResponse = getErrorResponse(response);
            log.error("카카오 서비스 요청 중 오류가 발생했습니다. 요청: {}, 응답: {}", requestDetails, errorResponse);
            throw new ExternalServiceException("카카오 서비스에 문제가 발생하여 요청을 처리할 수 없습니다. 사유: " + errorResponse);
        }

        return Objects.requireNonNull(response.bodyTo(responseType));
    }

    private boolean is4xxError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    private boolean isError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    private String getRequestDetails(HttpRequest request) {
        URI uri = request.getURI();
        HttpMethod method = request.getMethod();
        HttpHeaders headers = request.getHeaders();

        return "{ [" + method + "] " + uri + ", headers: " + headers + " }";
    }

    private KakaoErrorResponse getErrorResponse(ConvertibleClientHttpResponse response) {
        return response.bodyTo(KakaoErrorResponse.class);
    }
}
