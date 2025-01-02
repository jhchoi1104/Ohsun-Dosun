package com.OhsunDosun.member.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Member implements UserDetails {
    private long userId;
    private String username;
    private String password;
    private String name;
    private String age;
    private String gender;
    private String ssn;
    private String status = "Y";
    private String token;
    private List<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //Spring Security에서 사용자 권한 목록을 반환.
        return authorities;
    }

    public String setUsername(String username) {
        return this.username = username;
    }

    public String setname(String name) {
        return this.name=name;
    }

    @Override
    public  String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status.equalsIgnoreCase("Y");
    }

    public boolean checkRequiredValue(){
        try {
            return (username.isEmpty() || password.isEmpty() || name.isEmpty() || age.isEmpty() || gender.isEmpty() || ssn.isEmpty());
        }catch (Exception e){
            return false;
        }
    }
}
