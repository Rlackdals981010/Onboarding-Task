package com.example.demo.security.dto;

public sealed interface JwtUtilRequest permits JwtUtilRequest.CreateToken {
    record CreateToken(
            String username,
            String nickname
    ) implements JwtUtilRequest {}
}