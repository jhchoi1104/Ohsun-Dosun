package com.OhsunDosun.member.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MemberDTO {
    private long userId;
    private String username;
    private String password;
    private String name;
    private String age;
    private String gender;
    private String ssn;

    public Member toMember() {
        return Member.builder().userId(userId).username(username).password(password).name(name).age(age).gender(gender).ssn(ssn).build();
    }
}
