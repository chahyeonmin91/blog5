package com.example.blog5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/login", "/error").permitAll()  // 로그인 페이지와 에러 페이지는 누구나 접근 가능
                        .anyRequest().authenticated()  // 그 외 모든 요청은 인증 필요
                )
                .formLogin((form) -> form
                        .loginPage("/login")  // 사용자 정의 로그인 페이지
                        .defaultSuccessUrl("/home", true)  // 로그인 성공 후 리디렉션할 페이지
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());  // 로그아웃 허용


        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
