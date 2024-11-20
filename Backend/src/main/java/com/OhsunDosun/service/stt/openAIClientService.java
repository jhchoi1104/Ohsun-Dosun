package com.OhsunDosun.service.stt;

import com.OhsunDosun.dto.TranscriptionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class OpenAIClientService {

    private String API_URL = "https://api.openai.com/v1/audio/transcriptions";

    @Value("${OPENAI_API_KEY}")
    private String API_KEY;

    private String model = "whisper-1";

    public String createTranscription(TranscriptionRequest transcriptionRequest) {
        try {
            // RestTemplate 객체 생성
            RestTemplate restTemplate = new RestTemplate();

            // 파일과 파라미터를 담을 MultiValueMap 생성
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("model", model);
            body.add("file", transcriptionRequest.getFile().getResource()); // MultipartFile을 Resource로 변환하여 추가

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA); // multipart/form-data
            headers.setBearerAuth(API_KEY); // Authorization: Bearer <API_KEY>

            // 요청 엔티티 생성
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // URL 생성
            String url = UriComponentsBuilder.fromHttpUrl(API_URL).toUriString();

            // POST 요청 보내기
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // 응답 반환
            return responseEntity.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }
}
