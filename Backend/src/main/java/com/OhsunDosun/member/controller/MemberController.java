package com.OhsunDosun.member.controller;

import com.OhsunDosun.member.service.MemberService;
import com.OhsunDosun.member.dto.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@PropertySource({"classpath:/application.properties"})
public class MemberController {
    private final MemberService service;

    @GetMapping("/{userId}")
    public ResponseEntity<Member> get(@PathVariable String userId) {
        Member member = service.getMember(userId);
        return ResponseEntity.ok(member);
    }
}
