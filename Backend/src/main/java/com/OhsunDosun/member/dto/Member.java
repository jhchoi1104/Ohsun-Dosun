package com.OhsunDosun.member.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor //매개변수가 없는 기본 생성자를 생성합니다. 객체 초기화 시 기본 생성자를 필요로 할 경우 유용.
@AllArgsConstructor //클래스의 모든 필드를 매개변수로 받는 생성자를 자동으로 생성.
@Getter //클래스의 모든 필드에 대해 getter 메서드를 자동으로 생성.
@Setter //클래스의 모든 필드에 대해 setter 메서드를 자동으로 생성
@Builder //빌더 패턴을 사용하여 객체를 생성할 수 있도록 지원
public class Member implements UserDetails {
    private long userId;       //userPk(고유식별자)
    private String username;     //username
    private String password;     //userpassword
    private String status = "Y"; //계정 활성화 여부. 인증 및 권한 부여 과정 isEnabled()메서드로 확인.

    private String token; // JWT 토큰값, DB에는 저장하지 않음. 클라이언트-서버 간 통신

    // 복수개의 권한을 관리
    // 문자열data("ROLE_USER" , "ROLE_ADMIN")를 처리할 수 있는 GrantedAuthority의 하위클래스
    private List<SimpleGrantedAuthority> authorities; //authorities

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //Spring Security에서 사용자 권한 목록을 반환.
        return authorities;
    }

    public String setUsername(String username) {
        return this.username = username;
    } //username의 커스텀 메서드. username 대신 userId를 설정하고 반환하도록 설계함. why?? 고유식별자인 userId로 설계하면 검색의 조회의 기준이 더 명확하다.

    @Override
    public  String getUsername() {
        return username;
    } //UserDetails 인터페이스에서 요구하는 메서드로, Spring Security가 사용자명을 확인할 때 호출. userId를 사용자명으로 사용하도록 설정.

    @Override
    public boolean isAccountNonExpired() {
        return true;
    } //계정의 만료 여부를 반환. true를 반환하면 만료되지 않은 상태로 간주. 여기서는 항상 true를 반환하므로 만료 기능을 사용하지 않음을 나타냄.

    @Override
    public boolean isAccountNonLocked() {
        return true;
    } // 계정이 잠겨 있는지 여부를 반환합니다. true를 반환하면 계정이 잠겨 있지 않음을 의미합니다. 여기서도 잠금 기능을 사용하지 않으므로 항상 true.

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    } // 자격 증명(비밀번호 등)이 만료되지 않았는지를 확인. 항상 true를 반환하므로 만료 기능을 사용하지 않음.

    @Override
    public boolean isEnabled() {
        return status.equalsIgnoreCase("Y");
    } //계정이 활성화 상태인지 여부를 반환. Spring Security의 인증 및 권한 부여 과정에서 사용.
    public boolean checkRequiredValue(){
        try {
            return (username.isEmpty() || password.isEmpty());
        }catch (Exception e){
            return false;
        }
    }

}
