package com.OhsunDosun.service.conversation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.cognitiveservices.speech.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class TextToSpeechService {

    @Value("${SPEECH_KEY}")
    private String speechKey;

    @Value("${SPEECH_REGION}")
    private String speechRegion;

    /**
     * <pre>
     * 메소드명   : convertTextToSpeech
     * 설명       : 텍스트를 음성으로 변환하는 Azure TTS API를 호출하여 음성 데이터를 반환한다.
     *
     * @param input   음성으로 변환할 텍스트 입력
     * @return        변환된 음성 데이터 (MP3 파일 형식의 byte 배열)
     * </pre>
     */
    public byte[] convertTextToSpeech(String input) {
        synchronized (this) {
            SpeechConfig speechConfig = null;
            SpeechSynthesizer synthesizer = null;

            try {
                // JSON 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(input);

                if (!jsonNode.has("input")) {
                    throw new IllegalArgumentException("JSON does not contain 'input' key");
                }

                String inputText = jsonNode.get("input").asText();

                // Azure Speech SDK 설정
                speechConfig = SpeechConfig.fromSubscription(speechKey, speechRegion);
                speechConfig.setSpeechSynthesisVoiceName("ko-KR-YuJinNeural");

                synthesizer = new SpeechSynthesizer(speechConfig);

                // SSML을 사용하여 음성 속도를 조정
                String ssml = "<speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xml:lang='ko-KR'>" +
                        "<voice name='ko-KR-YuJinNeural'>" +
                        "<prosody rate='" + "+20%" + "'>" +
                        inputText +
                        "</prosody>" +
                        "</voice>" +
                        "</speak>";

                // 비동기적으로 SSML 텍스트를 음성으로 변환
                SpeechSynthesisResult result = synthesizer.SpeakSsmlAsync(ssml).get();

                if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                    return result.getAudioData(); // 음성 데이터를 byte 배열로 반환
                } else if (result.getReason() == ResultReason.Canceled) {
                    SpeechSynthesisCancellationDetails cancellationDetails =
                            SpeechSynthesisCancellationDetails.fromResult(result);
                    throw new RuntimeException("TTS synthesis canceled: " + cancellationDetails.getErrorDetails());
                } else {
                    throw new RuntimeException("Unexpected TTS result reason: " + result.getReason());
                }
            } catch (Exception e) {
                throw new RuntimeException("Error during TTS synthesis", e);
            } finally {
                if (synthesizer != null) {
                    synthesizer.close();
                }
                if (speechConfig != null) {
                    speechConfig.close();
                }
            }
        }
    }

}
