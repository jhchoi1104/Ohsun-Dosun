package com.OhsunDosun.member.controller;

import com.OhsunDosun.member.dto.MemberDTO;
import com.OhsunDosun.member.service.MemberService;
import com.OhsunDosun.member.dto.Member;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Api(value = "MemberController", tags = "멤버 정보")
@PropertySource({"classpath:/application.properties"})
public class MemberController {
    private final MemberService service;

    @GetMapping("/{username}")
    public ResponseEntity<Member> get(@PathVariable String username) {
        log.info("회원 조회 요청: username={}", username);
        try {
            Member member = service.getMember(username);
            log.info("회원 조회 성공: {}", member);
            return ResponseEntity.ok(member);
        } catch (NoSuchElementException e) {
            log.error("회원 조회 실패: username={}, message={}", username, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("")
    public ResponseEntity<Member> join(@RequestBody MemberDTO memberDTO) throws IllegalAccessException {
        log.info("회원 가입 요청: {}", memberDTO);
        try {
            Member member = memberDTO.toMember();
            log.info("DTO 변환 성공: {}", member);
            Member createdMember = service.join(member);
            log.info("회원 가입 성공: {}", createdMember);
            return ResponseEntity.status(201).body(createdMember);
        } catch (IllegalAccessException e) {
            log.error("회원 가입 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch(Exception e){
            log.error("회원 가입 중 예기치 못한 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

