package com.OhsunDosun.service.ChatRoom;

import com.OhsunDosun.dto.ChatbotRoom;
import com.OhsunDosun.dto.Message;
import com.OhsunDosun.mapper.ChatRoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomMapper chatRoomMapper;

    public List<ChatbotRoom> bringChatRoom() {
        return chatRoomMapper.bringChatRoom();
    }

    public List<Message> bringMessage(long id) {
        return chatRoomMapper.bringMessage(id);
    }

}
