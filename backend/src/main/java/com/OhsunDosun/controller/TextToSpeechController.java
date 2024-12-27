package com.OhsunDosun.controller;

import com.OhsunDosun.dto.TranscriptionRequest;
import com.OhsunDosun.service.conversation.TextToSpeechService;
import com.OhsunDosun.service.stt.OpenAISttService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tts")
public class TextToSpeechController {
    private final TextToSpeechService textToSpeechService;


    @PostMapping
    public String createTranscription(@RequestBody String input){

        // 음성 데이터 생성
        byte[] audioData = textToSpeechService.convertTextToSpeech(input);

        // 오디오 데이터를 Base64로 인코딩
        String audioBase64 = Base64.getEncoder().encodeToString(audioData);

        return audioBase64;
    }
}
