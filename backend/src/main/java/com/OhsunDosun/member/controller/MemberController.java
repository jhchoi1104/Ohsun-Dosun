package com.OhsunDosun.member.controller;

import com.OhsunDosun.common.util.UploadFiles;
import com.OhsunDosun.member.dto.ChangePasswordDTO;
import com.OhsunDosun.member.dto.Member;
import com.OhsunDosun.member.dto.MemberDTO;
import com.OhsunDosun.member.dto.RegisterMemberDTO;
import com.OhsunDosun.member.service.MemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Api(value = "MemberController", tags = "멤버 정보")
@PropertySource({"classpath:/application.properties"})
public class MemberController {

    @Value("#{'${os_type}' == 'win' ? '${file_save_location_win}':'${file_save_location_other}'}")
    public String LOCATION;

    private final MemberService service;

    @GetMapping("/checkid/{id}")
    public ResponseEntity<Boolean> checkDuplicate(@PathVariable String id) {
        return ResponseEntity.ok().body(service.checkDuplicate(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> get(@PathVariable String id) {
        return ResponseEntity.ok(service.getMember(id));
    }

    @GetMapping("/{id}/avatar")
    public void getAvatar(@PathVariable String id, HttpServletResponse response) {
        // 클래스패스를 기준으로 파일 경로 설정
        String baseDir = "uploads/profileImg/"; // src/main/resources/uploads/profileImg/는 자동으로 클래스패스에 포함됩니다.
        String avatarPath = baseDir + id + ".png";
        URL resource = getClass().getClassLoader().getResource(avatarPath); // 리소스 가져오기
        File file = null;

        if (resource != null) {
            file = new File(resource.getFile());
        } else {
            // 해당 파일이 없으면 기본 이미지로 대체
            resource = getClass().getClassLoader().getResource(baseDir + "unknown.png");
            if (resource != null) {
                file = new File(resource.getFile());
            }
        }

        if (file != null && file.exists()) {
            UploadFiles.downloadImage(response, file);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 파일이 존재하지 않을 경우 404 상태 코드 반환
        }
    }

    @PostMapping("")
    public ResponseEntity<Member> join(RegisterMemberDTO memberDTO,
                                       @RequestParam(name = "avatar", required = false) MultipartFile avatar) throws IllegalAccessException {
        Member member = memberDTO.toMember();

        return ResponseEntity.ok(service.join(member, avatar));
    }

    @PutMapping("/{id}/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePassword) {
        service.changePassword(changePassword);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> changeProfile(MemberDTO memberDTO,
                @RequestParam(name = "avatar", required = false) MultipartFile avatar) throws IllegalAccessException {
        Member member = memberDTO.toMember();
        System.out.println(member);
        System.out.println(avatar);


        return ResponseEntity.ok(service.update(member, avatar));
    }
//    @PutMapping("/{id}")
//    public ResponseEntity<Member> changeProfileName(@RequestBody MemberDTO memberDTO,
//                                                @RequestParam(name = "avatar", required = false) MultipartFile avatar) throws IllegalAccessException {
//        System.out.println("Received memberDTO: " + memberDTO);
//        Member member = memberDTO.toMember();
//        return ResponseEntity.ok(service.updateName(member, avatar));
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Member> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
