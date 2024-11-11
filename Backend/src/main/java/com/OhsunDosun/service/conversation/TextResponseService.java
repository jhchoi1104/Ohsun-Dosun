package com.OhsunDosun.service.conversation;


import com.OhsunDosun.dto.ChatbotResponse;
import com.OhsunDosun.dto.ClassificationResponse;
import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.dto.Log;
import com.OhsunDosun.service.ConversationRoomService;
import com.OhsunDosun.service.conversation.task.ClassificationTaskService;
import com.OhsunDosun.service.conversation.task.DailyConversationTaskService;
import com.OhsunDosun.service.conversation.task.GreetingTaskService;
import com.OhsunDosun.service.conversation.task.TransferService;
import com.OhsunDosun.service.conversation.task.LoanService;
import com.OhsunDosun.service.conversation.task.ConsultantService;
import com.OhsunDosun.service.conversation.task.NewissuanceService;
import com.OhsunDosun.service.conversation.task.ReissuanceService;
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
    private final TransferService transferService;
    private final LoanService loanService;
    private final ConsultantService consultantService;
    private final NewissuanceService newissuanceService;
    private final ReissuanceService reissuanceService;
    public ChatbotResponse TextResponse(ConversationRequest request, int userNo) throws JsonProcessingException {
        ChatbotResponse response;
        String input = request.getInput();

        // 이전 대화내용 조회
        List<Log> conversationLogs = conversationRoomService.findLastNByConversationRoomNo(10, request.getConversationRoomNo());

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
        Boolean taskLocked = classificationResult.getTaskLocked();
        log.info("🔗1️⃣ [{}] Task Classification Completed by - Main Task No: \u001B[34m{}\u001B[0m, Sub Task No: \u001B[34m{}\u001B[0m", userNo, mainTaskNo, subTaskNo, taskLocked);

        // Main Task 분류
        switch (mainTaskNo) {
            // 대출 서비스
            case "001" -> {
                response = loanService.generateLoanConversation(input, conversationLogs);
            }

            //상담원 연결 서비스
            case "002" -> {
                response = consultantService.generateConsultConversation(input, conversationLogs);
            }

            // 송금하기 서비스
            case "003" -> {
                response = transferService.generateTransferConversation(input, conversationLogs);

            }

            // 통장 재발행 서비스
            case "004" -> {
                response = reissuanceService.generateReissuanceConversation(input, conversationLogs);
            }

            // 통장 신규 생성 서비스
            case "005" -> {
                response = newissuanceService.generateNewissuanceConversation(input, conversationLogs);
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
