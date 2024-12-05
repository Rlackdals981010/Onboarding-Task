package com.example.demo.domain.user.dto.request;

import lombok.Getter;

@Getter
public class SignUpRequestDto {
    private String username;
    private String password;
    private String nickname;
}
