package com.OhsunDosun.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auth implements GrantedAuthority {
    private long mno;
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
