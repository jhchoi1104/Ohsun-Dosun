package com.OhsunDosun.mapper;

import com.OhsunDosun.dto.ConversationLogRequest;
import org.apache.ibatis.annotations.Param;

public interface CreateConversationLogMapper {
    void createConversationLogUser(ConversationLogRequest request);
    void createConversationLogBot(ConversationLogRequest request);
    void updateTimestamp(Long conversationRoomNo);
}
