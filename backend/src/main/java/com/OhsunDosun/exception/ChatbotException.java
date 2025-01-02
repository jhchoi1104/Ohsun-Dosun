package com.OhsunDosun.exception;

public class ChatbotException extends RuntimeException {
    public ChatbotException(String message, Throwable cause) {
        super(message, cause);
    }
}
