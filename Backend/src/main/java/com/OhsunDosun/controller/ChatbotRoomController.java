package com.OhsunDosun.controller;

import com.OhsunDosun.dto.ChatbotRoom;
import com.OhsunDosun.dto.Message;
import com.OhsunDosun.service.ChatRoom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatbotRoom")
public class ChatbotRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping
    public List<ChatbotRoom> bringChatRoom() {
        return chatRoomService.bringChatRoom();
    }

    @GetMapping("/{id}")
    public List<Message> bringMessage(@PathVariable("id") long id) {
        return chatRoomService.bringMessage(id);
    }

}
