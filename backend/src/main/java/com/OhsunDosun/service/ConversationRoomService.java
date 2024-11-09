package com.OhsunDosun.service;

import com.OhsunDosun.mapper.ConversationRoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConversationRoomService {
    private final ConversationRoomMapper conversationRoomMapper;

    public boolean readConversationRoomByConversationRoomNo(int sessionId) {
        return conversationRoomMapper.readConversationRoomByConversationRoomNo(sessionId);
    }

    public List<String> findLastNByConversationRoomNo(int number, int sessionId) {
        return conversationRoomMapper.findLastNByConversationRoomNo(number, sessionId);
    }
}
