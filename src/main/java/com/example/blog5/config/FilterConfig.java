package com.example.blog5.config;


import com.example.blog5.filter.AuthenticationFilter;
import com.example.blog5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
    private UserService userService;

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService);
        registrationBean.setFilter(authenticationFilter);
        registrationBean.addUrlPatterns("/*"); // 모든 경로에 필터 적용
        registrationBean.setOrder(1); // 필터 실행 순서 설정 (낮을수록 먼저 실행)
        return registrationBean;
    }
}
