package com.OhsunDosun.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationResponse {
    private String mainTaskNumber;
    private String subTaskNumber;
    private Boolean taskLocked;
    private int promptTokens;
    private int completionTokens;
    private int totalTokens;
}
