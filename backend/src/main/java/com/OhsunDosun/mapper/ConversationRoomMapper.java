package com.OhsunDosun.mapper;

import com.OhsunDosun.dto.Log;

import java.util.List;

public interface ConversationRoomMapper {
    boolean readConversationRoomByConversationRoomNo(int sessionId);

    List<Log> findLastNByConversationRoomNo(int number, int sessionId);
}
