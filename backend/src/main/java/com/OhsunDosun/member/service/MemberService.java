package com.OhsunDosun.member.service;

import com.OhsunDosun.member.dto.Auth;
import com.OhsunDosun.member.dto.ChangePasswordDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.OhsunDosun.member.dto.Member;
import com.OhsunDosun.member.exception.PasswordMissmatchException;
import com.OhsunDosun.member.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource({"classpath:/application.properties"})
public class MemberService{

    @Value("#{'${os_type}' == 'win' ? '${file_save_location_win}':'${file_save_location_other}'}")
    public String LOCATION;
    String baseDir = "uploads/profileImg/";


    final PasswordEncoder passwordEncoder;
    final MemberMapper mapper;


    public Member login(Member member) {
        Member saveMember = mapper.selectById(member.getId());
        if(passwordEncoder.matches(member.getPassword(), saveMember.getPassword())) {
            saveMember.setPassword("");
            saveMember.setMno(0);
            return saveMember;
        }else{
            return null;
        }
    }

    public boolean checkDuplicate(String id) {
        Member member = mapper.selectById(id);
        return member != null;
    }

    public Member getMember(String id) {
        return Optional.ofNullable(mapper.selectById(id))
                        .orElseThrow(NoSuchElementException::new);
    }

    private void saveAvatar(MultipartFile avatar, String id) {
        // 아바타 업로드
        if (avatar != null && !avatar.isEmpty()) {
            // 실제 파일을 저장할 경로 설정
            String baseDir = getClass().getClassLoader().getResource("/uploads/profileImg/").getPath(); // 리소스 가져오기

            System.out.println("@@@@@@@@@@@@@@@@@@@" + baseDir);
            // 프로젝트 루트 경로에 기반한 절대 경로를 설정
            File dir = new File(baseDir);

            // 디렉토리가 없으면 생성
            if (!dir.exists()) {
                dir.mkdirs(); // 디렉토리가 없으면 생성
                System.out.println("디렉토리 생성!");
            }

            File dest = new File(dir, id + ".png"); // 직접 baseDir에 저장

            // 기존 파일이 있다면 삭제
            if (dest.exists()) {
                dest.delete();
                System.out.println("기존 파일이 있다면 삭제!");
            }

            try {
                System.out.println("저장 경로 : " + dest.getAbsolutePath());
                avatar.transferTo(dest); // 파일 전송
                System.out.println("파일 전송! ");
            } catch (IOException e) {
                System.err.println("Error transferring file to destination: " + dest.getAbsolutePath());
                throw new RuntimeException("Failed to save avatar: " + e.getMessage(), e);
            }
        } else {
            System.out.println("No avatar provided or avatar is empty.");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public Member join(Member member, MultipartFile avatar) throws IllegalAccessException {
        if(member.checkRequiredValue()){
            throw new IllegalAccessException();
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        int result = mapper.insertMember(member);
        if(result != 1){
            throw new IllegalAccessException();
        }
        log.info("member joined successfully");
        long mno = member.getMno();
        log.info("mno is {}",mno);

        Auth auth = new Auth(member.getMno(), "ROLE_MEMBER");
        result = mapper.insertAuth(auth);
        if(result != 1){
            throw new IllegalAccessException();
        }
        saveAvatar(avatar, member.getUsername());
        log.info("join------------");
        return mapper.selectById(member.getId());
    }

    public Member update(Member updateMember, MultipartFile avatar) throws IllegalAccessException {
//        Member oldMember = mapper.selectById(updateMember.getId());
//        if(!passwordEncoder.matches(updateMember.getPassword(),oldMember.getPassword())) {
//            throw new PasswordMissmatchException();
//        }
//        updateMember.setMno(oldMember.getMno());
        mapper.updateMember(updateMember);
        if(avatar != null && !avatar.isEmpty()) {
            saveAvatar(avatar, updateMember.getId());
        }
        return mapper.selectById(updateMember.getId());
    }

    public Member updateName(Member updateMember, MultipartFile avatar) throws IllegalAccessException {
        Member oldMember = mapper.selectById(updateMember.getId());
        System.out.println(updateMember.getId());
        updateMember.setMno(oldMember.getMno());
        mapper.updateMemberName(updateMember);
        if(avatar != null && !avatar.isEmpty()) {
            saveAvatar(avatar, oldMember.getId());
        }
        return mapper.selectById(updateMember.getId());
    }

    public Member delete(String id) {
        Member member = mapper.selectById(id);
        mapper.deleteMember(member.getMno());
        return member;
    }

    public void changePassword(ChangePasswordDTO changePassword) {
        Member member = mapper.selectById(changePassword.getId());
//        System.out.println(changePassword);
        if(!passwordEncoder.matches(
                changePassword.getOldPassword(),
                member.getPassword()
        )) {
              throw new PasswordMissmatchException();
        }
        changePassword.setNewPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        mapper.updatePassword(changePassword);
    }
}
