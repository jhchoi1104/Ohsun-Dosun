package com.OhsunDosun.member.service;

import com.OhsunDosun.member.dto.Member;
import com.OhsunDosun.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource({"classpath:/application.properties"})
public class MemberService {

    final PasswordEncoder passwordEncoder;
    final MemberMapper mapper;

    public Member login(Member member){
        Member saveMember = mapper.selectByName(member.getUsername());
        if(passwordEncoder.matches(member.getPassword(), saveMember.getPassword())){
            saveMember.setPassword("");//비밀번호 제거
            saveMember.setUserId(0); //회원번호 초기화
            return saveMember;
        }else{
            return null; //인증 실패
        }
    }

    public Member getMember(String username){
        return Optional.ofNullable(mapper.selectByName(username))
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional(rollbackFor = Exception.class)
    public Member join(Member member) throws IllegalAccessException{
        if(member.checkRequiredValue()){
            throw new IllegalAccessException("필수 값이 누락되었습니다."); //예외 처리
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));//비밀번호 암호화
        log.info("비밀번호 암호화 완료");

        int result = mapper.insertMember(member);
        log.info("회원 가입 DB 작업 결과: result={}", result);

        if(result != 1){ //result가 1이 아니면 실패
            throw new IllegalAccessException("회원 가입에 실패했습니다."); //예외 처리
        }
        return mapper.selectByName(member.getUsername());
    }
}
