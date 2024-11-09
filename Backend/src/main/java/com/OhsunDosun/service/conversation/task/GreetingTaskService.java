package com.OhsunDosun.service.conversation.task;

import com.OhsunDosun.dto.ChatbotResponse;
import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.dto.Log;
import com.OhsunDosun.service.ConversationRoomService;
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
public class GreetingTaskService {

    private final ConversationRoomService conversationRoomService;
    private final PromptService promptService;
    private final ChainService chainService;

    public ChatbotResponse generateGreeting(ConversationRequest request, int userNo) {
        List<Log> conversationLogList = conversationRoomService.findLastNByConversationRoomNo(5, request.getConversationRoomNo());
        String conversationLogListString = conversationLogList != null && !conversationLogList.isEmpty()
                ? "\nPrevious Conversation:\n" + String.join("\n", conversationLogList.stream()
                .map(log -> log.getMessageText())  // getMessageText() 사용 가능
                .collect(Collectors.toList()))
                : "";

        List<String> promptFilePathList = Arrays.asList("prompts/basic.prompt", "prompts/greeting.prompt");
        List<Log> conversationLogs = Collections.emptyList();
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, "", conversationLogs, conversationLogListString);
        return chainService.chatbotChain(chatbotPrompt);
    }
}
