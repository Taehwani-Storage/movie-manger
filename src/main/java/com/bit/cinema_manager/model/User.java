package com.bit.cinema_manager.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class User implements UserDetails {
    private int id;
    private int userNo;
    private String username;
    private String password;
    private String nickname;
    private String role; // 기본값 일반 회원
    private Collection<GrantedAuthority> authorities;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }
}
