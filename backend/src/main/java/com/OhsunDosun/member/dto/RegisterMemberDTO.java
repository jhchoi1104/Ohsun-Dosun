package com.OhsunDosun.member.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RegisterMemberDTO {
    private String id;          // id=username
    private String password;    // password
    private String name;        // 사용자 이름
    private String email;       // 이메일

    public Member toMember() {
        Member member = new Member();
        member.setId(this.id);
        member.setPassword(this.password); // 비밀번호 설정
        member.setName(this.name);
        member.setEmail(this.email);
        // 필요에 따라 추가 필드 설정
        return member;
    }
}
