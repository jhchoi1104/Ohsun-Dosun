package com.OhsunDosun.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotRoom {
    private int sessionId;
    private String startTime;    // 시작 시간
    private String endTime;      // 종료 시간
    private String serviceType;  // 서비스 유형
    private String title;        // 제목
}
