package com.OhsunDosun.service.conversation.task;

import com.OhsunDosun.dto.ChatbotResponse;
import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.dto.Log;
import com.OhsunDosun.dto.TransferRequest;
import com.OhsunDosun.mapper.TransferMapper;
import com.OhsunDosun.service.conversation.ChainService;
import com.OhsunDosun.service.conversation.PromptService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class TransferService {
    private final PromptService promptService;
    private final ChainService chainService;

    public ChatbotResponse generateTransferConversation(ConversationRequest request, List<Log> conversationLogs) throws JsonProcessingException {
        List<String> promptFilePathList = Collections.singletonList("prompts/transfer.prompt");
        List<Map<String, String>> chatbotPrompt = promptService.chatbotPrompt(promptFilePathList, request.getInput(), conversationLogs);
        return chainService.chatbotPlainChain(chatbotPrompt);
    }

    @Autowired
    private TransferMapper transferMapper;

    // 송금하기
    @Transactional
    public void transferMoney(TransferRequest transferRequest) {
        // 송금 내역을 기록
        transferMapper.insertTransferHistory(transferRequest);

        // 송금하는 사람의 잔액 업데이트
        transferMapper.updateAccountBalances(transferRequest);

        // 송금받는 사람의 잔액 업데이트
        transferMapper.updateReceiverBalance(transferRequest);
    }
}