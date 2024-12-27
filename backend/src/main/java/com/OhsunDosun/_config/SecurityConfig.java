package com.OhsunDosun._config;

import com.OhsunDosun.security.fillter.JwtUsernamePasswordAuthenticationFilter;
import com.OhsunDosun.security.fillter.JwtAuthenticationFilter;
import com.OhsunDosun.security.fillter.AuthenticationErrorFilter;
import com.OhsunDosun.security.handler.CustomAuthenticationEntryPoint;
import com.OhsunDosun.security.handler.CustomAccessDeniedHandler;
import lombok.extern.log4j.Log4j;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@Log4j
@MapperScan(basePackages = {"com.OhsunDosun"})
@ComponentScan(basePackages = {"com.OhsunDosun.security"})
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter;

    private final UserDetailsService userDetailsService;
    //private final AuthenticationErrorFilter authenticationErrorFilter; //권한 필터
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationErrorFilter authenticationErrorFilter;

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public static final String[] ALLOWED_URLS = {
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/api/v1/posts/**",
            "/api/v1/replies/**",
            "/login",
            "/auth/login/kakao/**"  // 여기에 카카오 로그인 URL 추가
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
 }

    // AuthenticationManager 빈 등록
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    // Authentication Manager 구성
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
    // cross origin 접근 허용
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); //모든 origin을 허용
        config.addAllowedHeader("*"); //모든 헤더를 허용
        config.addAllowedMethod("*"); //모든 메서드를 허용
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // 접근 제한 무시 경로 설정 – resource
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**", "/*", "/api/member/**");
    }

    // 문자셋 필터
    public CharacterEncodingFilter encodingFilter() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        return encodingFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 한글 인코딩 필터 설정
        http.cors().and()
                .addFilterBefore(encodingFilter(), CsrfFilter.class)
                // Jwt 인증 필터
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 인증 에러 필터
                .addFilterBefore(authenticationErrorFilter, UsernamePasswordAuthenticationFilter.class)
                // 로그인 인증 필터
                .addFilterBefore(jwtUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 예외 처리 설정
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        http
                .authorizeRequests()
                .antMatchers(ALLOWED_URLS).permitAll()  // 허용 URL 추가
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.POST, "/api/member").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/member", "/api/member/*/changepassword").authenticated()
                .antMatchers(HttpMethod.POST, "/api/board/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/board/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/board/**").authenticated()
                .anyRequest().permitAll()
        ;

        http.httpBasic().disable()        // 기본 HTTP 인증 비활성화
                .csrf().disable()       // CSRF 비활성화
                .formLogin().disable()  // formLogin 비활성화  관련 필터 해제
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션 생성 모드 설정
    }
}

