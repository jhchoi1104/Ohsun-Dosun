package com.OhsunDosun.service.conversation.task;

import com.OhsunDosun.dto.ChatbotResponse;
import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.dto.Log;
import com.OhsunDosun.service.conversation.ChainService;
import com.OhsunDosun.service.conversation.PromptService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class LoanService {
    private final PromptService promptService;
    private final ChainService chainService;

    public void generateLoanConversation(ConversationRequest request, List<Log> conversationLogs, WebSocketSession session) throws JsonProcessingException {
        List<String> promptFilePathList = Collections.singletonList("prompts/loan.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, request.getInput(), conversationLogs);
        chainService.chatbotChain(request, chatbotPrompt, session);
    }
}
