package com.OhsunDosun.service.conversation.task;

import com.OhsunDosun.dto.ChatbotResponse;
import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.dto.Log;
import com.OhsunDosun.service.ConversationRoomService;
import com.OhsunDosun.service.conversation.ChainService;
import com.OhsunDosun.service.conversation.PromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SummaryTextService {
    private final ConversationRoomService conversationRoomService;
    private final PromptService promptService;
    private final ChainService chainService;

    public ChatbotResponse generateSummary(String input) {
        List<String> promptFilePathList = Arrays.asList("prompts/summary.prompt");
        List<Log> conversationLogs = Collections.emptyList();
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, input, conversationLogs);
        return chainService.chatbotPlainChain(chatbotPrompt);
    }
}
