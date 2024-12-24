package com.OhsunDosun.service.conversation;

import com.OhsunDosun.dto.ChatCompletionChunk;
import com.OhsunDosun.dto.ChatbotResponse;
import com.OhsunDosun.dto.ConversationLogRequest;
import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.exception.ChatbotException;
import com.OhsunDosun.service.ConversationLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microsoft.cognitiveservices.speech.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotService {

    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    @Value("${MODEL_NAME}")
    private String modelName;

    @Value("${MINI_MODEL_NAME}")
    private String miniModelName;

    @Value("${SPEECH_KEY}")
    private String speechKey;

    @Value("${SPEECH_REGION}")
    private String speechRegion;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final ConversationLogService conversationLogService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5); // 최대 5개의 스레드


    /**
     * <pre>
     * 메소드명   : sendRequest
     * 설명       : OpenAI API에 POST 요청을 보내고 응답을 받아 처리한다.
     * </pre>
     * @param messagesList 챗봇에 전송할 메시지 리스트
     * @param responseSchema 응답 스키마 (필요한 경우)
     * @return ChatbotResponse 챗봇의 응답 데이터
     */
    public void sendRequest(ConversationRequest request, String model,
                            List<Map<String, String>> messagesList,
                            Map<String, Object> responseSchema,
                            WebSocketSession session) {
        try {
            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // 요청 본문 데이터 생성
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", messagesList.toArray(new Map[0]));

            // Response Format 생성
            if (responseSchema != null) {
                Map<String, Object> jsonSchema = createJsonSchema(responseSchema);
                requestBody.put("response_format", jsonSchema);
            }

            // HTTP Entity 생성
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            printChatbotRequest(requestBody);

            // RestTemplate 객체 생성
            RestTemplate restTemplate = new RestTemplate();

            // API 호출 및 응답 처리
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    UriComponentsBuilder.fromHttpUrl(API_URL).toUriString(),
                    requestEntity,
                    String.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // OpenAI 응답 처리
                String content = parseOpenAIResponse(responseEntity.getBody());
                session.sendMessage(new TextMessage(content));

                // 챗봇 대화 내역 검사
                ConversationLogRequest conversationLog = makeConversationLogRequest(request, content);

                // 챗봇 대화 내역 저장
                conversationLogService.createConversationLog(conversationLog);

                executorService.submit(() -> handleTtsStreaming(content, session));
            } else {
                throw new RuntimeException("Failed to get STT response: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error during OpenAI request", e);
            try {
                session.sendMessage(new TextMessage("Error: Unable to process your request."));
            } catch (Exception sendError) {
                log.error("Error sending error message via WebSocket", sendError);
            }
        }
    }


    /**
     * sendRequestClassification
     */
    private ChatbotResponse sendRequestPlain(String model, List<Map<String, String>> messagesList, Map<String, Object> responseSchema) {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 요청 본문 데이터 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);

        // messagesList를 배열로 변환하여 requestBody에 추가
        requestBody.put("messages", messagesList.toArray(new Map[0]));

        // Response Format 생성
        if (responseSchema != null) {
            Map<String, Object> jsonSchema = createJsonSchema(responseSchema);
            requestBody.put("response_format", jsonSchema);
        }

        // HTTP Entity 생성
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        printChatbotRequest(requestBody);

        // RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // API 호출 및 응답 받기
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl(API_URL).toUriString(),
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // 응답 상태 코드 확인 및 처리
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            try {
                log.debug("📍 Chatbot Response:\n{}", responseEntity.getBody());
                return parseResponse(responseEntity.getBody());
            } catch (Exception e) {
                throw new ChatbotException("Failed to parse chatbot response", e);
            }
        }

        return ChatbotResponse.builder()
                .content("Error: " + responseEntity.getStatusCode())
                .build();
    }



    /**
     * <pre>
     * 메소드명   : getChatbotResponse
     * 설명       : 응답 스키마 없이 OpenAI API를 호출하여 챗봇 응답을 받아온다.
     * </pre>
     * @param messagesList 챗봇에 전송할 메시지 리스트
     * @return ChatbotResponse 챗봇의 응답 데이터
     */
    public void getChatbotResponse(ConversationRequest request, List<Map<String, String>> messagesList, WebSocketSession session) {
        sendRequest(request, modelName, messagesList, null, session);
    }

    /**
     * json 형태로 내보내는 경우의 응답
     */
    public ChatbotResponse getChatbotResponse(List<Map<String, String>> messagesList) {
        return sendRequestPlain(modelName, messagesList, null);
    }


    /**
     * classification을 위한
     */
    public ChatbotResponse getClassificationResult(List<Map<String, String>> messagesList, Map<String, Object> responseSchema) {
        return sendRequestPlain(modelName, messagesList, null);
    }

    /**
     * <pre>
     * 메소드명   : createJsonSchema
     * 설명       : JSON 응답 형식에 필요한 스키마를 생성한다.
     * </pre>
     * @param properties 스키마에 포함될 프로퍼티들
     * @return Map<String, Object> JSON 스키마를 포함한 응답 형식
     */
    private Map<String, Object> createJsonSchema(Map<String, Object> properties) {
        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");
        schema.put("strict", true);

        schema.put("properties", properties);

        // properties 맵의 모든 키를 필수 필드로 설정
        List<String> requiredFields = new ArrayList<>(properties.keySet());
        schema.put("required", requiredFields);

        Map<String, Object> jsonSchema = new HashMap<>();
        jsonSchema.put("name", "TaskNumberSchema");  // 스키마 이름 추가
        jsonSchema.put("schema", schema);  // 여기에 추가

        Map<String, Object> responseFormat = new HashMap<>();
        responseFormat.put("type", "json_schema");
        responseFormat.put("json_schema", jsonSchema);

        return responseFormat;
    }

    /**
     * <pre>
     * 메소드명   : printChatbotRequest
     * 설명       : 최종 JSON 요청 본문을 로깅한다.
     * </pre>
     * @param requestBody 요청 본문 데이터
     */
    private void printChatbotRequest(Map<String, Object> requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonRequestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody);
            log.debug("📍 Final JSON Request Body:\n{}", jsonRequestBody);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert request body to JSON: {}", e.getMessage());
        }
    }

    /**
     * <pre>
     * 메소드명   : parseResponse
     * 설명       : JSON 응답을 파싱하여 ChatbotResponse 객체로 변환한다.
     * </pre>
     * @param jsonResponse API로부터 받은 JSON 응답 문자열
     * @return ChatbotResponse 파싱된 챗봇 응답 데이터
     * @throws Exception JSON 파싱 실패 시 발생
     */
    private ChatbotResponse parseResponse(String jsonResponse) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // 필요한 필드 추출
        String content = rootNode.path("choices").get(0).path("message").path("content").asText();
        int promptTokens = rootNode.path("usage").path("prompt_tokens").asInt();
        int completionTokens = rootNode.path("usage").path("completion_tokens").asInt();
        int totalTokens = rootNode.path("usage").path("total_tokens").asInt();

        // DTO로 변환
        return ChatbotResponse.builder()
                .content(content)
                .promptTokens(promptTokens)
                .completionTokens(completionTokens)
                .totalTokens(totalTokens)
                .build();
    }

    private void handleTtsStreaming(String text, WebSocketSession session) {
        try {
            // Azure Speech SDK 설정
            SpeechConfig speechConfig = SpeechConfig.fromSubscription(speechKey, speechRegion);
            speechConfig.setSpeechSynthesisVoiceName("ko-KR-YuJinNeural");

            // SpeechSynthesizer 생성
            SpeechSynthesizer synthesizer = new SpeechSynthesizer(speechConfig);

            // SSML 텍스트로 음성 속도를 설정
            String ssml = "<speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xml:lang='ko-KR'>" +
                    "<voice name='ko-KR-YuJinNeural'>" +
                    "<prosody rate='+10%'>" + text + "</prosody>" +
                    "</voice>" +
                    "</speak>";

            // 비동기적으로 SSML 텍스트를 음성으로 변환
            Future<SpeechSynthesisResult> future = synthesizer.SpeakSsmlAsync(ssml);

            // 결과 가져오기
            SpeechSynthesisResult result = future.get();


            if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                byte[] audioData = result.getAudioData(); // 음성 데이터를 가져옴

                // WebSocket으로 음성 데이터 전송
                session.sendMessage(new BinaryMessage(audioData));
                log.info("Sent audio data to client");
            } else if (result.getReason() == ResultReason.Canceled) {
                SpeechSynthesisCancellationDetails cancellationDetails =
                        SpeechSynthesisCancellationDetails.fromResult(result);
                log.error("TTS canceled: {}", cancellationDetails.getErrorDetails());
            }

            // SpeechSynthesizer 리소스 정리
            synthesizer.close();

        } catch (ExecutionException | InterruptedException e) {
            log.error("Error during TTS synthesis", e);
        } catch (IOException e) {
            log.error("Error sending audio data via WebSocket", e);
        }
    }




    private String parseOpenAIResponse(String responseBody) {
        try {
            // JSON 응답 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // 응답에서 content 텍스트 추출 (OpenAI 응답 구조에 따라 조정)
            if (rootNode.has("choices")) {
                JsonNode choicesNode = rootNode.get("choices");
                if (choicesNode.isArray() && choicesNode.size() > 0) {
                    JsonNode firstChoice = choicesNode.get(0);
                    if (firstChoice.has("message") && firstChoice.get("message").has("content")) {
                        return firstChoice.get("message").get("content").asText();
                    }
                }
            }
            log.warn("OpenAI response format is not as expected: {}", responseBody);
            return "Unexpected response format.";
        } catch (JsonProcessingException e) {
            log.error("Error parsing OpenAI response", e);
            return "Error parsing response.";
        }
    }

    /**
     * 로그로 저장 가능한지 검사
     */
    private ConversationLogRequest makeConversationLogRequest(ConversationRequest request, String content) {
        ConversationLogRequest conversationLog;

        if (content == null || content.isEmpty()) {
            log.warn("⚠️ Chatbot response is empty: content={}", content);
            conversationLog = ConversationLogRequest.builder()
                    .conversationLogInput(request.getInput())
                    .conversationLogResponse("문제가 발생했습니다. 다시 한번 말해주세요.")
                    .conversationRoomNo(request.getConversationRoomNo())
                    .build();
        } else {
            conversationLog = ConversationLogRequest.builder()
                    .conversationLogInput(request.getInput())
                    .conversationLogResponse(content)
                    .conversationRoomNo(request.getConversationRoomNo())
                    .build();
        }
        return conversationLog;
    }

}
