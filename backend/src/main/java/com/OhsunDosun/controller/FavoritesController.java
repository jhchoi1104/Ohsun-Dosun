package com.OhsunDosun.controller;

import com.OhsunDosun.dto.Favorites;
import com.OhsunDosun.service.conversation.task.FavoritesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/nickname")
public class FavoritesController {
    private final FavoritesService favoritesService;

    /**
     * 송금 대상 별칭 저장
     *
     * @param userId   사용자 ID
     * @param nickname 별칭 요청 데이터 (JSON)
     * @return 성공 메시지
     */
    @PostMapping
    public ResponseEntity<String> saveNickname(
            @RequestHeader("userId") long userId,
            @RequestBody Favorites nickname) {
        // 별칭 존재 여부 체크
        if (favoritesService.isFavoriteExists(userId, nickname.getNickname())) {
            return ResponseEntity.status(HttpStatus.CREATED).body("별칭이 이미 존재합니다.");
        }
        // 별칭 없을 시, 새로운 별칭 저장
        else {
            favoritesService.saveNickname(userId, nickname);
            log.info("별칭 저장 완료 - User: {}, Nickname: {}", userId, nickname.getNickname());

            return ResponseEntity.status(HttpStatus.CREATED).body("별칭 저장 완료");
        }
    }
}
