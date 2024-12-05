package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.enums.UserRole;

import java.util.List;

public sealed interface UserResponse permits UserResponse.Signup, UserResponse.Sign {

    // 회원 정보 조회 시 응답
    record Signup(
            String username,
            String nickname,
            List<AuthorityResponse> authorities
    ) implements UserResponse {}

    record AuthorityResponse(
            String authorityName
    ) {}

    // 회원 정보 수정 시 응답
    record Sign(
            String token
    ) implements UserResponse {}
}