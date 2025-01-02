package com.OhsunDosun.mapper;

import com.OhsunDosun.dto.ChatbotRoom;
import com.OhsunDosun.dto.Message;

import java.util.List;

public interface ChatRoomMapper {
    List<ChatbotRoom> bringChatRoom();
    List<Message> bringMessage(long id);
}
