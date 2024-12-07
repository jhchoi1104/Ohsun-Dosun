package com.OhsunDosun._config;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@Log4j
@MapperScan
@ComponentScan
@RequiredArgsConstructor
public class SecurityConfig {
 @Bean
    public BCryptPasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
 }
}
