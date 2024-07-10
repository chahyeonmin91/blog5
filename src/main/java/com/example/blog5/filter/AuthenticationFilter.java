package com.example.blog5.filter;

import com.example.blog5.model.User;
import com.example.blog5.service.UserService;
import com.example.blog5.util.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthenticationFilter implements Filter {

    private final UserService userService;

    public AuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화 코드 (필요한 경우)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 로그인 페이지와 등록 페이지에 대해서는 필터 적용 안 함
        String uri = httpRequest.getRequestURI();
        if (uri.startsWith("/login") || uri.startsWith("/register")) {
            chain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("auth")) {
                    String username = cookie.getValue();
                    User user = userService.findByUsername(username);
                    if (user != null) {
                        UserContext.setUser(user);
                    }
                }
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }

    @Override
    public void destroy() {
        // 정리 코드 (필요한 경우)
    }
    private String validateTokenAndGetUserId(String token) {
        // 토큰 검증 및 사용자 ID 추출 로직 (예: JWT 검증)
        // 유효한 경우 사용자 ID를 반환, 그렇지 않으면 null 반환
        return null;
    }

}
