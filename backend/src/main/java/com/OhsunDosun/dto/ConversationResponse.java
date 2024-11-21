package com.OhsunDosun.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {
    private String content;
    private int totalTokens;
    private String audioData;
    private int step;
    private String subTask;
    private String name;
    private String amount;
}
