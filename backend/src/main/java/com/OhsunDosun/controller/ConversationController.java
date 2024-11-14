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

    // 말동무 대화 답변 생성(사용자 말하기 버튼 -> 중지 버튼 눌리게 되면 controller 호출)
    // 만약 처음 상황이라면 "Greeting"을 Requestbody의 reqeust input에 넣어주어야 함. -> front에서 request input 데이터 "Greeting" 으로 넣기
    @PostMapping
    public ResponseEntity<ConversationResponse> conversation(
            @RequestHeader("userId") int userNo,
            @RequestBody ConversationRequest request,
            HttpServletRequest httpServletRequest) {

        long startTime = System.currentTimeMillis();
        ConversationResponse response = conversationService.conversation(request, userNo);

        log.info("log check : {}", response);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("📌 Chatbot response: {}", duration);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);

    }

    //003.b 예금주 정보 확인 controller(예금 정보 입력 후 확인 버튼 눌리게 되면 controller 호출)

    //003.c 돈이 빠져나가는 계좌 list 중에서 선택하기 위한 계좌 리스트 조회 controller(계좌 정보 리스트 요청 시 controller 호출)

    //010.4 모든 정보 확인 후 이체를 위해 통장 비밀번호 조회 및 이체 controller(통장 비밀번호 입력 완료 시 controller 호출)

    //003.f 송금 대상 별칭 저장 controller(송금 완료 후 사용자 별칭 입력 및 저장 후 controller 호출)

}
