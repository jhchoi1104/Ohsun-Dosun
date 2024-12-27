package com.OhsunDosun.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // JSON의 다른 필드를 무시
public class ChatCompletionChunk {
    private List<Choice> choices;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private Delta delta;
        private String finish_reason;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Delta {
            private String content; // 응답 텍스트 데이터
        }
    }
}
