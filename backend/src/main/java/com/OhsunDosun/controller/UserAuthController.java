package com.OhsunDosun.controller;

import com.OhsunDosun.dto.UserAuth;
import com.OhsunDosun.service.conversation.task.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {
    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }


@PostMapping
public ResponseEntity<String> verifyPassword(@RequestBody UserAuth userAuth) {
    // 비밀번호 검증 서비스 호출하기
    boolean isVerified = userAuthService.verifyPassword(userAuth.getAccount_id(), userAuth.getAccount_password());
    if (isVerified) {
        return ResponseEntity.ok("Password is Correct");
    } else {
        return ResponseEntity.status(403).body("Password is Incorrect");
    }
}

}

