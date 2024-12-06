package com.example.demo.domain.user.dto.response;

import lombok.Getter;

@Getter
public class SignResponseDto {
    private String token;

    public SignResponseDto(String token) {
        this.token = token;
    }
}
