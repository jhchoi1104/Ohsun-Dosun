package com.OhsunDosun.mapper;

import com.OhsunDosun.dto.ConversationLogRequest;

public interface CreateConversationLogMapper {
    void createConversationLogUser(ConversationLogRequest request);
    void createConversationLogBot(ConversationLogRequest request);
}
