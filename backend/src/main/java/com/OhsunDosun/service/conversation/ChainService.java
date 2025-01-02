package com.OhsunDosun.service.conversation;

import com.OhsunDosun.dto.ClassificationResponse;
import com.OhsunDosun.dto.ConversationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.OhsunDosun.dto.ChatbotResponse;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChainService {
    final ChatbotService chatbotService;

    /**
     * <pre>
     * 메소드명   : classificationChain
     * 설명       : 주어진 프롬프트에 따라 메인 작업 번호와 서브 작업 번호를 분류하는 체인 호출.
     * </pre>
     * @param prompt 챗봇에 전송할 메시지 리스트
     * @return ClassificationResponse 메인 작업 번호 및 서브 작업 번호를 포함하는 응답 객체
     * @throws JsonProcessingException JSON 처리 중 오류 발생 시
     */
    public ClassificationResponse classificationChain(List<Map<String, String>> prompt) throws JsonProcessingException {
        Map<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("mainTaskNumber", Map.of("type", "string"));
        responseSchema.put("subTaskNumber", Map.of("type", "string", "nullable", true));

        ChatbotResponse response = chatbotService.getClassificationResult(prompt, responseSchema);
        String cleanContent = response.getContent()
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(cleanContent);

        return ClassificationResponse.builder()
                .mainTaskNumber(rootNode.path("mainTaskNumber").asText().trim())
                .subTaskNumber(rootNode.path("subTaskNumber").asText().trim())
                .promptTokens(response.getPromptTokens())
                .completionTokens(response.getCompletionTokens())
                .totalTokens(response.getTotalTokens())
                .build();
    }

    /**
     * <pre>
     * 메소드명   : classificationChain
     * 설명      : 송금 관련 챗봇 응답 처리
     * </pre>
     * @param prompt
     * @return
     * @throws JsonProcessingException
     */
    public ChatbotResponse chatbotPlainChain(List<Map<String, String>> prompt) throws JsonProcessingException {
        Map<String, Object> responseSchema = new HashMap<>();
        responseSchema.put("name", Map.of("type", "string", "nullable", true));
        responseSchema.put("account", Map.of("type", "string", "nullable", true));
        responseSchema.put("amount", Map.of("type", "string", "nullable", true));
        responseSchema.put("main_account", Map.of("type", "string", "nullable", true));
        responseSchema.put("step", Map.of("type", "integer", "nullable", true));
        responseSchema.put("content", Map.of("type", "string"));

        ChatbotResponse response = chatbotService.getChatbotResponse(prompt, responseSchema);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getContent());
        ChatbotResponse responseInfo = ChatbotResponse.builder()
                .name(rootNode.path("name").asText().trim())
                .account(rootNode.path("account").asText().trim())
                .amount(rootNode.path("amount").asInt())
                .main_account(rootNode.path("main_account").asText().trim())
                .step(rootNode.path("step").asInt())
                .content(rootNode.path("content").asText().trim())
                .build();

        return responseInfo;
    }
    /**
     * <pre>
     * 메소드명   : chatbotChain
     * 설명       : 응답 스키마 없이 일반적인 챗봇 응답을 처리하는 체인 호출.
     * </pre>
     * @param prompt 챗봇에 전송할 메시지 리스트
     * @return ChatbotResponse 챗봇 응답 객체
     */
    public void chatbotChain(ConversationRequest request, List<Map<String, String>> prompt, WebSocketSession session) {
        chatbotService.getChatbotResponse(request, prompt, session);
    }
}
