package com.OhsunDosun.service.conversation;

import com.OhsunDosun.dto.Log;
import com.OhsunDosun.exception.ChatbotException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PromptService {
    
    private final ResourceLoader resourceLoader;

    public PromptService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * <pre>
     * 메소드명   : chatbotPrompt (오버로드)
     * 설명       : 여러 프롬프트 파일을 로드하여 기본 챗봇 프롬프트를 생성. 추가 정보와 현재 날짜가 포함됨.
     * </pre>
     * @param promptFilePathList 프롬프트 파일 경로 리스트
     * @param input 사용자의 입력값
     * @param conversationLogs 대화 로그 리스트
     * @param additionalInfo 추가 정보
     * @return 프롬프트 메시지를 포함한 리스트
     */
    public List<Map<String, String>>
    chatbotPrompt(
            List<String> promptFilePathList,
            String input,
            List<Log> conversationLogs,
            String additionalInfo
    ) {

        String systemPrompt = loadPrompts(promptFilePathList) + "\n" + additionalInfo + getCurrentDate();

        return makePrompt(systemPrompt, input, conversationLogs);
    }

    /**
     * <pre>
     * 메소드명   : makePrompt
     * 설명       : 주어진 시스템 프롬프트, 사용자 입력, 대화 로그를 기반으로 최종 프롬프트 메시지 리스트를 생성.
     * </pre>
     * @param systemPrompt 시스템 프롬프트 내용
     * @param input 사용자의 입력값
     * @param conversationLogs 대화 로그 리스트
     * @return 프롬프트 메시지를 포함한 리스트
     */
    private List<Map<String, String>> makePrompt(String systemPrompt, String input, List<Log> conversationLogs) {
        List<Map<String, String>> messagesList = new ArrayList<>();

        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", systemPrompt);
        messagesList.add(systemMessage);

        // 대화 로그 추가
        for (Log log : conversationLogs) {
            Map<String, String> message = new HashMap<>();

            // log.getSenderType()에 따라 role을 할당
            if ("User".equals(log.getSenderType())) {
                message.put("role", "user");
                message.put("content", log.getMessageText());  // getMessageText()가 내용입니다.
            } else if ("Bot".equals(log.getSenderType())) {
                message.put("role", "assistant");
                message.put("content", log.getMessageText());  // getMessageText()가 내용입니다.
            }

            // messagesList에 메시지 추가
            messagesList.add(message);
        }

        // 사용자 입력 메시지 추가
        Map<String, String> userMessage1 = new HashMap<>();
        userMessage1.put("role", "user");
        userMessage1.put("content", input);
        messagesList.add(userMessage1);

        return messagesList;
    }

    /**
     * <pre>
     * 메소드명   : loadPrompts
     * 설명       : 주어진 파일 경로 리스트를 기반으로 모든 프롬프트를 로드하여 하나의 문자열로 결합.
     * </pre>
     * @param promptFilePathList 프롬프트 파일 경로 리스트
     * @return 결합된 프롬프트 내용
     */
    public String loadPrompts(List<String> promptFilePathList) {
        try {
            return promptFilePathList.stream()
                    .map(this::loadPrompt)
                    .collect(Collectors.joining("\n"));
        } catch (RuntimeException e) {
            throw new ChatbotException("Failed to load prompts", e);
        }
    }

    /**
     * <pre>
     * 메소드명   : loadPrompt
     * 설명       : 주어진 파일 경로에서 프롬프트를 로드하여 문자열로 반환.
     * </pre>
     * @param promptFilePath 프롬프트 파일 경로
     * @return 로드된 프롬프트 내용
     * @throws ChatbotException 프롬프트 로드 실패 시 발생
     */
    public String loadPrompt(String promptFilePath) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + promptFilePath);
            Path path = Paths.get(resource.getURI());
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ChatbotException("Failed to load prompt from: " + promptFilePath, e);
        }
    }

    public String getCurrentDate(){
        // Prompt에 현재 날짜 추가
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "\nToday's date: " + LocalDate.now().format(formatter);
    }

}
