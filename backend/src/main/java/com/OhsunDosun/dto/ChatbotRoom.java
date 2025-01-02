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
    private String startTime;
    private String endTime;
    private String serviceType;
    private String title;
}
