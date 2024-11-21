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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
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
    private final FavoritesService favoritesService;

    public ChatbotResponse TextResponse(ConversationRequest request, int userNo) throws JsonProcessingException {
        ChatbotResponse response;
        String input = request.getInput();

        // 이전 대화내용 조회
        List<Log> conversationLogs = conversationRoomService.findLastNByConversationRoomNo(1, request.getConversationRoomNo());

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
                response.setSubTaskNo(subTaskNo);
            }

            // 송금하기 서비스
            case "003" -> {

                response = transferService.generateTransferConversation(input, conversationLogs);
                response.setSubTaskNo(subTaskNo);
                log.info("response json 객체 확인 : {}" , response);
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonString = response.getContent()
                        .replaceAll("```json", "")
                        .replaceAll("```", "")
                        .trim();
                JsonNode jsonNode = objectMapper.readTree(jsonString);
                Integer step = jsonNode.get("step").asInt();
                System.out.println("step : 값은 " + step);
                response.setStep(step);
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
                        System.out.println("여기로 오는건가??");
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
                    response.setContent(step2_content_string);

                }

            }

            // 통장 재발행 서비스
            case "004" -> {
                response = reissuanceService.generateReissuanceConversation(input, conversationLogs);
                response.setSubTaskNo(mainTaskNo);

            }

            // 통장 신규 생성 서비스
            case "005" -> {
                response = newissuanceService.generateNewissuanceConversation(input, conversationLogs);
                response.setSubTaskNo(mainTaskNo);
            }


            // 일상 대화
            default -> {
                response = dailyConversationTaskService.generateDailyConversation(input, conversationLogs);
            }
        }

        // 전체 token 계산
        tokenService.calculateToken(response, classificationResult);

        log.info("🔗2️⃣ [{}] Response generated for: {}", userNo, response.getSubTaskNo());
        return response;
    }
}
