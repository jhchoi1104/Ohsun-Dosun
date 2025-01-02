package com.OhsunDosun.service;

import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.exception.ConversationRoomNotFoundException;
import com.OhsunDosun.service.conversation.TextResponseService;
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
    }

}

