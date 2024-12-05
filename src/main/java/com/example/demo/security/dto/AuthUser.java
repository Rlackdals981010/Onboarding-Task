package com.example.demo.security.dto;

import com.example.demo.domain.user.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Getter
public class AuthUser {

    private final long userId;
    private final String username;
    private final String nickname;

    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(long userId, String username,String nickname, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
    }
}