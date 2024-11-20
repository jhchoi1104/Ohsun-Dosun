package com.OhsunDosun.controller;

import com.OhsunDosun.dto.TranscriptionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.OhsunDosun.service.stt.OpenAIClientService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/speech")
public class SpeechtoTextController {

    private final OpenAIClientService openAIClientService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createTranscription(@ModelAttribute TranscriptionRequest transcriptionRequest){
        return openAIClientService.createTranscription(transcriptionRequest);
    }

}