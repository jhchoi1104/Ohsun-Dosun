package com.OhsunDosun.service.stt;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class SpeechService {

    @Value("${google.cloud.credentials.path}")
    private String credentialsPath;  // 인증 파일 경로

    public String convertSpeechToText(MultipartFile file) throws Exception {
        InputStream fileInputStream = file.getInputStream();

        // 서비스 계정 인증 파일을 통해 인증 처리
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
        SpeechSettings speechSettings = SpeechSettings.newBuilder()
                .setCredentialsProvider(() -> credentials)
                .build();

        // Google Speech-to-Text API 클라이언트 초기화
        try (SpeechClient speechClient = SpeechClient.create()) {
            // MP3를 LINEAR16 PCM 형식으로 변환
            AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false); // 16kHz, 16-bit, mono channel
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileInputStream);
            AudioInputStream convertedAudioStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);

            ByteString audioBytes = ByteString.readFrom(convertedAudioStream);

            // 요청 구성
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16) // LINEAR16으로 설정
                    .setSampleRateHertz(16000) // 샘플 레이트 설정
                    .setLanguageCode("ko-KR") // 한국어 언어 코드
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            // API 호출 및 응답 처리
            RecognizeResponse response = speechClient.recognize(config, audio);
            StringBuilder transcription = new StringBuilder();
            for (SpeechRecognitionResult result : response.getResultsList()) {
                transcription.append(result.getAlternatives(0).getTranscript());
            }
            return transcription.toString();
        }
    }
}