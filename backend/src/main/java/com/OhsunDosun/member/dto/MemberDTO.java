package com.OhsunDosun.member.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MemberDTO {
    private String id;          // id=username
    private String password;    // password
    private String name;        // 사용자 이름
    private String email;       // 이메일
    private String phone;       // 전화번호
    private Integer goalAmount; // 목표 금액
    private String message;     // 메시지
    private String gender;      // 성별
    private Long mno;           // 회원 고유번호

    public Member toMember() {
        Member member = new Member();
        member.setName(this.name);
        member.setMno(this.mno);
        member.setId(this.id);
        member.setEmail(this.email);
        member.setPhone(this.phone);
        member.setGoalAmount(this.goalAmount);
        member.setMessage(this.message);
        member.setGender(this.gender); // 성별 추가
        // 나머지 필드 매핑
        return member;
    }}
