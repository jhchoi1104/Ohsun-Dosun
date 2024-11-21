package com.OhsunDosun.service;

import java.util.Base64;
import java.util.concurrent.TimeoutException;

import com.OhsunDosun.dto.ConversationLogRequest;
import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.dto.ConversationResponse;
import com.OhsunDosun.exception.ConversationRoomNotFoundException;
import com.OhsunDosun.dto.ChatbotResponse;
import com.OhsunDosun.service.conversation.TextResponseService;
import com.OhsunDosun.service.conversation.TextToSpeechService;
import com.OhsunDosun.service.conversation.task.SummaryTextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
@Service
@Slf4j
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationLogService conversationLogService;
    private final ConversationRoomService conversationRoomService;
    private final TextResponseService textResponseService;
    private final TextToSpeechService textToSpeechService;
    private final SummaryTextService summaryTextService;

    public ConversationResponse conversation(ConversationRequest request, int userNo) {
        // 방 존재 여부 검사
        if(!conversationRoomService.readConversationRoomByConversationRoomNo(request.getConversationRoomNo())) {
            throw new ConversationRoomNotFoundException("ID가 " + request.getConversationRoomNo() + "인 대화방을 찾을 수 없습니다.");
        }

        // Chatbot 답변 생성
        ChatbotResponse response;
        try {
            response = executeWithTimeout(() -> textResponseService.TextResponse(request, userNo));
        } catch (TimeoutException e) {
            log.warn("⚠️ TextResponse timed out for input={}, conversationRoomNo={}", request.getInput(), request.getConversationRoomNo());
            response = ChatbotResponse.builder()
                    .content("응답 시간이 초과되었습니다. 다시 시도해 주세요.")
                    .build();
        } catch (Exception e) {
            log.error("❌ Exception occurred while getting TextResponse", e);
            response = ChatbotResponse.builder()
                    .content("문제가 발생했습니다. 다시 시도해 주세요.")
                    .build();
        }

        // Chatbot 답변 검사
        ConversationLogRequest conversationLog = makeConversationLogRequest(request, response);


        // 대화 내역 저장
        conversationLogService.createConversationLog(conversationLog);
        // 송금하기는 json으로 필드값의 정보를 받아오고, 그 외의 기능에서는 String으로 된 chatbot의 응답을 return 할 예정

        String content = response.getContent();

        if (content.trim().startsWith("{") || content.trim().startsWith("```"))  {
            // JSON 형식일 경우
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                // 불필요한 문자 제거 (예: ```json ... ```)
                String jsonString = content.replaceAll("```json", "").replaceAll("```", "").trim();
                // JSON 파싱
                JsonNode jsonNode = objectMapper.readTree(jsonString);
                String extractedContent = jsonNode.get("content").asText();

                if (extractedContent.length() > 120) {
                    ChatbotResponse summaryResponse = summaryTextService.generateSummary(extractedContent);
                    extractedContent = summaryResponse.getContent();
                }

                // 음성 데이터 생성
                byte[] audioData = textToSpeechService.convertTextToSpeech(extractedContent);

                // 오디오 데이터를 Base64로 인코딩
                String audioBase64 = Base64.getEncoder().encodeToString(audioData);
                int extractedstep = jsonNode.get("step").asInt();

                return ConversationResponse.builder()
                        .content(extractedContent)
                        .totalTokens(response.getTotalTokens())
                        .audioData(audioBase64)
                        .step(extractedstep)
                        .subTask(response.getSubTaskNo())
                        .build();
            } catch (IOException e) {
                log.error("JSON 파싱 오류: content 필드에서 값을 추출할 수 없습니다.", e);
                return ConversationResponse.builder()
                        .content("JSON 파싱 오류가 발생했습니다.")
                        .totalTokens(response.getTotalTokens())
                        .build();
            }
        } else {


            byte[] audioData;

            if (content.length() > 120) {
                ChatbotResponse summaryResponse = summaryTextService.generateSummary(content);
                String summaryContent = summaryResponse.getContent();

                System.out.println(summaryContent);
                // 음성 데이터 생성
                audioData = textToSpeechService.convertTextToSpeech(summaryContent);
            } else {
                // 음성 데이터 생성
                audioData = textToSpeechService.convertTextToSpeech(content);
            }

            // 오디오 데이터를 Base64로 인코딩
            String audioBase64 = Base64.getEncoder().encodeToString(audioData);

            // JSON이 아닌 경우는 단순 메시지로 처리
            return ConversationResponse.builder()
                    .content(content)
                    .totalTokens(response.getTotalTokens())
                    .audioData(audioBase64)
                    .subTask(response.getSubTaskNo())
                    .build();
        }
    }

    private ConversationLogRequest makeConversationLogRequest(ConversationRequest request, ChatbotResponse response) {
        ConversationLogRequest conversationLog;
        String content = response.getContent();
        if (content == null || content.isEmpty()) {
            log.warn("⚠️ Chatbot response is empty: content={}, totalTokens={}", response.getContent(), response.getTotalTokens());
            conversationLog = ConversationLogRequest.builder()
                    .conversationLogInput(request.getInput())
                    .conversationLogResponse("문제가 발생했습니다. 다시 한번 말해주세요.")
                    .conversationLogToken(response.getTotalTokens())
                    .conversationRoomNo(request.getConversationRoomNo())
                    .build();
        } else {
            conversationLog = ConversationLogRequest.builder()
                    .conversationLogInput(request.getInput())
                    .conversationLogResponse(response.getContent())
                    .conversationLogToken(response.getTotalTokens())
                    .conversationRoomNo(request.getConversationRoomNo())
                    .build();
        }
        return conversationLog;
    }

    private <T> T executeWithTimeout(Callable<T> task)
            throws TimeoutException, Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<T> future = executor.submit(task);

        try {
            return future.get(7, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);  // 작업 취소
            throw e;  // 타임아웃 예외를 다시 던짐
        } finally {
            executor.shutdown();
        }
    }


}

