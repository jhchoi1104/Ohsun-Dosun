package com.OhsunDosun.kakao.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.MediaType;  // MediaType 추가

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate restTemplate;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-url}")
    private String tokenUrl;

    @Value("${kakao.user-info-url}")
    private String userInfoUrl;

    // 액세스 토큰 요청 (POST 방식)
    public String getAccessToken(String code) {
        try {
            // code가 URL에 포함되어 있는 경우, URL 디코딩을 시도
            code = URLDecoder.decode(code, StandardCharsets.UTF_8);
            log.info("디코딩된 코드: {}", code);

            // 액세스 토큰 요청에 필요한 파라미터 설정
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", clientId);
            params.add("redirect_uri", redirectUri);
            params.add("code", code);

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // x-www-form-urlencoded 지정

            // 요청 엔티티(HttpEntity) 생성 (본문과 헤더 포함)
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            log.info("Access Token 요청: URL={}, params={}", tokenUrl, params);

            // 카카오 토큰 URL에 POST 요청을 보냄
            ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, String.class);
            log.info("Access Token 응답: {}", response.getBody());

            // JSON 응답 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            log.error("카카오 액세스 토큰 요청 실패", e);
            throw new RuntimeException("카카오 액세스 토큰 요청 실패");
        }
    }

    // 사용자 정보 요청 (GET 방식)
    public Map<String, Object> getUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            log.info("사용자 정보 요청: URL={}", userInfoUrl);

            // 사용자 정보 요청
            ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);
            log.info("사용자 정보 응답: {}", response.getBody());

            // 응답 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            // 사용자 정보 파싱
            Map<String, Object> userInfo = new HashMap<>();

            JsonNode kakaoAccountNode = jsonNode.get("kakao_account");
            if (kakaoAccountNode != null && kakaoAccountNode.has("email")) {
                userInfo.put("username", kakaoAccountNode.get("email").asText());
            } else {
                userInfo.put("username", "No email available");  // 이메일이 없으면 기본값 설정
            }

            JsonNode propertiesNode = jsonNode.get("properties");
            if (propertiesNode != null && propertiesNode.has("nickname")) {
                userInfo.put("name", propertiesNode.get("nickname").asText());
            } else {
                userInfo.put("name", "Unknown");  // nickname이 없을 경우 기본값 설정
            }

            return userInfo;
        } catch (Exception e) {
            log.error("카카오 사용자 정보 요청 실패", e);
            throw new RuntimeException("카카오 사용자 정보 요청 실패");
        }
    }
}
