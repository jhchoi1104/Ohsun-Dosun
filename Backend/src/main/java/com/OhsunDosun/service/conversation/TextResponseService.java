package com.OhsunDosun.service.conversation;


import com.OhsunDosun.dto.ChatbotResponse;
import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.service.ConversationRoomService;
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
    private final WelfareTaskService welfareTaskService;
    private final FinanceTaskService financeTaskService;

    public ChatbotResponse TextResponse(ConversationRequest request, int userNo) throws JsonProcessingException {
        ChatbotResponse response;
        String input = request.getInput();

        // 이전 대화내용 조회
        List<String> conversationLogs = conversationRoomService.findLastNByConversationRoomNo(5, request.getConversationRoomNo());

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
        log.info("🔗1️⃣ [{}] Task Classification Completed by - Main Task No: \u001B[34m{}\u001B[0m, Sub Task No: \u001B[34m{}\u001B[0m", user.getUserId(), mainTaskNo, subTaskNo);

        // Main Task 분류
        switch (mainTaskNo) {
            // 복지 서비스
            case "001" -> {
                response = welfareTaskService.generateWelfareService(subTaskNo, input, conversationLogs, user);
            }
            // 금융 서비스
            case "002" -> {
                response = financeTaskService.generateFinancialService(subTaskNo, input, conversationLogs, user);
            }
            // 일상 대화
            default -> {
                response = dailyConversationTaskService.generateDailyConversation(input, conversationLogs);
            }
        }

        // 전체 token 계산
        tokenService.calculateToken(response, classificationResult);

        log.info("🔗2️⃣ [{}] Response generated for: {}", user.getUserId(), response.getContent());
        return response;
    }
}
