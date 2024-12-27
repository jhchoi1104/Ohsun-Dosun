package com.OhsunDosun.Handler;

import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.dto.ConversationResponse;
import com.OhsunDosun.service.ConversationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConversationWebSocketHandler extends TextWebSocketHandler {

    private final ConversationService conversationService;
    private final ObjectMapper objectMapper; // JSON 파싱용

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket connection established. Session ID: {}", session.getId());
        // 필요하다면 session을 사용자 식별자와 매핑하는 로직 추가 가능
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            log.info("Received WebSocket message: {}", payload);

            // JSON -> ConversationRequest 변환
            ConversationRequest request = objectMapper.readValue(payload, ConversationRequest.class);
            int userId = request.getUserId();
            String userMessage = request.getInput();

            log.info("Parsed WebSocket message - userId: {}, userMessage: {}", userId, userMessage);

            // 서비스 호출 시 session 전달
            System.out.println("request: " + request);
            System.out.println("userId: " + userId);
            System.out.println("session: " + session);
            conversationService.conversation(request, userId, session);

        } catch (Exception e) {
            log.error("Error processing WebSocket message", e);
            session.sendMessage(new TextMessage("Error processing your request."));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket transport error:", exception);
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket connection closed. Session ID: {}, Status: {}", session.getId(), status);
        // 세션 종료 처리 로직
    }
}
