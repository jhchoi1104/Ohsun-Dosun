package com.OhsunDosun.controller;

import com.OhsunDosun.service.stt.SpeechService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        String ffmpegPath =  getFfmpegPath();
//        String ffmpegPath = "/opt/homebrew/bin/ffmpeg"; // 실제 ffmpeg 경로
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

    // ffmpeg 경로를 절대 경로로 설정
    private String getFfmpegPath() {
        // 운영체제별 경로 설정
        String os = System.getProperty("os.name").toLowerCase();
        String ffmpegPath = "";


        String resource = extractFfmpegFromClasspath();
        System.out.println("프로젝트 루트 경로: " + resource);

        return resource;
    }
    private static String extractFfmpegFromClasspath() {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            // 운영체제에 따라 ffmpeg 파일 이름 결정
            String ffmpegFileName = "ffmpeg";  // 기본적으로 macOS/Linux용
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                ffmpegFileName = "ffmpeg.exe";  // Windows용 ffmpeg.exe
            }

            // 클래스패스에서 ffmpeg 파일을 읽음
            inputStream = SpeechtoTextController.class.getClassLoader().getResourceAsStream("bin/" + ffmpegFileName);

            if (inputStream == null) {
                return null;  // 클래스패스에서 ffmpeg 파일을 찾을 수 없으면 null 반환
            }

            // 임시 파일로 저장
            File tempFile = File.createTempFile("ffmpeg", os.contains("win") ? ".exe" : "");
            tempFile.deleteOnExit();  // 종료 시 임시 파일 삭제

            outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return tempFile.getAbsolutePath();  // 임시 파일의 절대 경로 반환

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}