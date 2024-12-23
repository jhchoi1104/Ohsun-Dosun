package com.OhsunDosun.kakao.controller;

import com.OhsunDosun.kakao.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/kakao")

public class KakaoController {

    private final KakaoService kakaoService;

    // application.properties에서 카카오 클라이언트 ID 읽어오기
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    // 카카오 로그인 콜백 URL (사용자가 카카오 로그인 후 리다이렉트되는 URI)
    @RequestMapping("/callback")
    @CrossOrigin(origins = "http://localhost:5173")  // 프론트엔드에서 오는 요청 허용
    public void kakaoCallback(@RequestParam String code, HttpSession session, HttpServletResponse response) {
        log.info("카카오 콜백 코드 수신: {}", code);

        try {

            // 1. 카카오 토큰 요청
            String accessToken = kakaoService.getAccessToken(code);
            log.info("카카오 액세스 토큰: {}", accessToken);

            // 2. 사용자 정보 요청
            Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
            log.info("카카오 사용자 정보: {}", userInfo);

            // 3. 사용자 정보를 세션에 저장 (세션 유지, 로그인 상태 저장)
            session.setAttribute("userInfo", userInfo);
            session.setAttribute("code", code);  // code 값을 세션에 저장하여 프론트엔드에서 확인 가능

            // 로그인 성공 메시지와 사용자 정보를 프론트엔드로 전달
            response.setStatus(HttpServletResponse.SC_OK); //응답 상태 코드 설정
            log.info("응답 상태 코드 설정: {}", HttpServletResponse.SC_OK);

            response.sendRedirect("http://localhost:5173/kakao/callback");
        } catch (IOException ioException) {
            log.error("IOException 발생: {}", ioException.getMessage());
        }
        catch (IllegalArgumentException e) {
            log.error("잘못된 요청: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                response.getWriter().write("잘못된 요청: " + e.getMessage());
            } catch (IOException ioException) {
                log.error("IOException 발생: {}", ioException.getMessage());
            }
        } catch (Exception e) {
            log.error("서버 오류 발생: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("카카오 로그인 처리 중 오류 발생");
            } catch (IOException ioException) {
                log.error("IOException 발생: {}", ioException.getMessage());
            }
        }
    }
    @RequestMapping("/userInfo")
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpSession session) {
        Map<String, Object> userInfo = (Map<String, Object>) session.getAttribute("userInfo");
        if (userInfo != null) {
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Map.of("error", "로그인 정보가 없습니다."));
        }
    }
}
