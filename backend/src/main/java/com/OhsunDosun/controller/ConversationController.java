package com.OhsunDosun.controller;

import com.OhsunDosun.dto.ConversationRequest;
import com.OhsunDosun.dto.ConversationResponse;
import com.OhsunDosun.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversation")
public class ConversationController {

    private final ConversationService conversationService;

    // 말동무 대화 답변 생성
    @PostMapping
    public ResponseEntity<ConversationResponse> conversation(
            @RequestHeader("userId") int userNo,
            @RequestBody ConversationRequest request,
            HttpServletRequest httpServletRequest) {

        long startTime = System.currentTimeMillis();
        ConversationResponse response = conversationService.conversation(request, userNo);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("📌 Chatbot response: ", duration);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);

    }

}
