package com.OhsunDosun.service.conversation.task;

import com.OhsunDosun.dto.ClassificationResponse;
import com.OhsunDosun.dto.Log;
import com.OhsunDosun.service.conversation.ChainService;
import com.OhsunDosun.service.conversation.PromptService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClassificationTaskService {

    private final PromptService promptService;
    private final ChainService chainService;

    public ClassificationResponse classificationTask(String input, List<Log> conversationLogs) throws JsonProcessingException {
        List<Map<String, String>> classificationPrompt = promptService.classificationPrompt(input, conversationLogs);
        return chainService.classificationChain(classificationPrompt);
    }

}
