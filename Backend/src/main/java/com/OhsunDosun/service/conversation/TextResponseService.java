package com.OhsunDosun.service.conversation;


import com.OhsunDosun.dto.ChatbotResponse;
import com.OhsunDosun.dto.ClassificationResponse;
import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.dto.Log;
import com.OhsunDosun.service.ConversationRoomService;
import com.OhsunDosun.service.conversation.task.ClassificationTaskService;
import com.OhsunDosun.service.conversation.task.DailyConversationTaskService;
import com.OhsunDosun.service.conversation.task.GreetingTaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextResponseService {

    private final ConversationRoomService conversationRoomService;
    private final TokenService tokenService;

    private final ClassificationTaskService classificationTaskService;
    private final GreetingTaskService greetingTaskService;
    private final DailyConversationTaskService dailyConversationTaskService;

    public ChatbotResponse TextResponse(ConversationRequest request, int userNo) throws JsonProcessingException {
        ChatbotResponse response;
        String input = request.getInput();

        // 이전 대화내용 조회
        List<Log> conversationLogs = conversationRoomService.findLastNByConversationRoomNo(5, request.getConversationRoomNo());

        // 첫 인사 생성
        if (conversationLogs.isEmpty() && request.getInput().equals("Greeting")) {
            response = greetingTaskService.generateGreeting(request, userNo);
            log.info("🖐️ [{}] Greeting generated for: {}", userNo, response.getContent());
            return response;
        }

        // 사용자 입력에 따른 작업 분류
        ClassificationResponse classificationResult = classificationTaskService.classificationTask(input, conversationLogs);
        String mainTaskNo = classificationResult.getMainTaskNumber();
        String subTaskNo = classificationResult.getSubTaskNumber();
        log.info("🔗1️⃣ [{}] Task Classification Completed by - Main Task No: \u001B[34m{}\u001B[0m, Sub Task No: \u001B[34m{}\u001B[0m", userNo, mainTaskNo, subTaskNo);

        // Main Task 분류
        switch (mainTaskNo) {
            // 복지 서비스
            case "001" -> {
                response = ChatbotResponse.builder()
                        .content("안녕하세요. 저는 1번 케이스입니다.")
                        .build();;
            }
            // 금융 서비스
            case "002" -> {
                response = ChatbotResponse.builder()
                        .content("안녕하세요. 저는 2번 케이스입니다.")
                        .build();;
            }
            // 일상 대화
            default -> {
                response = dailyConversationTaskService.generateDailyConversation(input, conversationLogs);
            }
        }

        // 전체 token 계산
        tokenService.calculateToken(response, classificationResult);

        log.info("🔗2️⃣ [{}] Response generated for: {}", userNo, response.getContent());
        return response;
    }
}
