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

    public Member toMember() {
        return Member.builder().userId(userId).username(username).password(password).build();
    }
}
