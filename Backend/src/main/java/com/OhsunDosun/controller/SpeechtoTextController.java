package com.OhsunDosun.controller;

import com.OhsunDosun.service.stt.SpeechService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/speech")
public class SpeechtoTextController {
    @Autowired
    SpeechService speechService;
    @PostMapping
    public ResponseEntity<?> handleUpload(@RequestParam("file") MultipartFile file) {
        File tempInputFile = null;
        String tempOutputFilePath = null;

        try {
            // 임시 파일 생성
            tempInputFile = Files.createTempFile("upload_", ".wav").toFile();
            // 임시 출력 파일 경로만 생성
            tempOutputFilePath = Files.createTempDirectory("temp_audio").resolve("converted_audio.wav").toString();

            // 업로드된 파일을 임시 입력 파일로 저장
            try (FileOutputStream fos = new FileOutputStream(tempInputFile)) {
                fos.write(file.getBytes());
            }

            // ffmpeg 변환 실행
            boolean success = convertWithFfmpeg(tempInputFile.getAbsolutePath(), tempOutputFilePath);
            if (success) {
                // 변환 완료된 파일을 File 객체로 반환 가능
                File convertedFile = new File(tempOutputFilePath);

                // 변환 완료 후 텍스트 변환 실행
                String speechRecognitionResult = speechService.convertSpeechToTextFromffmpeg(convertedFile);
                System.out.println("Speech Recognition Result: " + speechRecognitionResult);

                String extractedText = extractTextFromResult(speechRecognitionResult);
                System.out.println("extractedText : " + extractedText);
                return ResponseEntity.ok().body(Map.of("text", extractedText));
            } else {
                return ResponseEntity.status(500).body("error");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        } finally {
            // 임시 입력 파일 삭제
            if (tempInputFile != null && tempInputFile.exists()) {
                tempInputFile.delete();
            }
        }
    }

    private boolean convertWithFfmpeg(String inputFilePath, String outputFilePath) {
        String ffmpegPath = "/opt/homebrew/bin/ffmpeg"; // 실제 ffmpeg 경로
        String command = String.format("%s -i %s -acodec pcm_s16le -ar 16000 %s",
                ffmpegPath, inputFilePath, outputFilePath);

        try {
            Process process = new ProcessBuilder(command.split(" "))
                    .inheritIO()
                    .start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    // JSON 응답에서 text 필드 추출
    private String extractTextFromResult(String result) {
        try {
            // JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(result);

            // text 필드 추출
            if (root.has("text")) {
                return root.get("text").asText();
            } else {
                return "텍스트를 추출할 수 없습니다.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "JSON 파싱 오류: " + e.getMessage();
        }
    }

    private String getFfmpegPath() {
        // 프로젝트 내 bin 폴더의 경로를 상대경로로 지정
        String projectRoot = new File("").getAbsolutePath(); // 현재 프로젝트의 루트 경로
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return projectRoot + "\\bin\\ffmpeg.exe";  // Windows용 경로
        } else if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
            return projectRoot + "/bin/ffmpeg";  // macOS/Linux용 경로
        } else {
            throw new UnsupportedOperationException("운영체제를 지원하지 않습니다.");
        }
    }


}