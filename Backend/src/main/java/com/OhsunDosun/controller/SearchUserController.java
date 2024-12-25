package com.OhsunDosun.controller;

import com.OhsunDosun.dto.SearchUserDTO;
import com.OhsunDosun.service.conversation.task.SearchUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class SearchUserController {

    private final SearchUserService userService;

    // 사용자 이름으로 ID 조회
    @GetMapping("/findByName")
    public ResponseEntity<SearchUserDTO> findUserByName(@RequestParam String name) {
        SearchUserDTO user = userService.findUserIdByName(name);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
