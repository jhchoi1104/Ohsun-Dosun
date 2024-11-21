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

    @GetMapping
        public List<ChatbotRoom> bringChatRoom() {
        return chatRoomService.bringChatRoom();
    }

    @GetMapping("/{id}")
    public List<Message> bringMessage(@PathVariable("id") long id) {

        List<Message> messages = chatRoomService.bringMessage(id);

        // ObjectMapper 생성 (Jackson을 이용해 JSON 파싱)
        ObjectMapper objectMapper = new ObjectMapper();

        // 메시지 텍스트 처리
        for (Message message : messages) {
            try {

                // 불필요한 문자 제거 (예: ```json … ```)
                String jsonString = message.getMessageText().replaceAll("```json", "").replaceAll("```", "").trim();
                // messageText가 JSON 형식이라면 파싱하여 content 필드만 추출
                JsonNode jsonNode = objectMapper.readTree(jsonString);
                if (jsonNode.has("content")) {
                    // content가 존재하면 그 값만 가져와서 새로 설정
                    message.setMessageText(jsonNode.get("content").asText());
                }
            } catch (Exception e) {
                // messageText가 JSON 형식이 아닌 경우는 그대로 둔다.
                // 예외 처리
            }
        }

        return messages;
    }

}
