package com.OhsunDosun.service.conversation.task;

import com.OhsunDosun.dto.ChatbotResponse;
import com.OhsunDosun.dto.Log;
import com.OhsunDosun.service.conversation.ChainService;
import com.OhsunDosun.service.conversation.PromptService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class ReissuanceService {
    private final PromptService promptService;
    private final ChainService chainService;

    public ChatbotResponse generateReissuanceConversation(String input, List<Log> conversationLogs) throws JsonProcessingException {
        List<String> promptFilePathList = Collections.singletonList("prompts/ReIssued.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);
        return chainService.chatbotChain(chatbotPrompt);
    }
}