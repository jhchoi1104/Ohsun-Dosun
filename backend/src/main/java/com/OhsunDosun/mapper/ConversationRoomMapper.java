package com.OhsunDosun.mapper;

import com.OhsunDosun.dto.Log;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConversationRoomMapper {
    boolean readConversationRoomByConversationRoomNo(int sessionId);

    List<Log> findLastNByConversationRoomNo(@Param("number") int number, @Param("sessionId") int sessionId);
}
