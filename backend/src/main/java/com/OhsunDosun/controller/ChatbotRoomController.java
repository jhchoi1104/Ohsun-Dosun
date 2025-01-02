package com.OhsunDosun.controller;

import com.OhsunDosun.dto.ChatbotRoom;
import com.OhsunDosun.dto.Message;
import com.OhsunDosun.service.ChatRoom.ChatRoomService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    /**
     *
     * @return 챗봇 대화방 리스트
     */
    @GetMapping
        public List<ChatbotRoom> bringChatRoom() {
        return chatRoomService.bringChatRoom();
    }

    /**
     *
     * @param id
     * @return 개별 챗봇 대화 내역 리스트
     */
    @GetMapping("/{id}")
    public List<Message> bringMessage(@PathVariable("id") long id) {
        List<Message> messages = chatRoomService.bringMessage(id);
        ObjectMapper objectMapper = new ObjectMapper();
        extracted(messages, objectMapper);
        return messages;
    }

    // 메시지 텍스트 처리
    private static void extracted(List<Message> messages, ObjectMapper objectMapper) {
        for (Message message : messages) {
            try {
                // json 형식 처리
                String jsonString = message.getMessageText().replaceAll("```json", "").replaceAll("```", "").trim();
                JsonNode jsonNode = objectMapper.readTree(jsonString);
                if (jsonNode.has("content")) {
                    message.setMessageText(jsonNode.get("content").asText());
                }
            } catch (Exception e) {
                // json 형식이 아닌 경우
            }
        }
    }

}
