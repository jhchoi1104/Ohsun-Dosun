package com.OhsunDosun.service.conversation;


import com.OhsunDosun.dto.*;
import com.OhsunDosun.service.ConversationLogService;
import com.OhsunDosun.service.ConversationRoomService;
import com.OhsunDosun.service.conversation.task.*;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextResponseService {

    private final ConversationRoomService conversationRoomService;
    private final ClassificationTaskService classificationTaskService;
    private final GreetingTaskService greetingTaskService;
    private final DailyConversationTaskService dailyConversationTaskService;
    private final TransferService transferService;
    private final LoanService loanService;
    private final ConsultantService consultantService;
    private final NewissuanceService newissuanceService;
    private final ReissuanceService reissuanceService;
    private final FavoritesService favoritesService;
    private final ConversationLogService conversationLogService;
    private final ChatbotService chatbotService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void TextResponse(ConversationRequest request, int userNo, WebSocketSession session) throws IOException {
        String input = request.getInput();

        // 이전 대화내용 조회
        List<Log> conversationLogs = conversationRoomService.findLastNByConversationRoomNo(1, request.getConversationRoomNo());

        // 첫 인사 생성
        if (conversationLogs.isEmpty() && request.getInput().equals("Greeting")) {
            greetingTaskService.generateGreeting(request, userNo, session);
        }

        // 사용자 입력에 따른 작업 분류
        ClassificationResponse classificationResult = classificationTaskService.classificationTask(input, conversationLogs);
        String mainTaskNo = classificationResult.getMainTaskNumber();
        String subTaskNo = classificationResult.getSubTaskNumber();
        Integer step = 0;

        Boolean taskLocked = classificationResult.getTaskLocked();

        log.info("🔗1️⃣ [{}] Task Classification Completed by - Main Task No: \u001B[34m{}\u001B[0m, Sub Task No: \u001B[34m{}\u001B[0m", userNo, mainTaskNo, subTaskNo, taskLocked);

        // Main Task 분류
        switch (mainTaskNo) {
            // 대출 서비스
            case "001" -> {
                loanService.generateLoanConversation(request, conversationLogs, session);
            }

            //상담원 연결 서비스
            case "002" -> {
                consultantService.generateConsultConversation(request, conversationLogs, session);
            }

            // 송금하기 서비스
            case "003" -> {
                step = handleTransferTask(request, userNo, session, conversationLogs, subTaskNo);

            }

            // 통장 재발행 서비스
            case "004" -> {
                reissuanceService.generateReissuanceConversation(request, conversationLogs, session);

            }

            // 통장 신규 생성 서비스
            case "005" -> {
                newissuanceService.generateNewissuanceConversation(request, conversationLogs, session);
            }


            // 일상 대화
            default -> {
                dailyConversationTaskService.generateDailyConversation(request, conversationLogs, session);
            }
        }
        // classification 결과 전송
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode responseJson = objectMapper.createObjectNode();
            responseJson.put("mainTaskNo", mainTaskNo);
            responseJson.put("subTaskNo", subTaskNo);
            responseJson.put("step", step);

            // WebSocket으로 전송
            session.sendMessage(new TextMessage(responseJson.toString()));
            log.info("🔗2️⃣ [{}] Classification result sent: {}", userNo, responseJson);
        } catch (Exception e) {
            log.error("Error sending WebSocket message", e);
        }
    }
    private int handleTransferTask(ConversationRequest request, int userNo, WebSocketSession session, List<Log> conversationLogs, String subTaskNo) {
        int step = 0;
        try {
            ChatbotResponse response = transferService.generateTransferConversation(request, conversationLogs);
            response.setSubTaskNo(subTaskNo);

            if (response.getStep() == 2) {
                String recipientName = response.getName();
                boolean favoriteExists = favoritesService.isFavoriteExists((long) userNo, recipientName);

                if (favoriteExists) {
                    response.setContent(String.format("%s님에게 송금하시겠습니까?", recipientName));
                    response.setStep(2);
                } else {
                    response.setContent("송금하신 적 없는 분이네요. 계좌번호를 입력해주세요.");
                    response.setStep(3);
                }
            }

            session.sendMessage(new TextMessage(response.getContent()));
            ConversationLogRequest conversationLog = chatbotService.makeConversationLogRequest(request, response.getContent());
            conversationLogService.createConversationLog(conversationLog);
            executorService.submit(() -> chatbotService.handleTtsStreaming(response.getContent(), session));
            return response.getStep();
        } catch (Exception e) {
            log.error("Error during transfer task", e);
            try {
                session.sendMessage(new TextMessage("Error: Unable to process your request."));
            } catch (Exception sendError) {
                log.error("Error sending error message via WebSocket", sendError);
            }
        }
        return step;
    }

}
