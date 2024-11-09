package com.OhsunDosun.service;

import java.util.Base64;
import java.util.concurrent.TimeoutException;
import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.dto.ConversationResponse;
import com.OhsunDosun.exception.ConversationRoomNotFoundException;
import com.OhsunDosun.dto.ChatbotResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRoomService conversationRoomService;

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
        conversationRoomService.updateConversationRoomEndAt(request.getConversationRoomNo());

        // 음성 데이터 생성
        byte[] audioData = textToSpeechService.convertTextToSpeech(conversationLog.getConversationLogResponse());

        // 오디오 데이터를 Base64로 인코딩
        String audioBase64 = Base64.getEncoder().encodeToString(audioData);

        ConversationResponse.RedirectionResult redirectionResult = null;
        if (response.getRedirectionResult() != null){
            redirectionResult = ConversationResponse.RedirectionResult.builder()
                    .serviceName(response.getRedirectionResult().getServiceName())
                    .serviceNumber(response.getRedirectionResult().getServiceNumber())
                    .serviceUrl(response.getRedirectionResult().getServiceUrl())
                    .build();
        }

        ConversationResponse.ReservationResult reservationResult = null;
        if (response.getReservationResult() != null) {
            reservationResult = ConversationResponse.ReservationResult.builder()
                    .welfareNo(response.getReservationResult().getServiceTypeNumber())
                    .welfareBookStartDate(response.getReservationResult().getReservationDate())
                    .welfareBookEndDate(response.getReservationResult().getReservationDate())
                    .welfareBookUseTime(response.getReservationResult().getReservationTimeNumber())
                    .build();
        }

        return ConversationResponse.builder()
                .content(response.getContent())
                .audioData(audioBase64)
                .actionRequired(response.isActionRequired())
                .totalTokens(response.getTotalTokens())
                .redirectionResult(redirectionResult)
                .reservationResult(reservationResult)
                .build();
    }


}
