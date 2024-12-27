package com.OhsunDosun.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotResponse {
    private String content;
    private int promptTokens;
    private int completionTokens;
    private int totalTokens;
    private String subTaskNo;
    private int step;
    private String name;
    private String account;
    private int amount;
    private String main_account;
}
