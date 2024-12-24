package com.OhsunDosun.service;

import com.OhsunDosun.dto.ConversationLogRequest;
import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.exception.ConversationRoomNotFoundException;
import com.OhsunDosun.dto.ChatbotResponse;
import com.OhsunDosun.service.conversation.TextResponseService;
import com.OhsunDosun.service.conversation.TextToSpeechService;
import com.OhsunDosun.service.conversation.task.SummaryTextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRoomService conversationRoomService;
    private final TextResponseService textResponseService;

    public void conversation(ConversationRequest request, int userNo, WebSocketSession session) {
        // 방 존재 여부 검사
        if(!conversationRoomService.readConversationRoomByConversationRoomNo(request.getConversationRoomNo())) {
            throw new ConversationRoomNotFoundException("ID가 " + request.getConversationRoomNo() + "인 대화방을 찾을 수 없습니다.");
        }

        try {
            textResponseService.TextResponse(request, userNo, session);
        } catch (Exception e) {
            log.error("❌ Exception occurred while getting TextResponse", e);
        }

        // Chatbot 답변 검사
//        ConversationLogRequest conversationLog = makeConversationLogRequest(request, response);


        // 대화 내역 저장
//        conversationLogService.createConversationLog(conversationLog);
        // 송금하기는 json으로 필드값의 정보를 받아오고, 그 외의 기능에서는 String으로 된 chatbot의 응답을 return 할 예정

    }

}

