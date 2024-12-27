package com.OhsunDosun.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long messageId; // 메시지 ID
    private String sessionId; // 세션 ID
    private String senderType; // 발신자 유형 (ex: chatbot, user 등)
    private String messageText; // 메시지 내용
    private String timestamp; // 메시지가 생성된 시간
}
