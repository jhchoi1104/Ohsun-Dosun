package com.OhsunDosun.service.stt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
public class SpeechService {

    @Value("${clova.secret.key}")
    private String secretKey;  // 클로바 클라이언트 Secret

    @Value("${clova.invoke.url}")
    private String invokeUrl;  // 클로바 음성 인식 Invoke URL

    private CloseableHttpClient httpClient = HttpClients.createDefault();

    //Fileuploadcontroller에 대한 서비스 처리
    public String convertSpeechToTextFromffmpeg(File file) throws  IOException {
        String result = callClovaSpeechAPI(file);
        return result;
    }
    private String callClovaSpeechAPI(File audioFile) throws IOException {
        System.out.println("확인!!!!!!!!!!!!");
        HttpPost httpPost = new HttpPost(invokeUrl + "/recognizer/upload");
        // params 설정 (JSON으로 직렬화할 Map)
        Map<String, Object> params = Map.of(
                "language", "ko-KR",
                "completion", "sync",
                "diarization", Map.of(
                        "enable", false // 화자 인식 비활성화
                )
        );
        // params를 JSON 문자열로 직렬화
        String paramsJson = new ObjectMapper().writeValueAsString(params);

        // Request Body에 오디오 파일과 params 포함한 multipart/form-data 설정
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addBinaryBody("media", audioFile, ContentType.MULTIPART_FORM_DATA, audioFile.getName())
                .addTextBody("params", paramsJson, ContentType.APPLICATION_JSON)
                .build();
        httpPost.setEntity(httpEntity);

        // API 요청을 위한 요청 헤더 설정
        Header[] headers = new Header[]{
                new BasicHeader("Accept", "application/json"),
                new BasicHeader("X-CLOVASPEECH-API-KEY", secretKey)
        };
        httpPost.setHeaders(headers);

        System.out.println("=== HTTP POST REQUEST INFO ===");
        System.out.println("URI: " + httpPost.getURI());
        System.out.println("Headers:");
        for (Header header : httpPost.getAllHeaders()) {
            System.out.println("  " + header.getName() + ": " + header.getValue());
        }
        System.out.println("Params: " + paramsJson);  // Log the params directly
        System.out.println("File: " + audioFile.getName());  // Log the file name
        String mimeType = Files.probeContentType(audioFile.toPath());
        System.out.println("Detected MIME Type: " + mimeType);

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IOException("Error during speech recognition", e);
        }
    }


}