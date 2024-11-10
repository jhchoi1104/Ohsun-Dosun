package com.OhsunDosun.service;

import com.OhsunDosun.dto.ConversationLogRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.OhsunDosun.mapper.CreateConversationLogMapper;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConversationLogService {
    private final CreateConversationLogMapper createConversationLogMapper;

    @Transactional
    public void createConversationLog(ConversationLogRequest request) {
        createConversationLogMapper.createConversationLogUser(request);
        createConversationLogMapper.createConversationLogBot(request);
    }
}
