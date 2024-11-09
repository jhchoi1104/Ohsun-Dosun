package com.OhsunDosun.mapper;

import java.util.List;

public interface ConversationRoomMapper {
    boolean readConversationRoomByConversationRoomNo(int sessionId);

    List<String> findLastNByConversationRoomNo(int number, int sessionId);
}
