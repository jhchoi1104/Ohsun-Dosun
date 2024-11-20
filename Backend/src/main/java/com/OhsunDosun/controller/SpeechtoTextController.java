package com.OhsunDosun.controller;

import com.OhsunDosun.dto.TranscriptionRequest;
import com.OhsunDosun.service.stt.OpenAISttService;
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

    private final OpenAISttService openAISttService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createTranscription(@ModelAttribute TranscriptionRequest transcriptionRequest){
        return openAISttService.createTranscription(transcriptionRequest);
    }

}