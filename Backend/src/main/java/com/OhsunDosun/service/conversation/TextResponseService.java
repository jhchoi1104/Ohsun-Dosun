package com.OhsunDosun.service.conversation;


import com.OhsunDosun.dto.ChatbotResponse;
import com.OhsunDosun.dto.ClassificationResponse;
import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.dto.Log;
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

    public void TextResponse(ConversationRequest request, int userNo, WebSocketSession session) throws JsonProcessingException {
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

                ChatbotResponse response = transferService.generateTransferConversation(request, conversationLogs);
                log.info("response json 객체 확인 : {}" , response);

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonString = response.getContent()
                        .replaceAll("```json", "")
                        .replaceAll("```", "")
                        .trim();
                JsonNode jsonNode = objectMapper.readTree(jsonString);
                step = jsonNode.get("step").asInt();

                if(step == 2){
                    String step2_content_name = jsonNode.get("name").asText();
                    Long userId = Long.valueOf(userNo); // 현재 유저의 ID로 설정

                    // 별칭 존재 여부 확인
                    boolean favoriteExists = favoritesService.isFavoriteExists(userId, step2_content_name);

                    // 사용자가 금액에 대한 정보를 미리 말한 경우와 그렇지 않은 경우 구분하기 위함
                    JSONObject step2_content_json = new JSONObject();


                    // DB user 이름 혹은 별칭 조회
                    String step2_content_message;
                    if (favoriteExists) { //존재하는 경우 003.a.01
                        step2_content_message = String.format("%s님에게 송금하시겠습니까?", step2_content_name);
                        step2_content_json.put("step", 2);
                    } else { //존재하지 않을 경우 003.a.02
                        step2_content_message = "송금하신적 없는 분이네요. 계좌번호를 입력해주세요.";
                        step2_content_json.put("step", 3);

                    }

                    step2_content_json.put("content", step2_content_message);

                    String name = jsonNode.get("name").asText();
                    if(!name.isEmpty()){
                        step2_content_json.put("name", name);
                    } else{
                        step2_content_json.put("name","");
                    }

                    String amount = jsonNode.get("amount").asText();
                    if(!amount.isEmpty()){
                        step2_content_json.put("amount", amount);
                    } else{
                        step2_content_json.put("amount","");
                    }
                    String step2_content_string = step2_content_json.toString();

                    try {
                        session.sendMessage(new TextMessage(step2_content_string));
                    }  catch (IOException e) {
                        log.error("JSON 파싱 오류: content 필드에서 값을 추출할 수 없습니다.", e);
                    }
                }

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
}
