package com.OhsunDosun.service.conversation;

import com.OhsunDosun.dto.ChatbotResponse;
import com.OhsunDosun.dto.ClassificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenService {
    public void calculateToken(ChatbotResponse response, ClassificationResponse classificationResult) {
        log.debug("🪙 Classification Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", classificationResult.getPromptTokens(), classificationResult.getCompletionTokens(), classificationResult.getTotalTokens());
        log.debug("🪙 Chatbot Tokens - PromptTokens: {}, CompletionTokens: {}, TotalTokens: {}", response.getPromptTokens(), response.getCompletionTokens(), response.getTotalTokens());

        response.setPromptTokens(response.getPromptTokens() + classificationResult.getPromptTokens());
        response.setCompletionTokens(response.getCompletionTokens() + classificationResult.getCompletionTokens());
        response.setTotalTokens(response.getTotalTokens() + classificationResult.getTotalTokens());
    }
}
