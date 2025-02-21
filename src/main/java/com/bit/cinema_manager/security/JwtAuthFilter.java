package com.bit.cinema_manager.security;

import com.bit.cinema_manager.service.UserDetailServiceImpl;
import com.bit.cinema_manager.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil JWT_UTIL;
    private final UserDetailServiceImpl USER_DETAILS_SERVICE;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        System.out.println(token);
        if(token != null && token.startsWith("Bearer")) {
            token = token.substring(7);
            String username = JWT_UTIL.validateToken(token);
            if(username != null) {
                // UserDetails 객체 생성 후 UserDetailsService의 loadByUsername을 통해 해당
                // username을 가진 사용자가 있는지 체크
                // 체크 후에 존재하면 해당 유저를 SecurityContextHolder에 등록
                UserDetails details = USER_DETAILS_SERVICE.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}